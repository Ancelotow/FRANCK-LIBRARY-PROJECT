package fr.ancelotow.catfacar;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import fr.ancelotow.catfacar.technique.Internet;

public class LoadActivity extends AppCompatActivity {

    private final static int TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Internet.isConnectedInternet(LoadActivity.this))
                {
                    Intent i = new Intent(LoadActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(LoadActivity.this,
                            "Vous n'êtes pas connecter à internet.",
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(LoadActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, TIME_OUT);
    }

}
