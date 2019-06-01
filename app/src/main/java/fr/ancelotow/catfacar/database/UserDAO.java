package fr.ancelotow.catfacar.database;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.ancelotow.catfacar.entities.User;

public class UserDAO{

    private static final String TABLE_NAME ="User";
    public static final String COL_ID_PK = "user_id";
    public static final String COL_NOM = "user_nom";
    public static final String COL_PRENOM = "user_prenom";
    public static final String COL_TEL = "user_tel";
    public static final String COL_EMAIL = "user_email";
    public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"( \n" +
            COL_ID_PK+" INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
            COL_NOM+" TEXT, \n" +
            COL_PRENOM+" TEXT, \n" +
            COL_TEL+" TEXT, \n" +
            COL_EMAIL+" TEXT \n" +
            ");";
    private DAOBase maBase;
    private SQLiteDatabase db;

    public UserDAO(Context context)
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
    public long addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COL_NOM, user.getNom());
        values.put(COL_PRENOM, user.getPrenom());
        values.put(COL_TEL, user.getTel());
        values.put(COL_EMAIL, user.getEmail());
        return db.insert(TABLE_NAME,null,values);
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.N)
    public User getUser(){
        User user = new User();
        Cursor cur = db.rawQuery(
                "SELECT * " +
                        "FROM "+TABLE_NAME, null
        );
        if (cur.moveToFirst()) {
            user.setNom(cur.getString(cur.getColumnIndex(COL_NOM)));
            user.setPrenom(cur.getString(cur.getColumnIndex(COL_PRENOM)));
            user.setTel(cur.getString(cur.getColumnIndex(COL_TEL)));
            user.setEmail(cur.getString(cur.getColumnIndex(COL_EMAIL)));
            cur.close();
        }
        else{
            user = null;
        }
        return user;
    }

}