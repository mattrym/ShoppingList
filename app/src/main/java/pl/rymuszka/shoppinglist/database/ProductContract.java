package pl.rymuszka.shoppinglist.database;

import android.provider.BaseColumns;

/**
 * Created by mateusz on 3/4/17.
 */

public class ProductContract {

    private ProductContract() {
    }

    public static final class ProductEntry implements BaseColumns {
        private ProductEntry() {
        }

        public static final String TABLE_NAME = "products";

        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_PRODUCT_UNITS = "units";

        public static final String COLUMN_TIMESTAMP = "timestamp";

    }
}
