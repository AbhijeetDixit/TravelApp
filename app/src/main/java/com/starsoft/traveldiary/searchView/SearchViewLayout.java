/*
 * Copyright (C) 2015 Sahil Dave
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.starsoft.traveldiary.searchView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;
import com.starsoft.traveldiary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SearchViewLayout extends FrameLayout {
    public static int ANIMATION_DURATION = 1500;
    private static final String LOG_TAG = SearchViewLayout.class.getSimpleName();

    private boolean mIsExpanded = false;

    private ViewGroup mCollapsed;
    private ViewGroup mExpanded;
    private EditText mSearchEditText;
    private View mSearchIcon;
    private View mCollapsedSearchBox;
    private View mBackButtonView;
    private View mExpandedSearchIcon;

    private int toolbarExpandedHeight = 0;

    private ValueAnimator mAnimator;
    private OnToggleAnimationListener mOnToggleAnimationListener;
    private SearchListener mSearchListener;
    private SearchBoxListener mSearchBoxListener;
    private android.app.Fragment mExpandedContentFragment;
    private android.support.v4.app.Fragment mExpandedContentSupportFragment;
    private android.app.FragmentManager mFragmentManager;
    private android.support.v4.app.FragmentManager mSupportFragmentManager;
    private TransitionDrawable mBackgroundTransition;
    private Toolbar mToolbar;

    private Drawable mCollapsedDrawable;
    private Drawable mExpandedDrawable;

    private int mExpandedHeight;
    private int mCollapsedHeight;
    private TextView mCollapsedHintView;
    private ListView searchList;


    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);


    /***
     * Interface for listening to animation start and finish.
     * expanding and expanded tell the current state of animation.
     */
    public interface OnToggleAnimationListener {
        void onStart(boolean expanding);

        void onFinish(boolean expanded);
    }

    /***
     * Interface for listening to search finish call.
     * Called on clicking of search button on keyboard and {@link #mExpandedSearchIcon}
     */

    public interface SearchListener {
        void onFinished(String searchKeyword);
    }

    /***
     * Interface for listening to search edit text.
     */

    public interface SearchBoxListener {
        void beforeTextChanged(CharSequence s, int start, int count, int after);
        void onTextChanged(CharSequence s, int start, int before, int count);
        void afterTextChanged(Editable s);
    }


    public void setOnToggleAnimationListener(OnToggleAnimationListener listener) {
        mOnToggleAnimationListener = listener;
    }

    public void setSearchListener(SearchListener listener) {
        mSearchListener = listener;
    }

    public void setSearchBoxListener(SearchBoxListener listener) {
        mSearchBoxListener = listener;
    }

    public SearchViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ANIMATION_DURATION = context.getResources().getInteger(R.integer.animation_duration);
    }

    @Override
    protected void onFinishInflate() {
        mCollapsed = (ViewGroup) findViewById(R.id.search_box_collapsed);
        mSearchIcon = findViewById(R.id.search_magnifying_glass);
        mCollapsedSearchBox = findViewById(R.id.search_box_start_search);
        mCollapsedHintView = (TextView) findViewById(R.id.search_box_collapsed_hint);

        mExpanded = (ViewGroup) findViewById(R.id.search_expanded_root);
        mSearchEditText = (EditText) mExpanded.findViewById(R.id.search_expanded_edit_text);
        mBackButtonView = mExpanded.findViewById(R.id.search_expanded_back_button);
        mExpandedSearchIcon = findViewById(R.id.search_expanded_magnifying_glass);
        searchList = (ListView) findViewById(R.id.searchResults);
        ArrayList<String> strings = new ArrayList<>();
        String[] strings1 = new String[6];
        strings1[0] = "Bengaluru";
        strings1[1] = "Mumbai";
        strings1[2] = "Mangalore";
        strings1[3] = "Chennai";
        strings1[4] = "Nagpur";
        strings1[5] = "Pune";

        for (int i = 0; i < 5; i++) {
            int rnd = new Random().nextInt(strings1.length);
            strings.add(strings1[rnd]);
        }


        final ListViewAdapter adapter = new ListViewAdapter(getContext(), strings,new PlaceSearch(getContext()).getmGoogleApiClient(),
                                            new PlaceSearch(getContext()).getBoundsWorld(),null);
        searchList.setAdapter(adapter);
        searchList.setTextFilterEnabled(true);
        searchList.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        // Convert a long click into a click to expand the search box, and then long click on the
        // search view. This accelerates the long-press scenario for copy/paste.
        mCollapsedSearchBox.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mCollapsedSearchBox.performClick();
                mSearchEditText.performLongClick();
                return false;
            }
        });

        mCollapsed.setOnClickListener(mSearchViewOnClickListener);
        mSearchIcon.setOnClickListener(mSearchViewOnClickListener);
        mCollapsedSearchBox.setOnClickListener(mSearchViewOnClickListener);

        mSearchEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Utils.showInputMethod(v);
                } else {
                    Utils.hideInputMethod(v);
                }
            }
        });
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    callSearchListener();
                    Utils.hideInputMethod(v);
                    return true;
                }
                return false;
            }
        });
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mSearchEditText.getText().length() > 0) {

                    if (mExpandedSearchIcon.getVisibility() != View.VISIBLE) {
                        Utils.fadeIn(mExpandedSearchIcon, ANIMATION_DURATION);
                    }
                    adapter.getFilter().filter(s);
                } else {
                    Utils.fadeOut(mExpandedSearchIcon, ANIMATION_DURATION);
                }
                if(mSearchBoxListener!=null) mSearchBoxListener.onTextChanged(s, start, before, count);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(mSearchBoxListener!=null) mSearchBoxListener.beforeTextChanged(s, start, count, after);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mSearchBoxListener!=null) mSearchBoxListener.afterTextChanged(s);
            }
        });

        mBackButtonView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                collapse();
            }
        });

        mExpandedSearchIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callSearchListener();
                Utils.hideInputMethod(v);
            }
        });

        mCollapsedDrawable = new ColorDrawable(ContextCompat.getColor(getContext(), android.R.color.transparent));
        mExpandedDrawable = new ColorDrawable(ContextCompat.getColor(getContext(), R.color.default_color_expanded));
        mBackgroundTransition = new TransitionDrawable(new Drawable[]{mCollapsedDrawable, mExpandedDrawable});
        mBackgroundTransition.setCrossFadeEnabled(true);
        setBackgroundCompat();
        Utils.setPaddingAll(SearchViewLayout.this, 8);

        super.onFinishInflate();
    }

    /***
     * Should toolbar be animated, y position.
     * @param toolbar current toolbar which needs to be animated.
     */

    public void handleToolbarAnimation(Toolbar toolbar) {
        this.mToolbar = toolbar;
    }

    /***
     * Set the fragment which would be shown in the expanded state
     * @param activity to get fragment manager
     * @param contentFragment fragment which needs to be shown.
     * @throws RuntimeException if support version of content fragment already set
     */

    public void setExpandedContentFragment(Activity activity, android.app.Fragment contentFragment) {
        mExpandedContentFragment = contentFragment;
        mFragmentManager = activity.getFragmentManager();
        mExpandedHeight = Utils.getSizeOfScreen(activity).y;
    }

    /***
     * Set the support version fragment which would be shown in the expanded state
     * @param activity to get support version of fragment manager, activity must be a FragmentActivity
     * @param contentSupportFragment support version of fragment which needs to be shown.
     * @throws RuntimeException if content fragment already set
     */

    public void setExpandedContentSupportFragment(FragmentActivity activity, android.support.v4.app.Fragment contentSupportFragment) {
        mExpandedContentSupportFragment = contentSupportFragment;
        mSupportFragmentManager = activity.getSupportFragmentManager();
        mExpandedHeight = Utils.getSizeOfScreen(activity).y;
    }

    /***
     * Set the background colours of the searchview.
     * @param collapsedDrawable drawable for collapsed state, default transparent
     * @param expandedDrawable drawable for expanded state, default color.default_color_expanded
     */
    public void setTransitionDrawables(Drawable collapsedDrawable, Drawable expandedDrawable) {
        this.mCollapsedDrawable = collapsedDrawable;
        this.mExpandedDrawable = expandedDrawable;

        mBackgroundTransition = new TransitionDrawable(new Drawable[]{mCollapsedDrawable, mExpandedDrawable});
        mBackgroundTransition.setCrossFadeEnabled(true);
        setBackgroundCompat();
        Utils.setPaddingAll(SearchViewLayout.this, 8);
    }

    @SuppressWarnings("deprecation")
    private void setBackgroundCompat() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(mBackgroundTransition);
        } else {
            setBackgroundDrawable(mBackgroundTransition);
        }
    }

    /***
     * Set hint in the collapsed state
     *
     * Also see {@link #setHint(String)}
     * @param searchViewHint
     */
    public void setCollapsedHint(String searchViewHint) {
        if (searchViewHint != null) {
            mCollapsedHintView.setHint(searchViewHint);
        }
    }

    /***
     * Set hint in the expanded state
     *
     * Also see {@link #setHint(String)}
     * @param searchViewHint
     */
    public void setExpandedHint(String searchViewHint) {
        if (searchViewHint != null) {
            mSearchEditText.setHint(searchViewHint);
        }
    }

    /***
     * Overrides both, {@link #setCollapsedHint(String)} and {@link #setExpandedHint(String)},
     * and sets hint for both the views.
     *
     * Use this if you don't want to show different hints in both the states
     * @param searchViewHint
     */
    public void setHint(String searchViewHint) {
        if (searchViewHint != null) {
            mCollapsedHintView.setHint(searchViewHint);
            mSearchEditText.setHint(searchViewHint);
        }
    }
    
    /***
     * Set a text for the expanded editText
     *
     * Maybe what you input is not a full keyword, and you can use this to stuff the editText
     * usually by clicking the items of list showing inexact results.
     * @param searchViewText
     */
    public void setExpandedText(String searchViewText) {
        if (searchViewText != null) {
            mSearchEditText.setText(searchViewText);
        }
    }
    
    public void expand(boolean requestFocus) {
        mCollapsedHeight = getHeight();
        toggleToolbar(true);
        if (mBackgroundTransition != null)
            mBackgroundTransition.startTransition(ANIMATION_DURATION);
        mIsExpanded = true;

        animateStates(true, 1f, 0f);
        Utils.crossFadeViews(mExpanded, mCollapsed, ANIMATION_DURATION);

        if (requestFocus) {
            mSearchEditText.requestFocus();
        }
    }

    public void collapse() {
        toggleToolbar(false);
        if (mBackgroundTransition != null)
            mBackgroundTransition.reverseTransition(ANIMATION_DURATION);
        mSearchEditText.setText(null);
        mIsExpanded = false;

        animateStates(false, 0f, 1f);
        Utils.crossFadeViews(mCollapsed, mExpanded, ANIMATION_DURATION);

        hideContentFragment();
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

    /**
     * Allow user to set a search icon in the collapsed view
     *
     * @param iconResource resource id of icon
     */
    public void setCollapsedIcon(@DrawableRes int iconResource) {
        ((ImageView)mSearchIcon).setImageResource(iconResource);

    }

    /**
     * Allow user to set a back icon in the expanded view
     *
     * @param iconResource resource id of icon
     */
    public void setExpandedBackIcon(@DrawableRes int iconResource) {
        ((ImageView)mBackButtonView).setImageResource(iconResource);
    }

    /**
     * Allow user to set a search icon in the expanded view
     *
     * @param iconResource resource id of icon
     */
    public void setExpandedSearchIcon(@DrawableRes int iconResource) {
        ((ImageView)mExpandedSearchIcon).setImageResource(iconResource);
    }

    private void showContentFragment() {
        if (mFragmentManager != null) {
            final android.app.FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.animator.fade_in_object_animator, R.animator.fade_out_object_animator);
            //transaction.replace(R.id.search_expanded_content, mExpandedContentFragment);
            transaction.commit();
        } else if (mSupportFragmentManager != null) {
            final android.support.v4.app.FragmentTransaction transaction = mSupportFragmentManager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fade_in_anim_set, R.anim.fade_out_anim_set);
            //transaction.replace(R.id.search_expanded_content, mExpandedContentSupportFragment);
            transaction.commit();
        }
    }

    private void hideContentFragment() {
        if (mFragmentManager != null) {
            final android.app.FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.remove(mExpandedContentFragment).commit();
        } else if (mSupportFragmentManager != null) {
            final android.support.v4.app.FragmentTransaction transaction = mSupportFragmentManager.beginTransaction();
            transaction.remove(mExpandedContentSupportFragment).commit();
        } else {
            Log.e(LOG_TAG, "Fragment Manager is null. Returning");
        }
    }

    private void toggleToolbar(boolean expanding) {
        if (mToolbar == null) return;

        mToolbar.clearAnimation();
        if (expanding) {
            toolbarExpandedHeight = mToolbar.getHeight();
        }

        int toYValue = expanding ? toolbarExpandedHeight * (-1) : 0;

        mToolbar.animate()
                .y(toYValue)
                .setDuration(ANIMATION_DURATION)
                .start();

        Utils.animateHeight(
                mToolbar,
                expanding ? toolbarExpandedHeight : 0,
                expanding ? 0 : toolbarExpandedHeight,
                ANIMATION_DURATION);
    }

    private void animateStates(final boolean expand, final float start, final float end) {
        mAnimator = ValueAnimator.ofFloat(start, end);
        mAnimator.cancel();

        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (expand) {
                    Utils.setPaddingAll(SearchViewLayout.this, 0);
                    showContentFragment();

                    ViewGroup.LayoutParams params = getLayoutParams();
                    params.height = mExpandedHeight;
                    setLayoutParams(params);
                } else {
                    Utils.setPaddingAll(SearchViewLayout.this, 8);
                }
                if (mOnToggleAnimationListener != null)
                    mOnToggleAnimationListener.onFinish(expand);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (!expand) {
                    ViewGroup.LayoutParams params = getLayoutParams();
                    params.height = mCollapsedHeight;
                    setLayoutParams(params);
                }
                if (mOnToggleAnimationListener != null)
                    mOnToggleAnimationListener.onStart(expand);
            }
        });

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int padding = (int) (8 * animation.getAnimatedFraction());
                if (expand) padding = 8 - padding;
                Utils.setPaddingAll(SearchViewLayout.this, padding);
            }
        });

        mAnimator.setDuration(ANIMATION_DURATION);
        mAnimator.start();
    }

    private void callSearchListener() {
        Editable editable = mSearchEditText.getText();
        if (editable != null && editable.length() > 0) {
            if (mSearchListener != null) {
                mSearchListener.onFinished(editable.toString());
            }
        }
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (mSearchEditTextLayoutListener != null) {
            if (mSearchEditTextLayoutListener.onKey(this, event.getKeyCode(), event)) {
                return true;
            }
        }
        return super.dispatchKeyEventPreIme(event);
    }

    /**
     * Open the search UI when the user clicks on the search box.
     */
    private final OnClickListener mSearchViewOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mIsExpanded) {
                expand(true);
            }
        }
    };

    /**
     * If the search term is empty and the user closes the soft keyboard, close the search UI.
     */
    private final OnKeyListener mSearchEditTextLayoutListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN &&
                    isExpanded()) {
                boolean keyboardHidden = Utils.hideInputMethod(v);
                if (keyboardHidden) return true;
                collapse();
                return true;
            }
            return false;
        }
    };


    public class ListViewAdapter extends ArrayAdapter<String> implements Filterable {

        private final Context context;
        private List<String> values;
        /**
         * Handles autocomplete requests.
         */
        private GoogleApiClient mGoogleApiClient;

        /**
         * The bounds used for Places Geo Data autocomplete API requests.
         */
        private LatLngBounds mBounds;

        /**
         * The autocomplete filter used to restrict queries to a specific set of place types.
         */
        private AutocompleteFilter mPlaceFilter;

        public ListViewAdapter(Context context, List<String> objects,GoogleApiClient googleApiClient,
                               LatLngBounds bounds, AutocompleteFilter filter) {
            super(context, R.layout.search_static_list_item, objects);
            this.context = context;
            this.values = objects;
            this.mGoogleApiClient = googleApiClient;
            this.mBounds = bounds;
            this.mPlaceFilter = filter;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.search_static_list_item, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.card_title);
            textView.setText(values.get(position));
            return rowView;
        }


            @Override
            public String getItem(int index) {
                return values.get(index);
            }


            public ArrayList<String> getNewSuggestions(CharSequence constraint){
                /*ArrayList<String> results = new ArrayList<>();
                for(int i = 0;i<5;i++){
                    results.add(query+"ANYLd"+"34543");
                }
                return results;*/
                ArrayList<String> res = new ArrayList<>();
                if (mGoogleApiClient.isConnected()) {
                    Log.i(LOG_TAG, "Starting autocomplete query for: " + constraint);

                    // Submit the query to the autocomplete API and retrieve a PendingResult that will
                    // contain the results when the query completes.
                    PendingResult<AutocompletePredictionBuffer> results =
                            Places.GeoDataApi
                                    .getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
                                            mBounds, mPlaceFilter);

                    // This method should have been called off the main UI thread. Block and wait for at most 60s
                    // for a result from the API.
                    AutocompletePredictionBuffer autocompletePredictions = results
                            .await(60, TimeUnit.SECONDS);

                    // Confirm that the query completed successfully, otherwise return null
                    final Status status = autocompletePredictions.getStatus();
                    if (!status.isSuccess()) {
                        Toast.makeText(getContext(), "Error contacting API: " + status.toString(),
                                Toast.LENGTH_SHORT).show();
                        Log.e(LOG_TAG, "Error getting autocomplete prediction API call: " + status.toString());
                        autocompletePredictions.release();
                        return null;
                    }

                    Log.i(LOG_TAG, "Query completed. Received " + autocompletePredictions.getCount()
                            + " predictions.");


                    ArrayList<AutocompletePrediction> predictions = DataBufferUtils.freezeAndClose(autocompletePredictions);
                    int size = predictions.size();
                    for(int i = 0;i<size;i++){
                        AutocompletePrediction item = predictions.get(i);
                        //res.add(item.getPrimaryText(STYLE_BOLD)+":"+item.getSecondaryText(STYLE_BOLD));
                        res.add(item.getFullText(STYLE_BOLD)+"");
                    }

                    // Freeze the results immutable representation that can be stored safely.
                    return res;
                }
                Log.e(LOG_TAG, "Google API client is not connected for autocomplete query.");
                return null;
            }


            @Override
            public Filter getFilter() {
                Filter filter = new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults filterResults = new FilterResults();
                        if (constraint != null) {
                            // Retrieve the autocomplete results.
                            if(new PlaceSearch(context).connectClient())
                            values = getNewSuggestions(constraint);

                            // Assign the data to the FilterResults
                            filterResults.values = values;
                            filterResults.count = values.size();
                        }
                        return filterResults;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        if (results != null && results.count > 0) {
                            //setImageVisibility();
                            notifyDataSetChanged();
                        } else {
                            notifyDataSetInvalidated();
                        }
                    }
                };
                return filter;
            }
    }

}
