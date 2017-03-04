package pl.rymuszka.shoppinglist.fragments;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.rymuszka.shoppinglist.R;
import pl.rymuszka.shoppinglist.database.ProductContract;

/**
 * Created by mateusz on 3/4/17.
 */

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ProductViewHolder> {

    private Context context;
    private Cursor dbCursor;

    public ShoppingListAdapter(Context context, Cursor dbCursor) {
        this.context = context;
        this.dbCursor = dbCursor;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        int layoutId = R.layout.shopping_list_item;
        boolean attachToParentImmediately = false;

        View view = layoutInflater.inflate(layoutId, parent, attachToParentImmediately);
        ProductViewHolder productViewHolder = new ProductViewHolder(view);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        dbCursor.moveToPosition(position);

        String productName = dbCursor.getString(dbCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME));
        double productQuantity = dbCursor.getDouble(dbCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY));
        String productUnits = dbCursor.getString(dbCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_UNITS));

        holder.productNameTextView.setText(productName);
        holder.productQuantityTextView.setText(String.valueOf(productQuantity));
        holder.productUnitsTextView.setText(productUnits);
    }

    @Override
    public int getItemCount() {
        return dbCursor.getCount();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView productNameTextView;
        private TextView productQuantityTextView;
        private TextView productUnitsTextView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            productNameTextView = (TextView) itemView.findViewById(R.id.tv_product_name);
            productQuantityTextView = (TextView) itemView.findViewById(R.id.tv_product_quantity);
            productUnitsTextView = (TextView) itemView.findViewById(R.id.tv_product_units);
        }
    }
}
