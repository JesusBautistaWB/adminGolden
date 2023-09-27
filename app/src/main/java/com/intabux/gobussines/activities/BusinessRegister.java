package com.intabux.gobussines.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import com.intabux.gobussines.HomeActivity;
import com.intabux.gobussines.Models.Business;

import com.intabux.gobussines.R;

import com.intabux.gobussines.providers.AuthProvider;
import com.intabux.gobussines.providers.BusinessProvider;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class BusinessRegister extends AppCompatActivity {

    Button mBtnRegister;
    TextInputEditText inputName;
    TextInputEditText inputEmail;
    TextInputEditText inputTelefono;
    TextInputEditText inputDescripcion;
    TextInputEditText inputGiro;

    AlertDialog mDialog;

    AuthProvider mProvider;
    BusinessProvider mBusinessProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_register);

        mBtnRegister = findViewById(R.id.btnRegister);
        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputTelefono = findViewById(R.id.inputPhone);
        inputDescripcion = findViewById(R.id.inputDescription);
        inputGiro = findViewById(R.id.inputGiro);


        mProvider = new AuthProvider();
        mBusinessProvider = new BusinessProvider();

        mDialog = new SpotsDialog.Builder().setContext(BusinessRegister.this).setMessage("Verificando datos").build();

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRegisterBusiness();
            }
        });
    }

    void clickRegisterBusiness(){
        final String name = inputName.getText().toString();
        final String email = inputEmail.getText().toString();
        final String description = inputDescripcion.getText().toString();
        final String type = inputGiro.getText().toString();

        if(!name.isEmpty() && !email.isEmpty() && !description.isEmpty() && !type.isEmpty()){
            mDialog.show();
            Business business = new Business();
            business.setId(mProvider.getId());
            business.setName(name);
            business.setEmail(email);
            business.setDescription(description);
            business.setType(type);
            update(business);
        }
        else{
            Toast.makeText(this, "Verificar datos", Toast.LENGTH_SHORT).show();
        }
    }

    void update(Business business){
        mBusinessProvider.update(business).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(BusinessRegister.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BusinessRegister.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(BusinessRegister.this, "Usuario no registrado, intente otra vez", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
