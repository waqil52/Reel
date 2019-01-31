package com.example.user.movie_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    //defining view objects
    private ImageView reelgif2;
    private ImageView signup2;
    private EditText emailtext2;
    private EditText passwordtext2;
    private Button signupbutton2;
    private Button loginbutton2;

    private ProgressDialog progressDialog;


    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_activity);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), Profile_Activity.class));
        }

        //initializing views
//        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
//        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
//        textViewSignin = (TextView) findViewById(R.id.textViewSignin);
//
//        buttonSignup = (Button) findViewById(R.id.buttonSignup);

        reelgif2=(ImageView) findViewById(R.id.reelgif2);
        signup2=(ImageView) findViewById(R.id.signupimage2);

        emailtext2=(EditText) findViewById(R.id.email2);
        passwordtext2=(EditText) findViewById(R.id.password2);

        loginbutton2=(Button) findViewById(R.id.login2);
        signupbutton2=(Button) findViewById(R.id.signup);


        //attaching listener to button
        signupbutton2.setOnClickListener(this);
        loginbutton2.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
    }

    private void registerUser(){

        //getting email and password from edit texts
        String email = emailtext2.getText().toString().trim();
        String password  = passwordtext2.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }else{
                            //display some message here
                            Toast.makeText(SignupActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {

        if(view == signupbutton2){
            registerUser();
        }

        if(view == loginbutton2){
            //open login activity when user taps on the already registered textview
            startActivity(new Intent(this, LoginActivity.class));
        }

    }
}