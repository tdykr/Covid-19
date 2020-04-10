package com.example.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class CountryDetailActivity extends AppCompatActivity {

    private ProgressBar loadProvinsi;
    private ArrayList<HashMap<String,String>> provinsiArrayList = new ArrayList<>();
    private RecyclerView provinsiListView;
    private AdapterProvinsi adapterProvinsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_detail);

        loadSummary();

        TextView tvDate = findViewById(R.id.date);
        loadProvinsi = findViewById(R.id.load);
        loadProvinsi.setVisibility(View.VISIBLE);
        provinsiListView = findViewById(R.id.provinsiList);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("E,dd MMM yyyy");
        Date currentTime = Calendar.getInstance().getTime();
        tvDate.setText(sdf.format(currentTime));

    }

    private void loadSummary(){
        RequestQueue mRequestQueue = Volley.newRequestQueue(CountryDetailActivity.this);

        StringRequest mStringRequest = new StringRequest(Request.Method.GET, APIConfig.URL_DETAIL_INDONESIA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ResponseProvinsi", response);
                try {
                    JSONArray result = new JSONArray(response);
                    for(int i=0;i<result.length();i++){
                        JSONObject jsonObject = result.getJSONObject(i);
                        JSONObject provinsiData = jsonObject.getJSONObject("attributes");

                        String provinsi = provinsiData.getString("Provinsi");
                        String cases = provinsiData.getString("Kasus_Posi");
                        String deaths = provinsiData.getString("Kasus_Meni");
                        String recovered = provinsiData.getString("Kasus_Semb");

                        HashMap<String, String> provinsiDataMap = new HashMap<>();
                        provinsiDataMap.put("Provinsi",provinsi);
                        provinsiDataMap.put("Kasus_Posi",cases);
                        provinsiDataMap.put("Kasus_Meni",deaths);
                        provinsiDataMap.put("Kasus_Semb",recovered);

                        provinsiArrayList.add(provinsiDataMap);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

                loadProvinsi.setVisibility(View.GONE);
                adapterProvinsi = new AdapterProvinsi(CountryDetailActivity.this,provinsiArrayList);
                provinsiListView.setHasFixedSize(true);
                provinsiListView.setLayoutManager(new LinearLayoutManager(CountryDetailActivity.this));
                provinsiListView.setAdapter(adapterProvinsi);

                Log.d("ProvinsiList", provinsiArrayList.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadProvinsi.setVisibility(View.GONE);
                Log.d("tag", String.valueOf(error));
                Toast.makeText(CountryDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(mStringRequest);
    }
}
