package com.joseluis.newsapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.joseluis.newsapp.R;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPass;
    private Button btnLogin, btnRegistro;
    private ScrollView formLogin;
    private ProgressBar pbLogin;
    private String email, password;
    boolean tryLogin = false;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.buttonLogin);
        formLogin= findViewById( R.id.FormLogin );
        pbLogin = findViewById(R.id.progressBarLogin);
        btnRegistro = findViewById(R.id.buttonRegister);


        firebaseAuth = FirebaseAuth.getInstance();
        changeLoginFormVisibility( true );
        eventos();

    }

    private void eventos() {
        btnLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email= etEmail.getText().toString();
                password=etPass.getText().toString();

                if(email.isEmpty()){
                    etEmail.setError( "El E-Mail es obligatorio" );
                }
                else{
                    if(password.isEmpty()){
                        etPass.setError( "La contraseña es obligatorio" );
                    }
                    else{
                        if (password.length()<8 || password.length()>16){
                            etPass.setError( "El tamaño es incorrecto" );
                        }
                        else{
                            //TODO: Aqui realizaremos el login con FireBase con Auth
                            changeLoginFormVisibility(false);
                            loginUser();
                        }
                    }
                }

            }
        } );

        btnRegistro.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this, RegisterActivity.class );
                startActivity( i );
                finish();
            }
        } );

    }


    private void loginUser() {
        firebaseAuth.signInWithEmailAndPassword( email,password ).addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                tryLogin=true;
                if (task.isSuccessful()){
                    FirebaseUser user=firebaseAuth.getCurrentUser();
                    updateUI(user);
                }
                else{
                    Log.w("TAG", "signInError: ", task.getException());
                    updateUI(null);
                }

            }
        } );
    }

    private void updateUI(FirebaseUser user) {
        if(user != null) {
            // Navegar hacia la siguiente pantalla de la aplicación
            Intent i = new Intent(LoginActivity.this, NewsActivity.class);
            startActivity(i);

        } else {
            changeLoginFormVisibility(true);
            if(tryLogin) {
                etPass.setError("Email y/o contraseña incorrectos");
                etPass.requestFocus();
            }
        }
    }

    private void changeLoginFormVisibility(boolean showForm) {
        pbLogin.setVisibility(showForm ? View.GONE : View.VISIBLE);
        formLogin.setVisibility(showForm ? View.VISIBLE : View.GONE);
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Comprobamos si previamente el usuario ya ha iniciado sesión en
        // este dispositivo
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

}
