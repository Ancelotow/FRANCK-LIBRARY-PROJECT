package fr.ancelotow.catfacar;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import fr.ancelotow.catfacar.database.LivreDAO;
import fr.ancelotow.catfacar.entities.Livre;

public class HistoriqueActivity extends AppCompatActivity {

    List<Livre> livres = new ArrayList<Livre>();
    ListView lvLivres;
    Button btnRetour;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);
        setTitle("HISTORIQUE DES RESERVATIONS");
        lvLivres = (ListView) findViewById(R.id.lvLivres);
        btnRetour = (Button) findViewById(R.id.btnRetour);
        LivreDAO db = new LivreDAO(this);
        db.ouvrir();
        try {
            livres = db.getLivres();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        db.fermer();
        ItemLivreAdaptateur item = new ItemLivreAdaptateur();
        lvLivres.setAdapter(item);
        View.OnClickListener retourner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HistoriqueActivity.this,
                        MainActivity.class);
                startActivity(i);
            }
        };
        btnRetour.setOnClickListener(retourner);
    }


    class ItemLivreAdaptateur extends ArrayAdapter<Livre> {

        ItemLivreAdaptateur(){
            super(
                    HistoriqueActivity.this,
                    R.layout.item_livre,
                    R.id.tvRes,
                    livres
            );
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View vItem = super.getView(position, convertView, parent);
            DateTimeFormatter df = DateTimeFormatter.ofPattern("d/MM/uuuu");
            TextView tvNom = (TextView) vItem.findViewById(R.id.tvNom);
            TextView tvAuteur1 = (TextView) vItem.findViewById(R.id.tvAuteur1);
            TextView tvAuteur2 = (TextView) vItem.findViewById(R.id.tvAuteur2);
            TextView tvEdition = (TextView) vItem.findViewById(R.id.tvEdition);
            TextView tvDate = (TextView) vItem.findViewById(R.id.tvDate);
            tvNom.setText(livres.get(position).getNom());
            tvAuteur1.setText(livres.get(position).getAuteur1());
            tvAuteur2.setText(livres.get(position).getAuteur2());
            tvEdition.setText(livres.get(position).getEdition());
            tvDate.setText(String.valueOf(
                    (df.format(livres.get(position).getCommande()))
            ));
            return vItem;
        }
    }


}
