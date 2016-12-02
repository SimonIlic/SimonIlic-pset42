package mprog.simon.simonilic_pset4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A SQLLiteOpenHelper class to assist in database management
 *
 * CRUD implements at the end of class
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // database information
    private static final String DB_NAME = "pset5-tasks";
    private static final int DB_VERSION = 1;

    //table name
    private static final String TABLE_NAME = "TASKS";

    // table columns
    public static final String _ID = "_id";
    public static final String TASK = "task";
    public static final String CHECKED = "checked";

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT NOT NULL, " + CHECKED + " INTEGER DEFAULT 0);";


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

    public SQLiteDatabase getDatabase(boolean write) {
        if (write) {
            return this.getWritableDatabase();
        }
        else {
            return this.getReadableDatabase();
        }
    }

    // CRUD methods

    public void insert(String task) {
        // open database
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValue = new ContentValues();
        contentValue.put(TASK, task);
        database.insert(TABLE_NAME, null, contentValue);

        // close database
        database.close();
    }

    public Cursor fetch() {
        // open database
        SQLiteDatabase database = this.getWritableDatabase();

        // get a cursor reading out the database
        String[] columns = new String[] {_ID, TASK, CHECKED};
        Cursor cursor = database.query(TABLE_NAME, columns,
                null, null, null, null, CHECKED + ", " + _ID + " DESC");
        if (cursor != null) {
            cursor.moveToFirst();
        }

        // close database
        database.close();

        return cursor;
    }

    public int update_task(long _id, String task) {
        // open database
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK, task);

        // update item
        int i = database.update(TABLE_NAME, contentValues, _ID + " = " + _id, null);

        // close database
        database.close();

        return i;
    }

    public int update_checked(long _id) {
        // open database
        SQLiteDatabase database = this.getWritableDatabase();

        // read out database to check if item is currently checked or not
        String[] columns = new String[] {CHECKED};
        Cursor cursor = database.query(TABLE_NAME, columns,
                _ID + " = " + _id, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        // flip current checked status
        int checked = cursor.getInt(0);
        if (checked == 1) {
            checked = 0;
        }
        else {
            checked = 1;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(CHECKED, checked);

        // update item
        int i = database.update(TABLE_NAME, contentValues, _ID + " = " + _id, null);

        // close database
        database.close();

        return i;
    }

    public void delete(long _id) {
        // open database
        SQLiteDatabase database = this.getWritableDatabase();

        // delete item
        database.delete(TABLE_NAME, _ID + "=" + _id, null);

        // close database
        database.close();
    }

}
