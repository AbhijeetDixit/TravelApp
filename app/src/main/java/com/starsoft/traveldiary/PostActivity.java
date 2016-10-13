package com.starsoft.traveldiary;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.starsoft.traveldiary.models.Post;
import com.starsoft.traveldiary.ui.fonts.CusEditText;

import java.util.Date;

public class PostActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ValueEventListener postListener;

    private CusEditText title,description,tag;
    private LinearLayout postBtn;
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        // Iinitialize the Firebase Database instance
        reference = FirebaseDatabase.getInstance().getReference("postData");

        // Get the user id from the firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        title = (CusEditText)findViewById(R.id.postTitle);
        description = (CusEditText)findViewById(R.id.postDescription);
        tag = (CusEditText)findViewById(R.id.postTag);

        postBtn = (LinearLayout)findViewById(R.id.postBtn);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tit = title.getText().toString().trim();
                String des = description.getText().toString().trim();
                String tg = tag.getText().toString().trim();
//                Date date = new Date();
//                date.getTime();
                if(postId == null)
                    postId = "post"+0;

                Post post = new Post(postId,tit,des,tg,firebaseUser.getUid());
                reference.child(postId).setValue(post);
                Toast.makeText(getApplicationContext(),"Post Successfull",Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        ValueEventListener pListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                   int postCount = (int)snapshot.getChildrenCount();
                   if(postCount!=0)
                       postId = "post"+postCount;
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        reference.addValueEventListener(pListener);
        postListener = pListener;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(postListener!=null){
            reference.removeEventListener(postListener);
        }
    }
}
