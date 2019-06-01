package fr.ancelotow.catfacar;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import fr.ancelotow.catfacar.database.UserDAO;
import fr.ancelotow.catfacar.entities.User;
import fr.ancelotow.catfacar.technique.Internet;
import fr.ancelotow.catfacar.technique.Session;

public class LoadActivity extends AppCompatActivity {

    private final static int TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                if(Internet.isConnectedInternet(LoadActivity.this))
                {
                    UserDAO db = new UserDAO(LoadActivity.this);
                    db.ouvrir();
                    User user = db.getUser();
                    System.out.println("////////////" + user +"//////////////////");
                    db.fermer();
                    if(user == null){
                        Intent i = new Intent(LoadActivity.this,
                                FirstUseActivity.class);
                        startActivity(i);
                    }
                    else{
                        Session.ouvrir(user);
                        Intent i = new Intent(LoadActivity.this,
                                MainActivity.class);
                        startActivity(i);
                    }
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
