package com.adenadata.android.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.adenadata.android.R;
import com.adenadata.android.model.New;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNewActivity extends AppCompatActivity {

    private static final String TAG = "AddNewActivity";

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;

    private ImageView mImageView;
    private EditText mTitleEditText;
    private EditText mLocationEditText;
    private EditText mTextEditText;

    private Bitmap mImageBitmap;

    private String mCurrentPhotoPath;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_new_toolbar);
        setSupportActionBar(toolbar);

        // Show back button (arrow)
        // Back arrow orange color http://stackoverflow.com/q/26788464/4034572
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitleEditText = (EditText) findViewById(R.id.add_new_title);
        mLocationEditText = (EditText) findViewById(R.id.add_new_location);
        mTextEditText = (EditText) findViewById(R.id.add_new_text);

        // Add photo
        mImageView = (ImageView) findViewById(R.id.add_new_image);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhotoClicked();
            }
        });

        // Share button (toolbar)
        TextView shareButton = (TextView) findViewById(R.id.add_new_toolbar_share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareButtonClicked();
            }
        });
    }


    // When click on photo
    private void addPhotoClicked() {
        // Let the user choose camera or gallery
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNewActivity.this);
        builder.setTitle("Add Picture");
        builder.setItems(new CharSequence[]{"Take Photo", "Choose From Gallery"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    // Camera
                    // http://developer.android.com/training/camera/photobasics.html
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            Log.i(TAG, "IOException");
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    }
                } else if (i == 1) {
                    // Gallery
                    // http://codetheory.in/android-pick-select-image-from-gallery-with-intents/
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    if (galleryIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
                    }
                }
            }
        });
        builder.show();
    }


    // Return from add photo intents
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Camera
            Log.i(TAG, "Get pic from camera");
            /* This only gets thumbnail
            Bundle extras = data.getExtras();
            mImageBitmap = (Bitmap) extras.get("data");
            */
            // Get full image - requires permission
//            mImageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, new BitmapFactory.Options());
            try {
                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                mImageView.setImageBitmap(mImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Gallery
            Uri uri = data.getData();
            try {
                mImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                mImageView.setImageBitmap(mImageBitmap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    // http://developer.android.com/training/camera/photobasics.html
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );


        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        Log.i(TAG, "mCurrentPhotoPath: " + mCurrentPhotoPath);
        return image;
    }


    private void shareButtonClicked() {
        // Hide keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        // Validate input
        if (mTitleEditText.getText().toString().trim().length() < 2) {
            mTitleEditText.setError("Please enter a title");
            return;
        }
        if (mLocationEditText.getText().toString().trim().length() < 2) {
            mLocationEditText.setError("Please enter a location");
            return;
        }
        if (mTextEditText.getText().toString().trim().length() < 2) {
            mTextEditText.setError("Please enter a text");
            return;
        }
        // Show ProgressDialog
        mProgressDialog = ProgressDialog.show(this, "Saving Post...", "Please wait.", true, false);
        // Use ad_thumbnail.jpeg if user didn't pick an image
        if (mImageBitmap == null) {
            mImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ad_thumbnail);
        }
        // Convert Bitmap to byte[]
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageByteArray = stream.toByteArray();
        // Save image file to Parse
        // (from Parse docs) - if you're storing PNG images, make sure your filename ends with .png
        final ParseFile imageFile = new ParseFile("picture.png", imageByteArray);
        imageFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    // Error -> show dialog
                    mProgressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddNewActivity.this);
                    builder.setTitle("Something went wrong");
                    builder.setMessage("Please try again.");
                    builder.setPositiveButton("OK", null);
                    builder.show();
                } else {
                    // Success -> save Post to Parse
                    New theNew = new New();
                    theNew.put("postTitle", mTitleEditText.getText().toString());
                    theNew.put("searchedTitle", mTitleEditText.getText().toString().toLowerCase());
                    theNew.put("postLocation", mLocationEditText.getText().toString());
                    theNew.put("postText", mTextEditText.getText().toString());
                    theNew.put("postImage", imageFile);
                    // TODO save smaller image to thumbnail??
                    theNew.put("postImageThumbnail", imageFile);
                    theNew.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            mProgressDialog.dismiss();
                            if (e != null) {
                                // Error
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddNewActivity.this);
                                builder.setTitle("Something went wrong");
                                builder.setMessage("Please try again.");
                                builder.setPositiveButton("OK", null);
                                builder.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddNewActivity.this);
                                builder.setTitle("Shared Post");
                                builder.setMessage("Post was saved successfully.");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // Go to MainActivity
                                        Intent intent = new Intent(AddNewActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                });
                                builder.show();
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
