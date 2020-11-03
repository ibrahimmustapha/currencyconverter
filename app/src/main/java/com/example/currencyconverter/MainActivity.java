package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/*
 Created by Ibrahim
 on 30 - 10 - 2020
 */

public class MainActivity extends AppCompatActivity {

    Button btnConvert;
    TextView tvAmountConverted;
    EditText etAmount;
    Spinner spinnerTo, spinnerFrom;
    ArrayAdapter<String> adapter;

    public void convertButtton(View view) {
        RetrofitInterface retrofitInterface = RetrofitBuilder.getRetrofitInstance().create(RetrofitInterface.class);
        Call<JsonObject> call =  retrofitInterface.getExchangeCurrency(spinnerFrom.getSelectedItem().toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (etAmount == null) {
                    Toast.makeText(MainActivity.this, "Please enter an amount!", Toast.LENGTH_SHORT).show();
                }
                try {
                    JsonObject jsonObject = response.body();
                    JsonObject rates = jsonObject.getAsJsonObject("rates");
                    double currency = Double.valueOf(etAmount.getText().toString());
                    double multiplier = Double.valueOf(rates.get(spinnerTo.getSelectedItem().toString()).toString());
                    double result = currency * multiplier;
                    tvAmountConverted.setText(String.valueOf(result));
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Choose a currency!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConvert = findViewById(R.id.btnConvert);
        tvAmountConverted = findViewById(R.id.tvAmountConverted);
        etAmount = findViewById(R.id.etAmount);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);

        String[] ratesInfo = {"CAD", "HKD", "ISK", "PHP", "DKK", "USD", "EUR", "ARS", "IDR","ILS","INR","ISK","JPY","KRW","KZT","MVR","MXN","MYR","NOK","NZD","PAB","PEN","PHP","PKR","PLN","PYG","RON","RUB","SAR","SEK","SGD","THB","TRY","TWD","UAH","UYU","ZAR"};


        // spinner dropdownmenu implementation for spinner to
        ArrayAdapter arrayAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ratesInfo);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTo.setAdapter(arrayAdapter);
        spinnerFrom.setAdapter(arrayAdapter);


    }

}