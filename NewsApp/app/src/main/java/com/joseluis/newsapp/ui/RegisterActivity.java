package com.joseluis.newsapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.google.android.gms.tasks.*;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joseluis.newsapp.R;
import com.joseluis.newsapp.model.User;
import java.util.regex.*;

public class RegisterActivity extends AppCompatActivity {
    EditText etName,etLastName ,etEmail, etPass;
    Button btnRegistro,btnAccede;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    String name, lastname, email, password;
    ProgressBar pbRegistro;
    ScrollView formRegistro;
    Boolean valido=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );

        etName = findViewById(R.id.editTextName);
        etLastName=findViewById( R.id.editTextLastName );
        etEmail = findViewById(R.id.editTextEmail);
        etPass = findViewById(R.id.editTextPassword);
        btnRegistro = findViewById(R.id.buttonRegistro);
        pbRegistro = findViewById(R.id.progressBarRegistro);
        btnAccede = findViewById( R.id.buttonAccede );
        formRegistro = findViewById(R.id.formRegistro);

        firebaseAuth = FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();
        changeRegisterFormVisibility( true );

        eventos();
    }

    private void eventos() {
        btnRegistro.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=etName.getText().toString();
                lastname=etLastName.getText().toString();
                email=etEmail.getText().toString();
                password=etPass.getText().toString();

                if(name.isEmpty()){
                    etName.setError( "El nombre es obligatorio" );
                }
                else{
                    if (lastname.isEmpty()){
                        etLastName.setError( "El apellido es obligatorio" );
                    }else{

                        if(email.isEmpty()){
                            etEmail.setError( "El e-mail es obligatorio" );
                        }
                        else{
                            if (!validaCorreo()){
                                etEmail.setError( "El correo no es valido" );
                            }
                            else{
                                if(password.isEmpty()){
                                    etPass.setError( "La contraseña es obligatoria" );
                                }
                                else{
                                    if (password.length()<8 || password.length()>16){
                                        etPass.setError( "El tamaño es incorrecto" );
                                    }
                                    else{
                                        //TODO: Aqui realizaremos el registro con FireBase con Auth
                                        createUser();
                                    }
                                }
                            }
                        }

                    }
                }
            }
        } );

        btnAccede.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accede=new Intent( RegisterActivity.this,LoginActivity.class );
                startActivity( accede );
                finish();
            }
        } );
    }

    private boolean validaCorreo() {

        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");


        Matcher mather = pattern.matcher(email);

        if (mather.find() == true) {

            valido=true;
            return valido;
        } else {
            valido=false;
            return valido;
        }

    }

    private void createUser() {
        changeRegisterFormVisibility( false );
        firebaseAuth.createUserWithEmailAndPassword( email,password ).addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user= firebaseAuth.getCurrentUser();
                    updateUI(user);
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        } );
    }

    private void updateUI(FirebaseUser user) {
        if(user != null){
            //Almacenar la informacion del usuario
            User nuevoUsuario=new User( name,lastname,email,password );
            db.collection( "users" ).document(user.getUid()).set( nuevoUsuario ).addOnSuccessListener( new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //Navegar hacia la siguiente pantalla
                    finish();
                    Intent i = new Intent(RegisterActivity.this, NewsActivity.class);
                    startActivity(i);
                    finish();
                }
            } );
        }
        else{
            changeRegisterFormVisibility( true );
            etPass.setError("Nombre, Email y/o contraseña incorrectos");
            etPass.requestFocus();
        }
    }

    private void changeRegisterFormVisibility(boolean showForm) {
        pbRegistro.setVisibility(showForm ? View.GONE : View.VISIBLE);
        formRegistro.setVisibility(showForm ? View.VISIBLE : View.GONE);
    }
}
