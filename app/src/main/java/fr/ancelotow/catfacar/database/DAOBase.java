package fr.ancelotow.catfacar.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DAOBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "catfacarDB.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static DAOBase sInstance;

    public static synchronized DAOBase getInstance(Context context) {
        if (sInstance == null) { sInstance = new DAOBase(context); }
        return sInstance;
    }

    private DAOBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(UserDAO.CREATE_TABLE);
        sqLiteDatabase.execSQL(LivreDAO.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        onCreate(sqLiteDatabase);
    }

}
