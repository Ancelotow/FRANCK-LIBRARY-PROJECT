package fr.ancelotow.catfacar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.ancelotow.catfacar.entities.Livre;
import fr.ancelotow.catfacar.technique.Session;

public class ResConfirm extends AppCompatActivity {

    Button btnRetour;
    TextView tvNumRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_confirm);
        setTitle(Session.getSession().getUser().getNom().toUpperCase()  +
                Session.getSession().getUser().getPrenom());
        Intent intent = getIntent();
        Bundle pack = this.getIntent().getExtras();
        String res = pack.getString("numRes");
        System.out.println("/////////" + res + " //////////");
        btnRetour = (Button) findViewById(R.id.btnRetour);
        tvNumRes = (TextView) findViewById(R.id.tvNumRes);
        tvNumRes.setText(res);
        View.OnClickListener retourner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResConfirm.this,
                        MainActivity.class);
                startActivity(i);
            }
        };
        btnRetour.setOnClickListener(retourner);
    }
}
