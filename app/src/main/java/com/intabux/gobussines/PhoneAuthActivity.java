package com.intabux.gobussines;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.intabux.gobussines.Models.Business;
import com.intabux.gobussines.activities.BusinessRegister;
import com.intabux.gobussines.providers.AuthProvider;
import com.intabux.gobussines.providers.BusinessProvider;

public class PhoneAuthActivity extends AppCompatActivity {

    Button mButtonCodeVerification;
    EditText mEditTextCodeVerification;

    String mExtraPhone;
    String mVerificationId;

    AuthProvider mAuthProvider;
    BusinessProvider mBusinessProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        mAuthProvider = new AuthProvider();
        mBusinessProvider = new BusinessProvider();

        mButtonCodeVerification = findViewById(R.id.btnCodeVerification);
        mEditTextCodeVerification = findViewById(R.id.editTextCodeVerification);

        mExtraPhone = getIntent().getStringExtra("phone");
        mAuthProvider.sendCodeVerification(mExtraPhone,mCallBacks);
        mButtonCodeVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = mEditTextCodeVerification.getText().toString();
                if(!code.equals("") && code.length() >=6){
                    signIn(code);
                }else{
                    Toast.makeText(PhoneAuthActivity.this,"Ingresa el código por favor", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            //cuándo se ha verificado correctamente

            String code = phoneAuthCredential.getSmsCode();

            if(code!=null){
                mEditTextCodeVerification.setText(code);
                signIn(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            //cuándo falla el envío del sms
            Toast.makeText(PhoneAuthActivity.this,"Se produjo un error al enviar el SMS, intenta otra vez"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            //Cuando el código ha sido enviado
            super.onCodeSent(verificationId, forceResendingToken);
            Toast.makeText(PhoneAuthActivity.this,"El código de verificación ha sido enviado", Toast.LENGTH_LONG).show();
            mVerificationId = verificationId;
        }
    };

    private void signIn(String code) {
        mAuthProvider.signInPhone(mVerificationId,code).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //usuario ha iniciado sesión
                    mBusinessProvider.getBusiness(mAuthProvider.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Intent intent = new Intent(PhoneAuthActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }else {
                                createInfo();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    Toast.makeText(PhoneAuthActivity.this,"No se ha podido iniciar sesión, intenta otra vez.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createInfo() {
        Business business = new Business();
        business.setId(mAuthProvider.getId());
        business.setPhone(mExtraPhone);
        mBusinessProvider.create(business).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> taskCreate) {
                if(taskCreate.isSuccessful()){
                    Intent intent = new Intent(PhoneAuthActivity.this, BusinessRegister.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(PhoneAuthActivity.this,"No se ha podido crear la información.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
