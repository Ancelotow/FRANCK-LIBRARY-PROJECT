package fr.ancelotow.catfacar.database;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.ancelotow.catfacar.entities.Livre;

public class LivreDAO {

    private static final String TABLE_NAME ="Livre";
    public static final String COL_ID_PK = "liv_id";
    public static final String COL_RES = "liv_res";
    public static final String COL_NOM = "liv_nom";
    public static final String COL_AUTEUR1 = "liv_auteur1";
    public static final String COL_AUTEUR2 = "liv_auteur2";
    public static final String COL_EDITION = "liv_edition";
    public static final String COL_DATE = "liv_date";
    public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"( \n" +
            COL_ID_PK+" INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
            COL_RES+" INTEGER, \n" +
            COL_NOM+" TEXT, \n" +
            COL_AUTEUR1+" TEXT, \n" +
            COL_AUTEUR2+" TEXT, \n" +
            COL_EDITION+" TEXT, \n" +
            COL_DATE+" TEXT \n" +
            ");";
    private DAOBase maBase;
    private SQLiteDatabase db;

    public LivreDAO(Context context)
    {
        maBase = DAOBase.getInstance(context);
    }

    public void ouvrir()
    {
        db = maBase.getWritableDatabase();
    }

    public void fermer()
    {
        db.close();
    }

    @TargetApi(Build.VERSION_CODES.O)
    public long addLivre(Livre livre) {
        ContentValues values = new ContentValues();
        values.put(COL_RES, livre.getNumRes());
        values.put(COL_NOM, livre.getNom());
        values.put(COL_AUTEUR1, livre.getAuteur1());
        values.put(COL_AUTEUR2, livre.getAuteur2());
        values.put(COL_EDITION, livre.getEdition());
        values.put(COL_DATE, String.valueOf(livre.getCommande()));
        return db.insert(TABLE_NAME,null,values);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Livre> getLivres() throws ParseException {
        List<Livre> livres = new ArrayList<Livre>();
        Cursor cur =  db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        while(cur.moveToNext()){
            int numRes = cur.getInt(cur.getColumnIndex(COL_RES));
            String nom = cur.getString(cur.getColumnIndex(COL_NOM));
            String auteur1 = cur.getString(cur.getColumnIndex(COL_AUTEUR1));
            String auteur2 = cur.getString(cur.getColumnIndex(COL_AUTEUR2));
            String edition = cur.getString(cur.getColumnIndex(COL_EDITION));
            String strDate = cur.getString(cur.getColumnIndex(COL_DATE));
            Date dDate = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
            LocalDate date = dDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Livre liv = new Livre(numRes, nom, auteur1, auteur2, edition, date);
            livres.add(liv);
        }
        return livres;
    }

}
