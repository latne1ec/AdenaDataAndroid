package com.adenadata.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
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
public class NewsAdapterWithSearchAndState extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "NewsAdapterWithSearch";

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_LIST = 1;
    private static final int TYPE_LOADING = 2;
    private static final int TYPE_EMPTY = 3;
    private static final int TYPE_ERROR = 4;

    public enum State {
        LIST, LOADING, EMPTY, ERROR
    }

    ArrayList<New> mNews;
    SearchListener mSearchListener;
    State mState;

    // Needed to make loading, empty and error views full-screen
    Integer mHeight;


    public NewsAdapterWithSearchAndState(ArrayList<New> news, SearchListener searchListener, State state) {
        mNews = news;
        mSearchListener = searchListener;
        mState = state;
    }


    public void setState(State state) {
        mState = state;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (mState == State.LIST) {
            return mNews.size() + 1;
        } else {
            return 2;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            switch (mState) {
                case LIST:
                    return TYPE_LIST;
                case LOADING:
                    return TYPE_LOADING;
                case EMPTY:
                    return TYPE_EMPTY;
                case ERROR:
                    return TYPE_ERROR;
                default:
                    return TYPE_EMPTY;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeight == null) {
            // We have to subtract the search header's height (53dp) because parent includes it
            // Otherwise the list will scroll up with TYPE_LOADING/EMPTY/ERROR
            float searchHeaderHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 53, parent.getContext().getResources().getDisplayMetrics());
            mHeight = parent.getMeasuredHeight() - Math.round(searchHeaderHeight);
        }
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_header, parent, false);
            return new ViewHolderHeader(v);
        } else if (viewType == TYPE_LIST) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_new, parent, false);
            return new ViewHolderNew(v);
        } else if (viewType == TYPE_LOADING) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_loading, parent, false);
            return new ViewHolderLoading(v);
        } else {
            // TYPE_EMPTY and TYPE_ERROR
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_text, parent, false);
            return new ViewHolderText(v);
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
        } else if (mState == State.LIST) {
            // TYPE NEW
            final New theNew = mNews.get(position - 1);
            ViewHolderNew vh = (ViewHolderNew) viewHolder;
            vh.title.setText(theNew.getTitle());
            vh.location.setText(theNew.getLocation());
            String url = theNew.getImageUrl();
            if (url != null && !url.isEmpty()) {
                Glide.with(vh.image.getContext()).load(url).centerCrop().crossFade().into(vh.image);
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
                    intent.putExtra(NewActivity.EXTRA_FLAG_COUNT, theNew.getFlagCount());
                    context.startActivity(intent);
                }
            });
        } else if (mState == State.LOADING) {
            // TYPE LOADING
            ViewHolderLoading vh = (ViewHolderLoading) viewHolder;
            vh.frame.setMinimumHeight(mHeight);
        } else if (mState == State.EMPTY) {
            // TYPE EMPTY
            ViewHolderText vh = (ViewHolderText) viewHolder;
            vh.frame.setMinimumHeight(mHeight);
            vh.text.setText("No events now.\nCheck back soon!");
        } else if (mState == State.ERROR) {
            // TYPE ERROR
            ViewHolderText vh = (ViewHolderText) viewHolder;
            vh.frame.setMinimumHeight(mHeight);
            vh.text.setText("An error occurred.\nPlease try again.");
        }
    }


    // TYPE_HEADER
    static class ViewHolderHeader extends RecyclerView.ViewHolder {

        View view;
        EditText searchEditText;

        public ViewHolderHeader(View v) {
            super(v);

            view = v;
            searchEditText = (EditText) v.findViewById(R.id.search_header_editText);
        }
    }


    // TYPE_LIST
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


    // TYPE_LOADING
    static class ViewHolderLoading extends RecyclerView.ViewHolder {
        FrameLayout frame;
        public ViewHolderLoading(View v) {
            super(v);
            frame = (FrameLayout) v.findViewById(R.id.recyclerview_frame);
        }
    }


    // TYPE_EMPTY and TYPE_ERROR
    static class ViewHolderText extends RecyclerView.ViewHolder {

        FrameLayout frame;
        TextView text;

        public ViewHolderText(View v) {
            super(v);

            frame = (FrameLayout) v.findViewById(R.id.recyclerview_frame);
            text = (TextView) v.findViewById(R.id.recyclerview_text);
        }
    }

}

