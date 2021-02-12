package com.txwy.and.snqx;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import lib.xdsdk.passport.CometPassport;
import lib.xdsdk.passport.GoogleHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button GuestLogin = findViewById(R.id.Button_Guest);
        Button GoogleLogin = findViewById(R.id.Button_Google);
        Button XDGLogin = findViewById(R.id.Button_Xdg);
        Button CopyQuery = findViewById(R.id.Button_CopyQuery);
        Button Logout = findViewById(R.id.Button_Logout);
        TextView Result = findViewById(R.id.TextView_queryResult);
        CometPassport passport = CometPassport.model();

        GuestLogin.setOnClickListener((v) -> new Thread(() -> passport.signWithGuest(this)).start());
        passport.setOnGuestLoginCompleteListener((result -> {
            Toast.makeText(this, "게스트 로그인 쿼리 정보 받아오기 성공!", Toast.LENGTH_SHORT).show();
            Result.setText(result.toString());
        }));

        GoogleLogin.setOnClickListener((v) -> new Thread(() -> passport.signWithGoogle(this)).start());
        passport.setOnGoogleLoginCompleteListener((result -> {
            Toast.makeText(this, "구글 로그인 쿼리 정보 받아오기 성공!", Toast.LENGTH_SHORT).show();
            Result.setText(result.toString());
        }));

        XDGLogin.setOnClickListener((v) -> {
            Dialog loginDialog = new Dialog(this);
            loginDialog.setContentView(R.layout.dialog_xdg_login);
            Window window = loginDialog.getWindow();
            TextView Id = window.findViewById(R.id.EditText_Nickname);
            TextView Pw = window.findViewById(R.id.ExitText_Passwrd);
            Button Login = window.findViewById(R.id.Button_Login);

            Login.setOnClickListener((v1) -> {
                String ID = Id.getText().toString();
                String PW = Pw.getText().toString();

                if(ID.equals("") || PW.equals("")) {
                    if(ID.equals("")) Id.setError("Input ID");
                    if(PW.equals("")) Pw.setError("Input PW");
                } else {
                    passport.signWithWegames(this,ID,PW);
                    loginDialog.dismiss();
                }
            });
            loginDialog.show();
        });
        passport.setOnXdgLoginCompleteListener((result -> {
            Toast.makeText(this, "XDG 로그인 쿼리 정보 받아오기 성공!", Toast.LENGTH_SHORT).show();
            Result.setText(result.toString());
        }));

        CopyQuery.setOnClickListener((v) -> {
            ClipboardManager clipboard = (ClipboardManager) MainActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Result Query", Result.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Result query copied!", Toast.LENGTH_SHORT).show();
        });

        Logout.setOnClickListener((v) -> {
            passport.logout(this);
            Toast.makeText(this, "Logout-ed!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        GoogleHelper.onActivityResult(this,requestCode,resultCode,data);
    }
}