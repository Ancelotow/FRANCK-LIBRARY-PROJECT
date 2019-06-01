package fr.ancelotow.catfacar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fr.ancelotow.catfacar.database.UserDAO;
import fr.ancelotow.catfacar.entities.User;
import fr.ancelotow.catfacar.technique.Session;

public class CreateUserActivity extends AppCompatActivity {

    TextView tvError;
    EditText etNom;
    EditText etPrenom;
    EditText etTel;
    EditText etMail;
    Button btnAnnuler;
    Button btnCreer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        final String error = "Tout les champs doivent être remplis.";
        tvError = (TextView) findViewById(R.id.tvError);
        etNom = (EditText) findViewById(R.id.etNom);
        etPrenom = (EditText) findViewById(R.id.etPrenom);
        etTel = (EditText) findViewById(R.id.etTel);
        etMail = (EditText) findViewById(R.id.etMail);
        btnAnnuler = (Button) findViewById(R.id.btnAnnuler);
        btnCreer = (Button) findViewById(R.id.btnCreer);
        View.OnClickListener creer = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNom.getText().toString().equals("")){
                    tvError.setText(error);
                }
                else if(etPrenom.getText().toString().equals("")){
                    tvError.setText(error);
                }
                else if(etTel.getText().toString().equals("")){
                    tvError.setText(error);
                }
                else if(etMail.getText().toString().equals("")){
                    tvError.setText(error);
                }
                else{
                    User user = new User();
                    user.setNom(etNom.getText().toString());
                    user.setPrenom(etPrenom.getText().toString());
                    user.setTel(etTel.getText().toString());
                    user.setEmail(etMail.getText().toString());
                    UserDAO db = new UserDAO(CreateUserActivity.this);
                    db.ouvrir();
                    db.addUser(user);
                    db.fermer();
                    Session.ouvrir(user);
                    Toast.makeText(CreateUserActivity.this,
                            "Votre profil à été crée avec succès.",
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(CreateUserActivity.this,
                            MainActivity.class);
                    startActivity(i);
                }
            }
        };
        View.OnClickListener annuler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvError.setText("");
                etNom.setText("");
                etPrenom.setText("");
                etTel.setText("");
                etMail.setText("");
            }
        };
        btnCreer.setOnClickListener(creer);
        btnAnnuler.setOnClickListener(annuler);
    }
}
