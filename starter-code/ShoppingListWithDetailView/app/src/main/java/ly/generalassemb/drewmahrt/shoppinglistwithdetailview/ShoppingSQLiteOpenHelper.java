package ly.generalassemb.drewmahrt.shoppinglistwithdetailview;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by drewmahrt on 12/28/15.
 */
public class ShoppingSQLiteOpenHelper extends SQLiteOpenHelper{
    private static final String TAG = ShoppingSQLiteOpenHelper.class.getCanonicalName();

    private static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "SHOPPING_DB";
    public static final String SHOPPING_LIST_TABLE_NAME = "SHOPPING_LIST";

    public static final String COL_ID = "_id";
    public static final String COL_ITEM_NAME = "ITEM_NAME";
    public static final String COL_ITEM_PRICE = "PRICE";
    public static final String COL_ITEM_DESCRIPTION = "DESCRIPTION";
    public static final String COL_ITEM_TYPE = "TYPE";
    public static final String COL_ITEM_NUM = "NUM";

    public static final String[] SHOPPING_COLUMNS = {COL_ID,COL_ITEM_NAME,COL_ITEM_DESCRIPTION,COL_ITEM_PRICE,COL_ITEM_TYPE, COL_ITEM_NUM};

    private static final String CREATE_SHOPPING_LIST_TABLE =
            "CREATE TABLE " + SHOPPING_LIST_TABLE_NAME +
                    "(" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_ITEM_NAME + " TEXT, " +
                    COL_ITEM_DESCRIPTION + " TEXT, " +
                    COL_ITEM_PRICE + " TEXT, " +
                    COL_ITEM_TYPE + " TEXT, " +
                    COL_ITEM_NUM + " INT)";

    private static ShoppingSQLiteOpenHelper instance;

    private ShoppingSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static ShoppingSQLiteOpenHelper getInstance(Context context){
        if (instance == null) {
            instance = new ShoppingSQLiteOpenHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SHOPPING_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SHOPPING_LIST_TABLE_NAME);
        this.onCreate(db);
    }

    //Add new itinerary list
    public long addItem(String name, String description, String price, String type){
        ContentValues values = new ContentValues();
        values.put(COL_ITEM_NAME, name);
        values.put(COL_ITEM_DESCRIPTION, description);
        values.put(COL_ITEM_PRICE, price);
        values.put(COL_ITEM_TYPE, type);
        values.put(COL_ITEM_NUM, 0);

        SQLiteDatabase db = this.getWritableDatabase();
        long returnId = db.insert(SHOPPING_LIST_TABLE_NAME, null, values);
        db.close();
        return returnId;
    }

    public void updateTableWithItemNum(){
        boolean columnExists = false;
        SQLiteDatabase checkDb = getReadableDatabase();
        Cursor cursor = checkDb.query(SHOPPING_LIST_TABLE_NAME, null, null, null, null, null, null, "1");
        if(cursor.getColumnIndexOrThrow(COL_ITEM_NUM) != -1){
            columnExists = true;
        }
        cursor.close();
        checkDb.close();
        SQLiteDatabase db = getWritableDatabase();
        if(!columnExists) {
            db.execSQL("ALTER TABLE " + SHOPPING_LIST_TABLE_NAME + " ADD " + COL_ITEM_NUM + " INT");
        }
        ContentValues values = new ContentValues();
        values.put(COL_ITEM_NUM, 0);
        db.updateWithOnConflict(SHOPPING_LIST_TABLE_NAME, values, null, null, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public Cursor getShoppingList(){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(SHOPPING_LIST_TABLE_NAME, // a. table
                SHOPPING_COLUMNS, // b. column names
                null, // c. selections
                null, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        return cursor;
    }

    public int deleteItem(int id){
        SQLiteDatabase db = getWritableDatabase();
        int deleteNum = db.delete(SHOPPING_LIST_TABLE_NAME,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return deleteNum;
    }

    public Cursor searchShoppingList(String query){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(SHOPPING_LIST_TABLE_NAME, // a. table
                SHOPPING_COLUMNS, // b. column names
                COL_ITEM_NAME + " LIKE ?", // c. selections
                new String[]{"%" + query + "%"}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        return cursor;
    }

    public Cursor getShoppingListItemById(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(SHOPPING_LIST_TABLE_NAME, // a. table
                null, // b. column names
                COL_ID + " = ?", // c. selections
                new String[]{id + ""}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        return cursor;
    }
}
