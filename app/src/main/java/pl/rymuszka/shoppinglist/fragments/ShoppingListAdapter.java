package pl.rymuszka.shoppinglist.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
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
        holder.onBind();
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

        public void onBind() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String fontSize = sharedPreferences.getString(context.getString(R.string.pref_font_size_key),
                    context.getString(R.string.pref_font_size_default_value));
            String fontColor = sharedPreferences.getString(context.getString(R.string.pref_font_color_key),
                    context.getString(R.string.pref_font_color_default_value));

            long productId = dbCursor.getLong(dbCursor.getColumnIndex(ProductContract.ProductEntry._ID));
            String productName = dbCursor.getString(dbCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME));
            double productQuantity = dbCursor.getDouble(dbCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY));
            String productUnits = dbCursor.getString(dbCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_UNITS));

            setFontSize(fontSize);
            setFontColor(fontColor);

            itemView.setTag(productId);
            productNameTextView.setText(productName);
            productQuantityTextView.setText(String.valueOf(productQuantity));
            productUnitsTextView.setText(productUnits);
        }

        private void setFontSize(String fontSize) {
            float dimension = context.getResources().getDimension(R.dimen.font_size_default);
            if(fontSize.equals(context.getString(R.string.pref_font_size_small_value))) {
                dimension = context.getResources().getDimension(R.dimen.font_size_small);
            } else if(fontSize.equals(context.getString(R.string.pref_font_size_medium_value))) {
                dimension = context.getResources().getDimension(R.dimen.font_size_medium);
            } else if(fontSize.equals(context.getString(R.string.pref_font_size_large_value))) {
                dimension = context.getResources().getDimension(R.dimen.font_size_large);
            } else if(fontSize.equals(context.getString(R.string.pref_font_size_very_large_value))) {
                dimension = context.getResources().getDimension(R.dimen.font_size_very_large);
            }

            if(productNameTextView != null){
                productNameTextView.setTextSize(dimension);
            }
            if(productQuantityTextView != null) {
                productQuantityTextView.setTextSize(dimension);
            }
            if(productUnitsTextView != null) {
                productUnitsTextView.setTextSize(dimension);
            }
        }

        private void setFontColor(String fontColor) {
            int color = ContextCompat.getColor(context, R.color.font_color_default);
            if(fontColor.equals(context.getString(R.string.pref_font_color_red_value))) {
                color = ContextCompat.getColor(context, R.color.font_color_red);
            } else if (fontColor.equals(context.getString(R.string.pref_font_color_green_value))) {
                color = ContextCompat.getColor(context, R.color.font_color_green);
            } else if (fontColor.equals(context.getString(R.string.pref_font_color_blue_value))) {
                color = ContextCompat.getColor(context, R.color.font_color_blue);
            }

            if(productNameTextView != null){
                productNameTextView.setTextColor(color);
            }
            if(productQuantityTextView != null) {
                productQuantityTextView.setTextColor(color);
            }
            if(productUnitsTextView != null) {
                productUnitsTextView.setTextColor(color);
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
