package it.univaq.mobileprogramming.uniweather.activity.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import it.univaq.mobileprogramming.uniweather.R;
//import it.univaq.mobileprogramming.uniweather.activity.DetailsActivity;
//import it.univaq.mobileprogramming.uniweather.activity.MapsActivity;
import it.univaq.mobileprogramming.uniweather.model.WeatherResult;

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.ViewHolder> {

    private List<WeatherResult> data;

    public AdapterRecycler(List<WeatherResult> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        WeatherResult city = data.get(i);
        viewHolder.title.setText(city.getName());
        viewHolder.subtitle.setText(city.getSys().getCountry());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    // Use ViewHolder Pattern
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView subtitle;

        ViewHolder(@NonNull View view) {
            super(view);

            title = view.findViewById(R.id.title);
            subtitle = view.findViewById(R.id.subtitle);
            /*
            // Define the click event on item
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // Open another Activity and pass to it the right city
                    WeatherResult city = data.get(getAdapterPosition());
                    Intent intent = new Intent(v.getContext(), MapsActivity.class);
                    intent.putExtra("cityName", city.getName());
                    intent.putExtra("regionName", city.getRegion());
                    intent.putExtra("latitude", city.getLatitude());
                    intent.putExtra("longitude", city.getLongitude());
                    v.getContext().startActivity(intent);
                }

            });
            */
        }
    }
}
