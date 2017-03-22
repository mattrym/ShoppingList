package pl.rymuszka.shoppinglist.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.Text;

import pl.rymuszka.shoppinglist.R;
import pl.rymuszka.shoppinglist.database.ProductContract;
import pl.rymuszka.shoppinglist.database.ProductDatabase;

public class ProductActivity extends AppCompatActivity {

    private long productId;
    private String productName;
    private double productQuantity;
    private String productUnits;
    private int productCategory;

    private EditText productNameEditText;
    private EditText productQuantityEditText;
    private EditText productUnitsEditText;
    private Spinner productCategorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productNameEditText = (EditText) findViewById(R.id.et_product_name);
        productQuantityEditText = (EditText) findViewById(R.id.et_product_quantity);
        productUnitsEditText = (EditText) findViewById(R.id.et_product_units);
        productCategorySpinner = (Spinner) findViewById(R.id.spinner_product_category);
        initializeSpinner();

        productId = -1;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            getDataFromBundle(bundle);
        }
    }

    protected void initializeSpinner() {
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.product_categories, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productCategorySpinner.setAdapter(arrayAdapter);
    }

    public void saveProductInfo(View view) {
        if (TextUtils.isEmpty(productNameEditText.getText()) ||
                TextUtils.isEmpty(productQuantityEditText.getText()) ||
                TextUtils.isEmpty(productUnitsEditText.getText())) {
            Toast.makeText(this, "Fill in all the fields properly!", Toast.LENGTH_SHORT).show();
            return;
        }

        ProductDatabase database = ProductDatabase.getInstance(this);
        if(!getDataFromEditViews()) {
            Toast.makeText(this, "Fill in all the fields properly!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (productId >= 0){
            if(database.updateProduct(productId, productName, productQuantity, productUnits, productCategory)) {
                finish();
            }
        } else {
            if(database.addNewProduct(productName, productQuantity, productUnits, productCategory)) {
                finish();
            }
        }

    }
    private void getDataFromBundle(Bundle bundle) {
        productId = (long) bundle.getLong(ProductContract.ProductEntry._ID);
        productName = bundle.getString(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        productQuantity = bundle.getDouble(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        productUnits = bundle.getString(ProductContract.ProductEntry.COLUMN_PRODUCT_UNITS);
        productCategory = bundle.getInt(ProductContract.ProductEntry.COLUMN_PRODUCT_CATEGORY);

        productNameEditText.setText(productName);
        productQuantityEditText.setText(Double.toString(productQuantity));
        productUnitsEditText.setText(productUnits);
        productCategorySpinner.setSelection(productCategory);
    }

    private boolean getDataFromEditViews() {
        try {
            productName = productNameEditText.getText().toString();
            productQuantity = Double.parseDouble(productQuantityEditText.getText().toString());
            productUnits = productUnitsEditText.getText().toString();
            productCategory = productCategorySpinner.getSelectedItemPosition();
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
