package com.starsoft.traveldiary.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.starsoft.traveldiary.R;
import com.starsoft.traveldiary.Showcaselibrary.CardScaleHelper;
import com.starsoft.traveldiary.Showcaselibrary.SpeedRecyclerView;
import com.starsoft.traveldiary.adapters.CardAdapter;
import com.starsoft.traveldiary.fragments.fragAdapter.FragAdapter;
import com.starsoft.traveldiary.util.BlurBitmapUtils;
import com.starsoft.traveldiary.util.ViewSwitchUtils;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aashish on 9/9/2016.
 */
public class FragTwo extends Fragment {

    private SpeedRecyclerView mRView;
    private ImageView mBlurView;
    private List<Integer> mList = new ArrayList<>();
    private CardScaleHelper mCardScaleHelper = null;
    private Runnable mBlurRunnable;
    private int mLastPos = -1;
    private static final String TAG = FragTwo.class.getSimpleName();

public static FragTwo getNewInstance(String title){
    FragTwo fragTwo = new FragTwo();
    Bundle args = new Bundle();
    args.putString("title", title);
    fragTwo.setArguments(args);
    Log.d(TAG, "fragTwo new instance");
    return fragTwo;
  }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_two, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        for (int i = 0; i < 10; i++) {
            mList.add(R.drawable.pic4);
            mList.add(R.drawable.pic5);
            mList.add(R.drawable.pic6);
        }

        mRView = (SpeedRecyclerView) view.findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRView.setLayoutManager(linearLayoutManager);
        mRView.setAdapter(new CardAdapter(mList));
        // mRecyclerView绑定scale效果
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(2);
        mCardScaleHelper.attachToRecyclerView(mRView);

        initBlurBackground(view);
    }

    private void initBlurBackground(View view) {
        mBlurView = (ImageView) view.findViewById(R.id.blurView);
        mRView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    notifyBackgroundChange();
                }
            }
        });

        notifyBackgroundChange();
    }

    private void notifyBackgroundChange() {
        if (mLastPos == mCardScaleHelper.getCurrentItemPos()) return;
        mLastPos = mCardScaleHelper.getCurrentItemPos();
        final int resId = mList.get(mCardScaleHelper.getCurrentItemPos());
        mBlurView.removeCallbacks(mBlurRunnable);
        mBlurRunnable = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
                ViewSwitchUtils.startSwitchBackgroundAnim(mBlurView, BlurBitmapUtils.getBlurBitmap(mBlurView.getContext(), bitmap, 15));
            }
        };
        mBlurView.postDelayed(mBlurRunnable, 500);
    }
}
