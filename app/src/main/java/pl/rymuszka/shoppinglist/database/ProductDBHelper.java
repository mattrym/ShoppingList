package pl.rymuszka.shoppinglist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mateusz on 3/4/17.
 */

public class ProductDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "products.db";

    private static final int DATABASE_VERSION = 1;

    public ProductDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateProductsTable = "CREATE TABLE " + ProductContract.ProductEntry.TABLE_NAME + " (" +
                ProductContract.ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY + " REAL NOT NULL, " +
                ProductContract.ProductEntry.COLUMN_PRODUCT_UNITS + " TEXT NOT NULL, " +
                ProductContract.ProductEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        db.execSQL(sqlCreateProductsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + ProductContract.ProductEntry.TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }
}
