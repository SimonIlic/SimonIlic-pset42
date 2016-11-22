package mprog.simon.simonilic_pset4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Simon on 22-11-2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // database information
    private static final String DB_NAME = "pset4-tasks";
    private static final int DB_VERSION = 1;

    //table name
    private static final String TABLE_NAME = "TASKS";

    // table columns
    public static final String _ID = "_id";
    public static final String TASK = "task";

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT NOT NULL);";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // CRUD methods

    public void insert(String task) {
        // open database
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.TASK, task);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);

        // close database
        database.close();
    }

    public Cursor fetch() {
        // open database
        SQLiteDatabase database = this.getWritableDatabase();

        // get a cursor reading out the database
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.TASK};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns,
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        // close database
        database.close();

        return cursor;
    }

    public int update(long _id, String task) {
        // open database
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.TASK, task);

        // update item
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);

        // close database
        database.close();

        return i;
    }

    public void delete(long _id) {
        // open database
        SQLiteDatabase database = this.getWritableDatabase();

        // delete item
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);

        // close database
        database.close();
    }

}
