package com.starsoft.traveldiary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.starsoft.traveldiary.ui.fonts.CusEditText;
import com.starsoft.traveldiary.ui.fonts.FBtn;
import com.starsoft.traveldiary.ui.fonts.QsRTextView;

public class LoginActivity extends AppCompatActivity {

    private CardView loginCard,signupCard;
    private QsRTextView signAction;
    private int action = 0;
    private FirebaseAuth firebaseAuth;
    private CusEditText uname,upwd,suname,supwd;
    private FBtn loginBtn,signupBtn;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getApplicationContext().getSharedPreferences("pref_user",MODE_PRIVATE);

        editor = sharedPreferences.edit();
        uname = (CusEditText)findViewById(R.id.userEmail);
        upwd = (CusEditText)findViewById(R.id.userPassword);
        suname = (CusEditText)findViewById(R.id.userEmailSU);
        supwd = (CusEditText)findViewById(R.id.userPasswordSU);

        loginCard = (CardView)findViewById(R.id.loginContainer);
        signupCard = (CardView)findViewById(R.id.signupContainer);

        loginBtn = (FBtn)findViewById(R.id.signBtn);
        signupBtn = (FBtn)findViewById(R.id.signupBtn);


        signAction = (QsRTextView)findViewById(R.id.signupText);
        signAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginCard.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out_anim_set));
                loginCard.setVisibility(View.GONE);

                signupCard.setVisibility(View.VISIBLE);
                signupCard.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_from_left));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = uname.getText().toString().trim();
                String pwd = upwd.getText().toString().trim();


                if(TextUtils.isEmpty(name)){
                    Toast.makeText(getApplicationContext(),"Email can't be empty",Toast.LENGTH_SHORT).show();
                }

                if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(getApplicationContext(),"Password can't be empty",Toast.LENGTH_SHORT).show();
                }

                else{
                    firebaseAuth.signInWithEmailAndPassword(name,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                editor.putBoolean("isLogin",true);
                                editor.commit();
                                startActivity(new Intent(getApplicationContext(),MActivity.class));
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Oops, there was some error, try again !!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = suname.getText().toString().trim();
                String pwd = supwd.getText().toString().trim();


                if(TextUtils.isEmpty(name)){
                    Toast.makeText(getApplicationContext(),"Email can't be empty",Toast.LENGTH_SHORT).show();
                }

                if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(getApplicationContext(),"Password can't be empty",Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.createUserWithEmailAndPassword(name,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                editor.putBoolean("isLogin",true);
                                editor.commit();
                                startActivity(new Intent(getApplicationContext(),MActivity.class));
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Oops, an error occured. Try again !!!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }
}
