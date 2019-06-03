package fr.ancelotow.catfacar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fr.ancelotow.catfacar.technique.Session;

public class MainActivity extends AppCompatActivity {

    Button btnCommande;
    Button btnHist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(Session.getSession().getUser().getNom().toUpperCase()  +
                Session.getSession().getUser().getPrenom());
        btnCommande = (Button) findViewById(R.id.btnCommande);
        btnHist = (Button) findViewById(R.id.btnHist);
        View.OnClickListener commander = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,
                        CommandeActivity.class);
                startActivity(i);
            }
        };
        btnCommande.setOnClickListener(commander);
    }
}
