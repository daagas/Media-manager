package com.example.daagasmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class Datos extends AppCompatActivity {

    private TextView txtPhone = findViewById(R.id.txtPhone);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);
    }

    public void makeCall(){
        phoneCall(this.txtPhone.toString());
    }

    private void phoneCall(String num){
        String dial = "tel:" + num;
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
    }
}
