package com.example.convertcurrencypro;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.convertcurrencypro.Retrofit.RetrofitBuilder;
import com.example.convertcurrencypro.Retrofit.RetrofitInterface;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText currencyToBeConverted;
    EditText currencyConverted;
    Spinner convertToDropdown;
    Spinner convertFromDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialization
        currencyConverted = (EditText) findViewById(R.id.currency_converted);
        currencyToBeConverted = (EditText) findViewById(R.id.currency_to_be_converted);
        convertToDropdown = (Spinner) findViewById(R.id.convert_to);
        convertFromDropdown = (Spinner) findViewById(R.id.convert_from);

        //Adding Functionality
        String[] dropDownList = {"INR", "INR", "EUR", "NZD", "AUD", "JPY", "SGD", "CAD", "GBP", "USD"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, dropDownList);
        convertToDropdown.setAdapter(adapter);
        convertFromDropdown.setAdapter(adapter);

        RetrofitInterface retrofitInterface = RetrofitBuilder.getRetrofitInstance().create(RetrofitInterface.class);
        Call<JsonObject> call = retrofitInterface.getExchangeCurrency(convertFromDropdown.getSelectedItem().toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject res = response.body();
                assert res != null;
                JsonObject rates = res.getAsJsonObject("rates");

                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            double currency = Double.parseDouble(currencyToBeConverted.getText().toString());
                            double multiplier = Double.parseDouble(rates.get(convertToDropdown.getSelectedItem().toString()).toString());
                            if (convertToDropdown.getSelectedItem().toString().equals(convertFromDropdown.getSelectedItem().toString())) {
                                Toast.makeText(getApplicationContext(), "You are choosing same currencies. Choose different currency, please!", Toast.LENGTH_LONG).show();
                                currencyConverted.setText(null);
                                currencyToBeConverted.setText(null);
                            } else {
                                double result = currency * multiplier;
                                currencyConverted.setText(String.valueOf(result));
                                Log.d("1---------", String.valueOf(multiplier));
                            }
                        } catch (
                                Exception e) {
                            Log.d("ignore", "-----");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };
                currencyToBeConverted.addTextChangedListener(textWatcher);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
