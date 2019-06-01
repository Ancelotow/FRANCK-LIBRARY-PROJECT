package fr.ancelotow.catfacar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fr.ancelotow.catfacar.technique.Session;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(Session.getSession().getUser().getNom().toUpperCase() + " "  +
                Session.getSession().getUser().getPrenom());
    }
}
