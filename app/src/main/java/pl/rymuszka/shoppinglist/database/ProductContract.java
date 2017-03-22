package pl.rymuszka.shoppinglist.database;

import android.provider.BaseColumns;

/**
 * Created by mateusz on 3/4/17.
 */

public class ProductContract {

    public static final class ProductCategory {
        public static final int FRUIT_VEGETABLES = 0;
        public static final int DAIRY_PRODUCTS = 1;
        public static final int BAKED_GOODS = 2;
        public static final int DRINKS = 3;
    }

    private ProductContract() {
    }

    public static final class ProductEntry implements BaseColumns {
        private ProductEntry() {
        }

        public static final String TABLE_NAME = "products";

        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_PRODUCT_CATEGORY =  "category";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_PRODUCT_UNITS = "units";

        public static final String COLUMN_TIMESTAMP = "timestamp";

    }
}
