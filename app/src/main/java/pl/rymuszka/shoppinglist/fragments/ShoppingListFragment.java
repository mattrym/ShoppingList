package pl.rymuszka.shoppinglist.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.rymuszka.shoppinglist.R;
import pl.rymuszka.shoppinglist.database.ProductContract;
import pl.rymuszka.shoppinglist.database.ProductDBHelper;
import pl.rymuszka.shoppinglist.database.ProductTestUtility;

/**
 * A placeholder fragment containing a simple view.
 */
public class ShoppingListFragment extends Fragment {

    private RecyclerView productList;
    private ShoppingListAdapter productAdapter;
    private SQLiteDatabase productDatabase;

    public ShoppingListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        productList = (RecyclerView) view.findViewById(R.id.rv_product_list);
        productList.setLayoutManager(new LinearLayoutManager(getContext()));
        productList.setHasFixedSize(true);

//
//        if(productDatabase != null) {
//            attachProductAdapter();
//        }

        productDatabase = new ProductDBHelper(getContext()).getWritableDatabase();
        ProductTestUtility.insertSampleData(productDatabase);

        productAdapter = new ShoppingListAdapter(getContext(), selectAllProductsFromDatabase());
        productList.setAdapter(productAdapter);

        return view;
    }

    public void attachSQLiteDatabase(SQLiteDatabase database) {
        productDatabase = database;

        if(productList != null) {
            attachProductAdapter();
        }
    }

    private void attachProductAdapter() {
        productAdapter = new ShoppingListAdapter(getActivity(), selectAllProductsFromDatabase());
        productList.setAdapter(productAdapter);
    }

    private Cursor selectAllProductsFromDatabase() {
        //productDatabase.execSQL("DROP DATABASE " + ProductDbHelper.DATABASE_NAME);
        return productDatabase.query(ProductContract.ProductEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ProductContract.ProductEntry.COLUMN_TIMESTAMP);
    }
}
