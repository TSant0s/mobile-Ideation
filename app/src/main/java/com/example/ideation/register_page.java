package com.example.ideation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ideation.Retrofit.INodeJS;
import com.example.ideation.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
public class register_page extends AppCompatActivity {

INodeJS myAPI;
CompositeDisposable compositeDisposable = new CompositeDisposable();




private EditText emailRegisterInput;
private EditText passwordRegisterInput;
private EditText nameRegisterInput;
private Button btnRegister;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);


        emailRegisterInput=findViewById(R.id.email_register_input);
        passwordRegisterInput=findViewById(R.id.password_register_input);
        nameRegisterInput = findViewById(R.id.name_register_input);
        btnRegister = findViewById(R.id.button_register);




        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               registerUser(nameRegisterInput.getText().toString(),emailRegisterInput.getText().toString(),passwordRegisterInput.getText().toString());
            }
        });






    }


    private void registerUser(String name,String email, String password) {
        compositeDisposable.add(myAPI.registerUser(name,email,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(register_page.this, ""+s, Toast.LENGTH_SHORT).show();

                    }
                })
        );
    }





}
