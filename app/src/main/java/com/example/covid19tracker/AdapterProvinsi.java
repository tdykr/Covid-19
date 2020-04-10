package com.example.covid19tracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class AdapterProvinsi extends RecyclerView.Adapter<AdapterProvinsi.ItemViewHolder> {

    private ArrayList<HashMap<String, String>> data;
    private Context context;

    @NonNull
    @Override
    public AdapterProvinsi.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_provinsi, viewGroup, false);
        return new ItemViewHolder(view);
    }

    public AdapterProvinsi(Context context, ArrayList<HashMap<String, String>> list) {
        this.data = list;
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterProvinsi.ItemViewHolder holder, int position) {

        holder.recovered.setText(data.get(position).get("Kasus_Semb"));
        holder.recoveredTv.setText("Recovered");
        holder.provinsi.setText(data.get(position).get("Provinsi"));
        holder.provinsiTv.setText("Provinsi");
        holder.cases.setText(data.get(position).get("Kasus_Posi"));
        holder.casesTv.setText("Cases");
        holder.death.setText(data.get(position).get("Kasus_Meni"));
        holder.deathTv.setText("Death");

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView cases,casesTv;
        TextView death,deathTv;
        TextView provinsi,provinsiTv;
        TextView recovered,recoveredTv;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            cases = itemView.findViewById(R.id.countryCases);
            casesTv = itemView.findViewById(R.id.countryCasesTv);
            death = itemView.findViewById(R.id.countryDeaths);
            deathTv = itemView.findViewById(R.id.countryDeathsTv);
            provinsi = itemView.findViewById(R.id.provinsi);
            provinsiTv = itemView.findViewById(R.id.provinsiTv);
            recovered = itemView.findViewById(R.id.countryRecovered);
            recoveredTv = itemView.findViewById(R.id.countryRecoveredTv);
        }
    }
}

