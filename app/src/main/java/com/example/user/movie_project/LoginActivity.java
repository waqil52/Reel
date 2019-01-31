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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    //defining views
    private ImageView reelgif;
    private ImageView login;
    private EditText emailtext;
    private EditText passwordtext;
    private Button loginbutton;
    private Button signupbutton;

    public static String user;



    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the objects getcurrentuser method is not null
        //means user is already logged in
        if(firebaseAuth.getCurrentUser() != null){
            //close this activity
            finish();
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), Profile_Activity.class));
        }


        reelgif=(ImageView) findViewById(R.id.reelgif);
        login=(ImageView) findViewById(R.id.loginimage);

        emailtext=(EditText) findViewById(R.id.email);
        passwordtext=(EditText) findViewById(R.id.password);

        loginbutton=(Button) findViewById(R.id.login);
        signupbutton=(Button) findViewById(R.id.signup);


        //initializing views
//        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
//        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
//        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
//        textViewSignup  = (TextView) findViewById(R.id.textViewSignUp);

        progressDialog = new ProgressDialog(this);

        //attaching click listener
        loginbutton.setOnClickListener(this);
        signupbutton.setOnClickListener(this);
    }

    //method for user login
    private void userLogin(){
        final String email = emailtext.getText().toString().trim();
        String password  = passwordtext.getText().toString().trim();


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

        progressDialog.setMessage("Logging in Please Wait...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        //if the task is successfull
                        if(task.isSuccessful()){
                            //start the profile activity
                            user = email;
                            finish();
                            startActivity(new Intent(getApplicationContext(), Profile_Activity.class));
                        }
                        else{
                            Toast.makeText(LoginActivity.this,"Wrong Email/Password",Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(view == loginbutton){
            userLogin();
        }

        if(view == signupbutton){
            finish();
            startActivity(new Intent(this, SignupActivity.class));
        }
    }
}