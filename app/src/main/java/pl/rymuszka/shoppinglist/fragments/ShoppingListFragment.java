package pl.rymuszka.shoppinglist.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.rymuszka.shoppinglist.R;
import pl.rymuszka.shoppinglist.database.ProductContract;
import pl.rymuszka.shoppinglist.database.ProductDBHelper;
import pl.rymuszka.shoppinglist.database.ProductDatabase;
import pl.rymuszka.shoppinglist.database.ProductTestUtility;

/**
 * A placeholder fragment containing a simple view.
 */
public class ShoppingListFragment extends Fragment {

    private Cursor allProductsCursor;
    private RecyclerView productList;
    private ShoppingListAdapter productAdapter;
    private ProductDatabase productDatabase;

    public ShoppingListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        productList = (RecyclerView) view.findViewById(R.id.rv_product_list);
        productList.setLayoutManager(new LinearLayoutManager(getContext()));
        productList.setHasFixedSize(false);

        productDatabase = ProductDatabase.getInstance(getContext());

        attachProductAdapter();
        attachItemTouchHelper();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        attachProductAdapter();
    }

    private void attachProductAdapter() {
        if(allProductsCursor != null) {
            allProductsCursor.close();
        }

        allProductsCursor = productDatabase.selectAllProductsFromDatabase();
        productAdapter = new ShoppingListAdapter(getActivity(), allProductsCursor);
        productList.setAdapter(productAdapter);
    }

    private void attachItemTouchHelper() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                long id = (long) viewHolder.itemView.getTag();
                ProductDatabase.getInstance(getContext()).removeProduct(id);
                attachProductAdapter();
            }
        }).attachToRecyclerView(productList);
    }
}
