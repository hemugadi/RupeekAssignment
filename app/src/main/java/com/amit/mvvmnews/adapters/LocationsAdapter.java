package com.amit.mvvmnews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amit.mvvmnews.R;
import com.amit.mvvmnews.model.Location;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationsViewHolder> {

    Context context;
    ArrayList<Location> locations;
    OnItemClickListener listener;

    public LocationsAdapter(Context context, ArrayList<Location> locations) {
        this.context = context;
        this.locations = locations;
        this.listener = (OnItemClickListener)context;
    }

    @NonNull
    @Override
    public LocationsAdapter.LocationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.locations_item, parent, false);
        return new  LocationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationsAdapter.LocationsViewHolder holder, int position) {
        holder.tvName.setText(locations.get(position).getPlace());
        holder.tvDate.setText(locations.get(position).getDate());
        Picasso.get().load(locations.get(position).getUrl()).into(holder.ivLocations);
        holder.ivLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class LocationsViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        TextView tvDate;
        ImageView ivLocations;

        public LocationsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name);
            tvDate = itemView.findViewById(R.id.date);
            ivLocations = itemView.findViewById(R.id.image);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}