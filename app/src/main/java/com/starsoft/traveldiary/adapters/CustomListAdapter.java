package com.starsoft.traveldiary.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.starsoft.traveldiary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aashish on 9/9/2016.
 */
public class CustomListAdapter extends LoadMoreAdapter {
    public static final String STATE_DATA = "state_data";

    private ArrayList<String> mData = new ArrayList<>();
    private Context context;

    public CustomListAdapter(RecyclerView recyclerView, Context con) {
        super(recyclerView);this.context = con;
    }

    @Override
    public void saveState(Bundle outState) {
        super.saveState(outState);
        outState.putStringArrayList(STATE_DATA, mData);
    }

    @Override
    public void restoreState(Bundle restoreState) {
        super.restoreState(restoreState);
        if (restoreState != null) {
            mData = restoreState.getStringArrayList(STATE_DATA);
        }
    }


    public void addData(List<String> data) {
        // Use this here to avoid forgetting about calling this
        setLoading(false);
        int previousSize = mData.size();
        mData.addAll(data);
        notifyItemRangeInserted(previousSize, data.size());
    }

    @Override
    public List getItems() {
        return mData;
    }

    @Override
    public ViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_demo, parent, false));
    }

    @Override
    public void onBindViewHolder(LoadMoreAdapter.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_NORMAL) {
            ((ViewHolder) holder).setData(mData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends LoadMoreAdapter.ViewHolder {


        ImageButton cmtBtn, likeBtn;
        TextSwitcher cmCount, likCount;
        int cCount = 23;
        int lCount = 80;
        int state1,state2 = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            cmtBtn = (ImageButton)itemView.findViewById(R.id.cmtBtn);
            likeBtn = (ImageButton)itemView.findViewById(R.id.likeBtn);

            cmCount = (TextSwitcher)itemView.findViewById(R.id.cmtCountText);
            likCount = (TextSwitcher)itemView.findViewById(R.id.likeCountText);

            cmCount.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    TextView switcherTextView = new TextView(context);
                    switcherTextView.setTextSize(14);
                    switcherTextView.setTextColor(context.getResources().getColor(R.color.colorTxt));
                    switcherTextView.setText(""+cCount);
                    return switcherTextView;
                }
            });

            likCount.setFactory(new ViewSwitcher.ViewFactory() {
                @Override
                public View makeView() {
                    TextView switcherTextView = new TextView(context);
                    switcherTextView.setTextSize(14);
                    switcherTextView.setTextColor(context.getResources().getColor(R.color.colorTxt));
                    switcherTextView.setText(""+lCount);
                    //switcherTextView.setShadowLayer(6, 6, 6, Color.BLACK);
                    return switcherTextView;
                }
            });

            Animation animationOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
            Animation animationIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);

            cmCount.setOutAnimation(animationOut);
            cmCount.setInAnimation(animationIn);

            likCount.setOutAnimation(animationOut);
            likCount.setInAnimation(animationIn);



            //cmCount.setText(""+cCount);
            //likCount.setText(""+lCount);

            cmtBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(state1 == 0){
                        cmtBtn.setImageResource(R.drawable.ic_chat_bubble_teal_a700_18dp);
                        cCount+=1;
                        cmCount.setText(""+cCount);
                        state1 = 1;
                    }else{
                        cmtBtn.setImageResource(R.drawable.ic_chat_bubble_grey_500_18dp);
                        cCount-=1;
                        cmCount.setText(""+cCount);
                        state1 = 0;
                    }
                }
            });


            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(state2 == 0){
                        likeBtn.setImageResource(R.drawable.ic_thumb_up_light_blue_a700_18dp);
                        lCount+=1;
                        likCount.setText(""+lCount);
                        state2 = 1;
                    }else{
                        likeBtn.setImageResource(R.drawable.ic_thumb_up_grey_500_18dp);
                        lCount-=1;
                        likCount.setText(""+lCount);
                        state2 = 0;
                    }
                }
            });


        }

        public void setData(String data) {

        }
    }
}
