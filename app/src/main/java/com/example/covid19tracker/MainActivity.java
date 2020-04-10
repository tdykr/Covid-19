package com.example.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog loading;
    private ProgressBar loadCountry;
    private TextView tvTotalConfirmed;
    private TextView tvTodaysConfirmed;
    private TextView tvTotalDeaths;
    private TextView tvActive;
    private TextView tvTotalRecovered;
    private TextView tvAffecctedCountry;
    private ArrayList<HashMap<String,String>> countryArrayList = new ArrayList<>();
    private ArrayList<HashMap<String,String>> countrySearch = new ArrayList<>();
    private AdapterCountry adapterCountry;
    private RecyclerView countryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        loadSummary();
        loadCountry();

        loadCountry = findViewById(R.id.load);
        loadCountry.setVisibility(View.VISIBLE);
        tvTotalConfirmed = findViewById(R.id.tvTotalConfirmed);
        tvTodaysConfirmed = findViewById(R.id.tvTodaysConfirmed);
        tvTotalDeaths = findViewById(R.id.tvTotalDeaths);
        tvActive = findViewById(R.id.tvActive);
        tvTotalRecovered = findViewById(R.id.tvTotalRecovered);
        tvAffecctedCountry = findViewById(R.id.tvAffecctedCountry);
        TextView tvDate = findViewById(R.id.date);
        LinearLayout data = findViewById(R.id.linear);
        countryListView = findViewById(R.id.countryList);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("E,dd MMM yyyy");
        Date currentTime = Calendar.getInstance().getTime();

        tvDate.setText(sdf.format(currentTime));
        final EditText searchValueET = findViewById(R.id.etSearch);
        Button btnSearchCountry = findViewById(R.id.btnSearch);

        btnSearchCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String param = searchValueET.getText().toString();
                if(param.equals("") || param.equals(null) || param.isEmpty()){

                    adapterCountry = new AdapterCountry(MainActivity.this,countryArrayList);
                    countryListView.setHasFixedSize(true);
                    countryListView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    countryListView.setAdapter(adapterCountry);
                }else{
                    searchCountry(param);
                }
            }
        });


    }

    private void loadSummary(){
        loading = ProgressDialog.show(MainActivity.this, "Loading Data...", "Please Wait...", false, false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET, APIConfig.URL_SUMMARY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    tvTotalConfirmed.setText(jsonObject.getString("cases"));
                    tvTodaysConfirmed.setText(jsonObject.getString("todayCases"));
                    tvTotalDeaths.setText(jsonObject.getString("deaths"));
                    tvActive.setText(jsonObject.getString("active"));
                    tvTotalRecovered.setText(jsonObject.getString("recovered"));
                    tvAffecctedCountry.setText(jsonObject.getString("critical"));

                }catch (Exception e){
                    e.printStackTrace();
                }
                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Log.d("tag", String.valueOf(error));
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void loadCountry(){

        RequestQueue mRequestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET, APIConfig.URL_LIST_COUNTRIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray result = new JSONArray(response);
                    for(int i=0;i<result.length();i++){
                        JSONObject jsonObject = result.getJSONObject(i);

                        String country = jsonObject.getString("country");
                        String cases = jsonObject.getString("cases");
                        String active = jsonObject.getString("active");
                        String deaths = jsonObject.getString("deaths");
                        String critical = jsonObject.getString("critical");
                        String recovered = jsonObject.getString("recovered");

                        JSONObject countryInfo = jsonObject.getJSONObject("countryInfo");
                        String flag = countryInfo.getString("flag");

                        HashMap<String, String> countryData = new HashMap<>();
                        countryData.put("COUNTRY",country);
                        countryData.put("ACTIVE",active);
                        countryData.put("CASES",cases);
                        countryData.put("DEATHS",deaths);
                        countryData.put("CRITICAL",critical);
                        countryData.put("RECOVERED",recovered);
                        countryData.put("FLAG",flag);

                        countryArrayList.add(countryData);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                loadCountry.setVisibility(View.GONE);
                adapterCountry = new AdapterCountry(MainActivity.this,countryArrayList);
                countryListView.setHasFixedSize(true);
                countryListView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                countryListView.setAdapter(adapterCountry);

                Log.d("CountryList", countryArrayList.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", String.valueOf(error));
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void searchCountry(String param){

        RequestQueue mRequestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET, APIConfig.URL_LIST_COUNTRIES_SEARCH(param), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());

                    countrySearch.clear();

                    String country = jsonObject.getString("country");
                    String cases = jsonObject.getString("cases");
                    String active = jsonObject.getString("active");
                    String deaths = jsonObject.getString("deaths");
                    String critical = jsonObject.getString("critical");
                    String recovered = jsonObject.getString("recovered");

                    JSONObject countryInfo = jsonObject.getJSONObject("countryInfo");
                    String flag = countryInfo.getString("flag");

                    HashMap<String, String> countryData = new HashMap<>();
                    countryData.put("COUNTRY",country);
                    countryData.put("ACTIVE",active);
                    countryData.put("CASES",cases);
                    countryData.put("DEATHS",deaths);
                    countryData.put("CRITICAL",critical);
                    countryData.put("RECOVERED",recovered);
                    countryData.put("FLAG",flag);

                    countrySearch.add(countryData);

                }catch (Exception e){
                    e.printStackTrace();
                }
                loadCountry.setVisibility(View.GONE);
                adapterCountry = new AdapterCountry(MainActivity.this,countrySearch);
                countryListView.setHasFixedSize(true);
                countryListView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                countryListView.setAdapter(adapterCountry);
                Log.d("CountryList", countrySearch.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", String.valueOf(error));
                Toast.makeText(MainActivity.this, "Country not found or doesn't have any cases", Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(mStringRequest);
    }
}
