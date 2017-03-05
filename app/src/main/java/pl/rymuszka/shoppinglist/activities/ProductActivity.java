package pl.rymuszka.shoppinglist.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

import pl.rymuszka.shoppinglist.R;
import pl.rymuszka.shoppinglist.database.ProductDatabase;

public class ProductActivity extends AppCompatActivity {

    private EditText productNameEditText;
    private EditText productQuantityEditText;
    private EditText productUnitsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productNameEditText = (EditText) findViewById(R.id.et_product_name);
        productQuantityEditText = (EditText) findViewById(R.id.et_product_quantity);
        productUnitsEditText = (EditText) findViewById(R.id.et_product_units);
    }

    public void addNewGuest(View view) {
        if(TextUtils.isEmpty(productNameEditText.getText()) ||
                TextUtils.isEmpty(productQuantityEditText.getText()) ||
                TextUtils.isEmpty(productUnitsEditText.getText())) {
            Toast.makeText(this, "Fill in all the fields properly!", Toast.LENGTH_SHORT).show();
            return;
        }

        String productName = productNameEditText.getText().toString();
        double productQuantity = Double.parseDouble(productQuantityEditText.getText().toString());
        String productUnits = productUnitsEditText.getText().toString();

        ProductDatabase.getInstance(this).addNewProduct(productName, productQuantity, productUnits);
        finish();
    }
}
