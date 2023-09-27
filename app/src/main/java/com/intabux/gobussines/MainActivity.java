package com.intabux.gobussines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.intabux.gobussines.activities.BusinessRegister;
import com.intabux.gobussines.providers.AuthProvider;


public class MainActivity extends AppCompatActivity {

    Button mButtonGoToLogin;
    EditText mEditTextPhone;
    CountryCodePicker mCountryCode;

    AuthProvider mAuthprovider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuthprovider = new AuthProvider();

        mCountryCode = findViewById(R.id.ccp);
        mEditTextPhone = findViewById(R.id.editTextPhone);
        mButtonGoToLogin = findViewById(R.id.btnGoToLogin);
        mButtonGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });
    }

    private void goToLogin() {
        String code = mCountryCode.getSelectedCountryCodeWithPlus();
        String phone = mEditTextPhone.getText().toString();
        if(!phone.equals("")){
            Intent intent = new Intent(MainActivity.this,PhoneAuthActivity.class);
            intent.putExtra("phone",code+phone);
            startActivity(intent);
        }else{
            Toast.makeText(this,"Verifica el número de teléfono",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuthprovider.existSession()){
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


}
