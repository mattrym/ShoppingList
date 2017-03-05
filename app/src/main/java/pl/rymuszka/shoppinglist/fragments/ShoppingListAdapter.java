package pl.rymuszka.shoppinglist.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.rymuszka.shoppinglist.R;
import pl.rymuszka.shoppinglist.activities.ProductActivity;
import pl.rymuszka.shoppinglist.database.ProductContract;

/**
 * Created by mateusz on 3/4/17.
 */

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ProductViewHolder> {

    private Context context;
    private Cursor dbCursor;
    private ProductItemOnClickHandler onClickHandler;

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

        long productId = dbCursor.getLong(dbCursor.getColumnIndex(ProductContract.ProductEntry._ID));
        String productName = dbCursor.getString(dbCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME));
        double productQuantity = dbCursor.getDouble(dbCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY));
        String productUnits = dbCursor.getString(dbCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_UNITS));

        holder.itemView.setTag(productId);
        holder.productNameTextView.setText(productName);
        holder.productQuantityTextView.setText(String.valueOf(productQuantity));
        holder.productUnitsTextView.setText(productUnits);
    }

    @Override
    public int getItemCount() {
        return dbCursor.getCount();
    }

    public interface ProductItemOnClickHandler {
        void onClick(Bundle bundle);
    }

    public void setOnClickHandler(ProductItemOnClickHandler onClickHandler) {
        this.onClickHandler = onClickHandler;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView productNameTextView;
        private TextView productQuantityTextView;
        private TextView productUnitsTextView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            productNameTextView = (TextView) itemView.findViewById(R.id.tv_product_name);
            productQuantityTextView = (TextView) itemView.findViewById(R.id.tv_product_quantity);
            productUnitsTextView = (TextView) itemView.findViewById(R.id.tv_product_units);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();

            if(dbCursor.moveToPosition(adapterPosition)) {
                Bundle bundle = getProductBundle(dbCursor);
                onClickHandler.onClick(bundle);
            }
        }

        private Bundle getProductBundle(Cursor cursor) {
            int idColumn = cursor.getColumnIndex(ProductContract.ProductEntry._ID);
            int nameColumn = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            int quantityColumn = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int unitsColumn = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_UNITS);

            Bundle bundle = new Bundle();
            bundle.putLong(ProductContract.ProductEntry._ID, cursor.getLong(idColumn));
            bundle.putString(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, cursor.getString(nameColumn));
            bundle.putDouble(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, cursor.getDouble(quantityColumn));
            bundle.putString(ProductContract.ProductEntry.COLUMN_PRODUCT_UNITS, cursor.getString(unitsColumn));
            return bundle;
        }
    }
}
