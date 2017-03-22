package pl.rymuszka.shoppinglist.database;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by mateusz on 3/4/17.
 */

public final class ProductTestUtility {

    public static void insertSampleData(SQLiteDatabase database) {
        if(database == null) {
            return;
        }

        ContentValues[] contentValues = new ContentValues[6];
        for(int i = 0; i < contentValues.length; ++i) {
            contentValues[i] = new ContentValues();
        }

        contentValues[0].put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, "Pomidory");
        contentValues[0].put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, 1.5);
        contentValues[0].put(ProductContract.ProductEntry.COLUMN_PRODUCT_UNITS, "kg");
        contentValues[0].put(ProductContract.ProductEntry.COLUMN_PRODUCT_CATEGORY,
                ProductContract.ProductCategory.FRUIT_VEGETABLES);

        contentValues[1].put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, "Masło");
        contentValues[1].put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, 200);
        contentValues[1].put(ProductContract.ProductEntry.COLUMN_PRODUCT_UNITS, "g");
        contentValues[1].put(ProductContract.ProductEntry.COLUMN_PRODUCT_CATEGORY,
                ProductContract.ProductCategory.DAIRY_PRODUCTS);

        contentValues[2].put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, "Ser");
        contentValues[2].put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, 15);
        contentValues[2].put(ProductContract.ProductEntry.COLUMN_PRODUCT_UNITS, "dag");
        contentValues[2].put(ProductContract.ProductEntry.COLUMN_PRODUCT_CATEGORY,
                ProductContract.ProductCategory.DAIRY_PRODUCTS);

        contentValues[3].put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, "Woda");
        contentValues[3].put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, 1.5);
        contentValues[3].put(ProductContract.ProductEntry.COLUMN_PRODUCT_UNITS, "l");
        contentValues[3].put(ProductContract.ProductEntry.COLUMN_PRODUCT_CATEGORY,
                ProductContract.ProductCategory.DRINKS);

        contentValues[4].put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, "Bułki");
        contentValues[4].put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, 6);
        contentValues[4].put(ProductContract.ProductEntry.COLUMN_PRODUCT_UNITS, "szt.");
        contentValues[4].put(ProductContract.ProductEntry.COLUMN_PRODUCT_CATEGORY,
                ProductContract.ProductCategory.BAKED_GOODS);

        try {
            database.beginTransaction();
            database.delete(ProductContract.ProductEntry.TABLE_NAME, null, null);
            for(ContentValues cv : contentValues) {
                database.insert(ProductContract.ProductEntry.TABLE_NAME, null, cv);
            }
            database.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            database.endTransaction();
        }

    }
}