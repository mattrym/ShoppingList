package pl.rymuszka.shoppinglist.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import pl.rymuszka.shoppinglist.R;
import pl.rymuszka.shoppinglist.database.ProductDBHelper;
import pl.rymuszka.shoppinglist.database.ProductDatabase;
import pl.rymuszka.shoppinglist.database.ProductTestUtility;
import pl.rymuszka.shoppinglist.fragments.ShoppingListAdapter;
import pl.rymuszka.shoppinglist.fragments.ShoppingListFragment;

public class ShoppingListActivity extends AppCompatActivity {

    private ProductDatabase productDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Getting instance of the shopping list database
        productDatabase = ProductDatabase.getInstance(this);

        if(findViewById(R.id.product_list_container) != null) {
            if(savedInstanceState != null) {
                return;
            }
            ShoppingListFragment shoppingListFragment = new ShoppingListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.product_list_container, shoppingListFragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingListActivity.this, ProductActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_list, menu);
        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
