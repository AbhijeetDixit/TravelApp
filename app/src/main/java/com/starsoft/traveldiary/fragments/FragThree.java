package com.starsoft.traveldiary.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.starsoft.traveldiary.R;

/**
 * Created by Aashish on 9/9/2016.
 */
public class FragThree extends Fragment {

    public static FragThree getNewInstance(String title){
        FragThree fragThree = new FragThree();
        Bundle args = new Bundle();
        args.putString("title",title);
        fragThree.setArguments(args);
        return fragThree;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_three, container, false);
        TextView tv = (TextView)view.findViewById(R.id.textView2);
        tv.setText("Page: "+getArguments().getString("title"));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
