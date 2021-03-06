package pl.rymuszka.shoppinglist.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.transition.ChangeBounds;
import android.support.transition.Scene;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionValues;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import pl.rymuszka.shoppinglist.R;
import pl.rymuszka.shoppinglist.database.ProductContract;

/**
 * Created by mateusz on 3/4/17.
 */

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ProductViewHolder> {

    private final RecyclerView recyclerView;
    private int expandedPosition = -1;
    private Context context;
    private Cursor dbCursor;
    private ProductItemOnLongClickHandler onLongClickHandler;

    public ShoppingListAdapter(Context context, Cursor dbCursor, RecyclerView recyclerView) {
        this.context = context;
        this.dbCursor = dbCursor;
        this.recyclerView = recyclerView;
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
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
        dbCursor.moveToPosition(position);

        final boolean isExpanded = position == expandedPosition;

        if(isExpanded){
            holder.itemView.setBackground(context.getDrawable(R.drawable.selected_product_item_background));
        } else {
            holder.itemView.setBackground(context.getDrawable(R.drawable.product_item_background));
        }
        holder.productCategoryImageView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.productQuantityTextView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.productUnitsTextView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);


        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedPosition = isExpanded ? -1 : position;

                notifyDataSetChanged();
            }
        });

        holder.onBind();

    }

    @Override
    public void onViewRecycled(ProductViewHolder holder) {
        holder.itemView.setOnClickListener(null);
    }

    @Override
    public int getItemCount() {
        return dbCursor.getCount();
    }

    public interface ProductItemOnLongClickHandler {
        void onLongClick(Bundle bundle);
    }

    public void setOnLongClickHandler(ProductItemOnLongClickHandler onLongClickHandler) {
        this.onLongClickHandler = onLongClickHandler;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private TextView productNameTextView;
        private TextView productQuantityTextView;
        private TextView productUnitsTextView;
        private ImageView productCategoryImageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            productNameTextView = (TextView) itemView.findViewById(R.id.tv_product_name);
            productQuantityTextView = (TextView) itemView.findViewById(R.id.tv_product_quantity);
            productUnitsTextView = (TextView) itemView.findViewById(R.id.tv_product_units);
            productCategoryImageView = (ImageView) itemView.findViewById(R.id.iv_product_category);

            itemView.setOnLongClickListener(this);

        }

        @Override
        public boolean onLongClick(View v) {
            int adapterPosition = getAdapterPosition();

            if(dbCursor.moveToPosition(adapterPosition)) {
                Bundle bundle = getProductBundle(dbCursor);
                onLongClickHandler.onLongClick(bundle);
                return true;
            }
            return false;
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
            int productCategory = dbCursor.getInt(dbCursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_CATEGORY));

            setFontSize(fontSize);
            setFontColor(fontColor);

            itemView.setTag(productId);
            productNameTextView.setText(productName);
            productQuantityTextView.setText(String.valueOf(productQuantity));
            productUnitsTextView.setText(productUnits);
            setCategoryIcon(productCategory);
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

        private void setCategoryIcon(int category) {
            Drawable categoryIcon = null;
            switch (category) {
                case ProductContract.ProductCategory.FRUIT_VEGETABLES:
                    categoryIcon = ContextCompat.getDrawable(context, R.drawable.category_fruit_vegetables);
                    break;
                case ProductContract.ProductCategory.BAKED_GOODS:
                    categoryIcon = ContextCompat.getDrawable(context, R.drawable.category_baked_goods);
                    break;
                case ProductContract.ProductCategory.DAIRY_PRODUCTS:
                    categoryIcon = ContextCompat.getDrawable(context, R.drawable.category_dairy_products);
                    break;
                case ProductContract.ProductCategory.DRINKS:
                    categoryIcon = ContextCompat.getDrawable(context, R.drawable.category_drinks);
                    break;
            }
            productCategoryImageView.setImageDrawable(categoryIcon);
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
