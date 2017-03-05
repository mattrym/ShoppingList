package pl.rymuszka.shoppinglist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mateusz on 3/5/17.
 */

public class ProductDatabase {

    private static ProductDatabase productDatabase;

    public static ProductDatabase getInstance(Context context) {
        if(productDatabase == null) {
            productDatabase = new ProductDatabase(context);
        }
        return productDatabase;
    }

    private SQLiteDatabase sqliteDatabase;

    private ProductDatabase(Context context) {
        sqliteDatabase = new ProductDBHelper(context).getWritableDatabase();
        ProductTestUtility.insertSampleData(sqliteDatabase);
    }

    public boolean addNewProduct(String productName, double productQuantity, String productUnits) {
        ContentValues values = new ContentValues();

        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_UNITS, productUnits);

        return sqliteDatabase.insert(ProductContract.ProductEntry.TABLE_NAME,
                null, values) > 0;
    }

    public boolean removeProduct(long id) {
        String deleteWhereClause = ProductContract.ProductEntry._ID + "=" + id;
        return sqliteDatabase.delete(ProductContract.ProductEntry.TABLE_NAME, deleteWhereClause, null) > 0;
    }

    public Cursor selectAllProductsFromDatabase() {
        //productDatabase.execSQL("DROP DATABASE " + ProductDbHelper.DATABASE_NAME);
        return sqliteDatabase.query(ProductContract.ProductEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ProductContract.ProductEntry.COLUMN_TIMESTAMP);
    }
}
