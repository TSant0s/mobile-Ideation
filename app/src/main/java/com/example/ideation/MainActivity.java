package com.example.ideation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ideation.Retrofit.INodeJS;
import com.example.ideation.Retrofit.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    private TextInputEditText emailLoginInput;
    private TextInputEditText passwordLoginInput;
    private ImageButton btnLogin;
    private Button btnOpenRegister;
    public static String loggedUserEmail;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Initialize API
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        //Views
        btnOpenRegister = findViewById(R.id.button_open_register);
        btnLogin = findViewById(R.id.button_login);
        emailLoginInput =  findViewById(R.id.email_login_input);
        passwordLoginInput = findViewById(R.id.password_login_input);



        //Events
        btnOpenRegister.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                openRegisterPage();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                loginUser(emailLoginInput.getText().toString(),passwordLoginInput.getText().toString());


            }
        });

    }

    public static String getLoggedUserEmail(){
        return loggedUserEmail;
    }

    public void setLoggedUserEmail(String email) {
        loggedUserEmail = email;}

    private void loginUser(String email, String password) {
        compositeDisposable.add(myAPI.loginUser(email,password)
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if(s.contains("salt")) {
                            Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                            openMainPage();
                            setLoggedUserEmail(email);


                        } else
                            Toast.makeText(MainActivity.this, ""+s, Toast.LENGTH_SHORT).show();

                    }
                })
        );
    }


    public void openMainPage(){
        Intent intent = new Intent(this,MainPage.class);
        startActivity(intent);

    }
    public void openRegisterPage(){
        Intent intent = new Intent(this,register_page.class);
        startActivity(intent);
    }




}
