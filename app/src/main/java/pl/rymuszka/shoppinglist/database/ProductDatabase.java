package pl.rymuszka.shoppinglist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mateusz on 3/5/17.
 */

public class ProductDatabase {

    private static ProductDatabase productDatabase;
    private final ProductDBHelper productDatabaseHelper;

    public static ProductDatabase getInstance(Context context) {
        if(productDatabase == null) {
            productDatabase = new ProductDatabase(context);
        }
        return productDatabase;
    }

    private SQLiteDatabase sqliteDatabase;

    private ProductDatabase(Context context) {
        productDatabaseHelper = new ProductDBHelper(context);
        sqliteDatabase = productDatabaseHelper.getWritableDatabase();
        ProductTestUtility.insertSampleData(sqliteDatabase);
    }

    public boolean addNewProduct(String productName, double productQuantity, String productUnits, int productCategory) {
        ContentValues values = new ContentValues();

        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_UNITS, productUnits);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_CATEGORY, productCategory);

        return sqliteDatabase.insert(ProductContract.ProductEntry.TABLE_NAME,
                null, values) > 0;
    }

    public Cursor getProductById(long id) {
        String[] selectedColumns = new String[]{
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductContract.ProductEntry.COLUMN_PRODUCT_UNITS,
                ProductContract.ProductEntry.COLUMN_PRODUCT_CATEGORY
        };

        return sqliteDatabase.query(ProductContract.ProductEntry.TABLE_NAME,
                selectedColumns,
                ProductContract.ProductEntry._ID + "=" + id,
                null,
                null,
                null,
                null);
    }

    public boolean updateProduct(long productId, String productName, double productQuantity, String productUnits, int productCategory) {
        ContentValues cv = new ContentValues();
        cv.put(ProductContract.ProductEntry._ID, productId);
        cv.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, productName);
        cv.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, productQuantity);
        cv.put(ProductContract.ProductEntry.COLUMN_PRODUCT_UNITS, productUnits);
        cv.put(ProductContract.ProductEntry.COLUMN_PRODUCT_CATEGORY, productCategory);

        String updateWhereClause = ProductContract.ProductEntry._ID + "=" + productId;

        return sqliteDatabase.update(ProductContract.ProductEntry.TABLE_NAME,
                cv,
                updateWhereClause,
                null) > 0;
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

    public void close() {
        productDatabaseHelper.close();
        productDatabase.close();
        productDatabase = null;
    }
}
