package fr.ancelotow.catfacar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstUseActivity extends AppCompatActivity {

    Button btnContinuer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_use);
        btnContinuer = (Button) findViewById(R.id.btnContinuer);
        View.OnClickListener continuer = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstUseActivity.this,
                        CreateUserActivity.class);
                startActivity(i);
            }
        };
        btnContinuer.setOnClickListener(continuer);
    }

}
