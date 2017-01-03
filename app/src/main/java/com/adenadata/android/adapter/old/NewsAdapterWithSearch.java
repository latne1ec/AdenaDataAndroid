package com.adenadata.android.adapter.old;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.adenadata.android.R;
import com.adenadata.android.activity.NewActivity;
import com.adenadata.android.model.New;
import com.adenadata.android.util.SearchListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by albertvilacalvo on 29/7/15.
 */
public class NewsAdapterWithSearch extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "NewsAdapterWithSearch";

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_LIST = 1;

    ArrayList<New> mNews;
    SearchListener mSearchListener;

    public NewsAdapterWithSearch(ArrayList<New> news, SearchListener searchListener) {
        mNews = news;
        mSearchListener = searchListener;
    }


    @Override
    public int getItemCount() {
        return mNews.size() + 1;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_LIST;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_header, parent, false);
            return new ViewHolderHeader(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_new, parent, false);
            return new ViewHolderNew(v);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (position == 0) {
            // TYPE HEADER
            final ViewHolderHeader vhh = (ViewHolderHeader) viewHolder;
            if (vhh.searchEditText.getText().toString().length() > 0) {
                vhh.searchEditText.requestFocus();
            }
            vhh.searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        mSearchListener.performSearch(vhh.searchEditText.getText().toString().trim());
                        return true;
                    }
                    return false;
                }
            });
            vhh.searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mSearchListener.performSearch(vhh.searchEditText.getText().toString().trim());
                    if (charSequence.length() == 0) {
                        // Hide keyboard
                        InputMethodManager imm = (InputMethodManager) vhh.view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(vhh.view.getWindowToken(), 0);
                    }
                }
                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        } else {
            // TYPE NEW
            final New theNew = mNews.get(position - 1);
            ViewHolderNew vh = (ViewHolderNew) viewHolder;
            vh.title.setText(theNew.getTitle());
            vh.location.setText(theNew.getLocation());
            String url = theNew.getImageUrl();
            if (url != null && !url.isEmpty()) {
                Glide.with(vh.image.getContext()).load(url).centerCrop().placeholder(R.drawable.ad_thumbnail).crossFade().into(vh.image);
            }
            vh.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, NewActivity.class);
                    intent.putExtra(NewActivity.EXTRA_OBJECT_ID, theNew.getObjectId());
                    intent.putExtra(NewActivity.EXTRA_TITLE, theNew.getTitle());
                    intent.putExtra(NewActivity.EXTRA_LOCATION, theNew.getLocation());
                    intent.putExtra(NewActivity.EXTRA_TEXT, theNew.getText());
                    intent.putExtra(NewActivity.EXTRA_DATE, theNew.getDateString());
                    intent.putExtra(NewActivity.EXTRA_IMAGE_URL, theNew.getImageUrl());
                    context.startActivity(intent);
                }
            });
        }
    }



    public void showProgressWheel() {

    }


    public void showEmptyListText() {

    }


    static class ViewHolderHeader extends RecyclerView.ViewHolder {

        View view;
        EditText searchEditText;

        public ViewHolderHeader(View v) {
            super(v);

            view = v;
            searchEditText = (EditText) v.findViewById(R.id.search_header_editText);
        }
    }


    static class ViewHolderNew extends RecyclerView.ViewHolder {

        View view;
        TextView title;
        TextView location;
        ImageView image;

        public ViewHolderNew(View v) {
            super(v);

            view = v;
            title = (TextView) v.findViewById(R.id.list_item_new_title);
            location = (TextView) v.findViewById(R.id.list_item_new_location);
            image = (ImageView) v.findViewById(R.id.list_item_new_image);
        }
    }

}

