package com.example.chatt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private EditText user;
    private Button btn_login;

    private FirebaseAuth mAuth = null;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount gsa;
    private static final int RC_SIGN_IN = 100;
    private SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("로그인");
        setContentView(R.layout.activity_login);

        signInButton = (SignInButton)findViewById(R.id.btn_google);
        mAuth = FirebaseAuth.getInstance();

//        user = (EditText)findViewById(R.id.user);
//        btn_login = (Button)findViewById(R.id.btn_login);

        gsa = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
        if(gsa!=null){
            Toast.makeText(LoginActivity.this,gsa.getDisplayName()+"님 로그인 되어있습니다.",Toast.LENGTH_SHORT).show();
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        btn_login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String str_name = user.getText().toString();
//
//                if("".equals(str_name)){
//                    Toast.makeText(LoginActivity.this,"닉네임을 입력해주세요!",Toast.LENGTH_SHORT).show();
//                }else{
//                    Intent intent = new Intent(LoginActivity.this,RoomActivity.class);
//                    intent.putExtra("name",str_name);
//                    startActivity(intent);
//                }
//            }
//        });

        signInButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                signIn();
            }
        });


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount acct = task.getResult(ApiException.class);

                if (acct != null) {
                    firebaseAuthWithGoogle(acct.getIdToken());
                }
            } catch (ApiException e) {
            }

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(),"로그인에 실패했습니다.",Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) { //update ui code here

        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, RoomActivity.class);
            intent.putExtra("name",user.getDisplayName());
            startActivity(intent);
        }
    }
}