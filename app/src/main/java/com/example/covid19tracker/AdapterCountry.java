package com.example.covid19tracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class AdapterCountry extends RecyclerView.Adapter<AdapterCountry.ItemViewHolder> {

    private ArrayList<HashMap<String, String>> data;
    private Context context;

    @NonNull
    @Override
    public AdapterCountry.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_country, viewGroup, false);
        return new ItemViewHolder(view);
    }

    public AdapterCountry(Context context, ArrayList<HashMap<String, String>> list) {
        this.data = list;
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterCountry.ItemViewHolder holder, int position) {

//        String flag = data.get(position).get("FLAG");
//
//        holder.flag.setImageBitmap(getBitmapFromURL(flag));
        Glide.with(context)
                .load(data.get(position).get("FLAG"))
                .apply(new RequestOptions().override(240, 160))
                .into(holder.flag);
        final String countryName = data.get(position).get("COUNTRY");
        holder.country.setText(countryName);
        holder.recovered.setText(data.get(position).get("RECOVERED"));
        holder.recoveredTv.setText("Recovered");
        holder.critical.setText(data.get(position).get("CRITICAL"));
        holder.criticalTv.setText("Critical");
        holder.cases.setText(data.get(position).get("CASES"));
        holder.casesTv.setText("Cases");
        holder.active.setText(data.get(position).get("ACTIVE"));
        holder.activeTv.setText("Active");
        holder.death.setText(data.get(position).get("DEATHS"));
        holder.deathTv.setText("Death");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countryName.equals("Indonesia")){
                    Intent in = new Intent(context,CountryDetailActivity.class);
                    context.startActivity(in);
                }else{
                    Toast.makeText(context, "Currently Country Detail Menu Only Works for Indonesia", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView country;
        ImageView flag;
        TextView cases,casesTv;
        TextView active,activeTv;
        TextView death,deathTv;
        TextView critical,criticalTv;
        TextView recovered,recoveredTv;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            flag = itemView.findViewById(R.id.img);
            country = itemView.findViewById(R.id.countryName);
            cases = itemView.findViewById(R.id.countryCases);
            casesTv = itemView.findViewById(R.id.countryCasesTv);
            active = itemView.findViewById(R.id.countryActive);
            activeTv = itemView.findViewById(R.id.countryActiveTv);
            death = itemView.findViewById(R.id.countryDeaths);
            deathTv = itemView.findViewById(R.id.countryDeathsTv);
            critical = itemView.findViewById(R.id.countryCritical);
            criticalTv = itemView.findViewById(R.id.countryCriticalTv);
            recovered = itemView.findViewById(R.id.countryRecovered);
            recoveredTv = itemView.findViewById(R.id.countryRecoveredTv);
        }
    }
}

