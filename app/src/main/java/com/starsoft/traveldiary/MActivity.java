package com.starsoft.traveldiary;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;


import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.starsoft.traveldiary.ahbottomnavigation.AHBottomNavigation;
import com.starsoft.traveldiary.ahbottomnavigation.AHBottomNavigationAdapter;
import com.starsoft.traveldiary.ahbottomnavigation.AHBottomNavigationItem;
import com.starsoft.traveldiary.ahbottomnavigation.AHBottomNavigationViewPager;
import com.starsoft.traveldiary.bottomnavigation.BottomNavigation;
import com.starsoft.traveldiary.fragments.fragAdapter.FragAdapter;
import com.starsoft.traveldiary.searchView.PlaceSearch;
import com.starsoft.traveldiary.searchView.SearchStaticListSupportFragment;
import com.starsoft.traveldiary.searchView.SearchViewLayout;
import com.starsoft.traveldiary.ui.fonts.MuseoTextView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aashish on 9/9/2016.
 */
public class MActivity extends AppCompatActivity implements BottomNavigation.OnMenuItemSelectionListener{

    public static final String TAG = MActivity.class.getSimpleName();
    private FragAdapter fragAdapter;

    private BottomNavigation mBottomNavigation;

    private CustomViewPager viewPager;

    private FloatingActionButton fab;

    private boolean FAB_Status = false;

    //Animations
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    private MuseoTextView headerTitle;

    private SearchViewLayout searchViewLayout;

    private ViewGroup.MarginLayoutParams params;

    private static int TopMargin = 315;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchViewLayout = (SearchViewLayout)findViewById(R.id.search_view_container);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // Setup the toolbar
        handleSearch(toolbar);

        /*String address = getIntent().getStringExtra("LOCATION_ADDRESS");
        Log.d(TAG, address);
        headerTitle = (MuseoTextView) findViewById(R.id.locationText);
        if (address != null){
            String[] parts = address.split(",");
            headerTitle.setText(parts[parts.length-2]);
        }*/


        // Fab
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);


        //Fab Listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),PostActivity.class));
            }
        });


        // Bottom Navigation
        mBottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);
        mBottomNavigation.setOnMenuItemClickListener(this);
        mBottomNavigation.setDefaultSelectedIndex(0);


        // Viewpager
        viewPager = (CustomViewPager)findViewById(R.id.contentContainer);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setPagingEnabled(false);
        fragAdapter = new FragAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragAdapter);
        viewPager.setCurrentItem(0,false);
        params = (ViewGroup.MarginLayoutParams)viewPager.getLayoutParams();
        Log.d(TAG,""+getSupportActionBar().getHeight());
        params.topMargin = TopMargin;


    }


    // Function to handle the search
    private void handleSearch(Toolbar toolbar) {
        final SearchViewLayout searchViewLayout = (SearchViewLayout) findViewById(R.id.search_view_container);
        searchViewLayout.handleToolbarAnimation(toolbar);
        searchViewLayout.setCollapsedHint("Search places and more..");
        searchViewLayout.setExpandedHint("Enter place name or groups...");

        searchViewLayout.setExpandedContentSupportFragment(this, new SearchStaticListSupportFragment());

        ColorDrawable collapsed = new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary));
        ColorDrawable expanded = new ColorDrawable(ContextCompat.getColor(this, R.color.default_color_expanded));

        searchViewLayout.setTransitionDrawables(collapsed, expanded);

        searchViewLayout.setSearchListener(new SearchViewLayout.SearchListener() {
            @Override
            public void onFinished(String searchKeyword) {
                searchViewLayout.collapse();

            }
        });


        // Animation Listener
        searchViewLayout.setOnToggleAnimationListener(new SearchViewLayout.OnToggleAnimationListener() {
            @Override
            public void onStart(boolean expanding) {
                if (expanding) {
                    fab.hide();
                    showOrHideBottomNavigation(false);
                } else {
                    fab.show();
                    showOrHideBottomNavigation(true);
                }
            }

            @Override
            public void onFinish(boolean expanded) { }
        });


        searchViewLayout.setSearchBoxListener(new SearchViewLayout.SearchBoxListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: " + s + "," + start + "," + count + "," + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + s + "," + start + "," + before + "," + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged: " + s);
            }
        });
    }






    /**
     * Show or hide the bottom navigation with animation
     */
    public void showOrHideBottomNavigation(boolean show) {
        if (show) {
            mBottomNavigation.setVisibility(View.VISIBLE);
        } else {
            mBottomNavigation.setVisibility(View.GONE);
        }
    }




    /**
     * Function to show Fab
     * Called when bottom navigation is clicked
     */
    private void showFAB() {

        fab.setVisibility(View.VISIBLE);
        fab.setAlpha(0f);
        fab.setScaleX(0f);
        fab.setScaleY(0f);
        fab.animate()
                .alpha(1)
                .scaleX(1)
                .scaleY(1)
                .setDuration(300)
                .setInterpolator(new OvershootInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fab.animate()
                                .setInterpolator(new LinearOutSlowInInterpolator())
                                .start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }


    /**
     * Function to hide Fab
     * Called when bottom navigation is clicked
     */
    private void hideFAB() {
                        fab.animate()
                                .alpha(0)
                                .scaleX(0)
                                .scaleY(0)
                                .setDuration(300)
                                .setInterpolator(new LinearOutSlowInInterpolator())
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        fab.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {
                                        fab.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                })
                                .start();
    }



    // Show Searchbar
    public void showSearchBar(){
      searchViewLayout.setVisibility(View.VISIBLE);
    }

    // Hide Searchbar
    public void hideSearchBar(){
        searchViewLayout.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_search) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(new PlaceSearch(getApplicationContext()).isClientConnected()){
            new PlaceSearch(getApplicationContext()).disconnectClient();
        }
    }


    /**
     *
     * @param itemId
     * @param position
     * Listeners for bottom navigation item click event
     */
    @Override
    public void onMenuItemSelect(@IdRes int itemId, int position) {
        Log.d(TAG,""+position);
        switch (position){
            case 0: viewPager.setCurrentItem(0,false);
                    params.topMargin = TopMargin;
                    showFAB();
                    if (searchViewLayout.getVisibility() == View.GONE)
                        showSearchBar();
                    break;

            case 1: viewPager.setCurrentItem(1,false);
                    params.topMargin = 165;
                    hideFAB();
                    if (searchViewLayout.getVisibility() == View.VISIBLE)
                        hideSearchBar();
                    break;

            case 2: viewPager.setCurrentItem(2,false);
                    params.topMargin = 165;
                    hideFAB();
                    if (searchViewLayout.getVisibility() == View.VISIBLE)
                        hideSearchBar();
                    break;

            case 3: viewPager.setCurrentItem(3,false);
                    params.topMargin = 165;
                    hideFAB();
                    if (searchViewLayout.getVisibility() == View.VISIBLE)
                        hideSearchBar();
                    break;

            default: viewPager.setCurrentItem(0,false);
                     params.topMargin = TopMargin;
                     showFAB();
                     if (searchViewLayout.getVisibility() == View.GONE)
                         showSearchBar();
        }
    }

    @Override
    public void onMenuItemReselect(@IdRes int itemId, int position) {

    }
}
