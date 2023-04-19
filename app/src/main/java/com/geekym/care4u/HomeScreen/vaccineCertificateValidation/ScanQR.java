package com.geekym.care4u.HomeScreen.vaccineCertificateValidation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.geekym.care4u.HomeScreen.Homescreen;
import com.geekym.care4u.R;
import com.geekym.care4u.HomeScreen.Result_Safe;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class ScanQR extends AppCompatActivity {

    CodeScanner codeScanner;
    CodeScannerView scannView;
    Button Gen;
    SharedPreferences sl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_scan_qr);
        scannView = findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this,scannView);
        Gen = findViewById(R.id.gal);
        sl = getSharedPreferences("scanned", Context.MODE_PRIVATE);

        Gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScanQR.this, GenQR.class);
                startActivity(i);
                finish();
            }
        });

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(ScanQR.this, Result_Safe.class);
                        final String decrypt = result.getText();
                        SharedPreferences.Editor editor = sl.edit();
                        editor.putString("res", decrypt);
                        editor.commit();
                        startActivity(i);
                        finish();

                       /* try{
                            if(decrypt.equals("None")){
                                Intent i = new Intent(ScanQR.this, ResultAct.class);
                                startActivity(i);
                                finish();
                            }else  if(decrypt.equals("Half")) {
                                Intent i = new Intent(ScanQR.this, Partially.class);
                                startActivity(i);
                                finish();
                            }else if(decrypt.equals("Full")){
                                Intent i = new Intent(ScanQR.this, Fully.class);
                                startActivity(i);
                                finish();
                            }else{
                                Toast.makeText(ScanQR.this, "The QR is Not generated by CaringYou App", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(ScanQR.this, "The QR is Not generated by CaringYou App", Toast.LENGTH_SHORT).show();
                        }*/
                    }
                });
            }
        });

        scannView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestForCamera();
    }

    public void requestForCamera() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(ScanQR.this, "Camera Permission is Required.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();

            }
        }).check();
    }

    public void onBackPressed(){
        Intent y = new Intent(ScanQR.this, Homescreen.class);
        startActivity(y);
        finishAffinity();
    }
}