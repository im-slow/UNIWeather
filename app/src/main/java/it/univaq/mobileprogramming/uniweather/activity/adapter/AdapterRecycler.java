package it.univaq.mobileprogramming.uniweather.activity.adapter;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;

import it.univaq.mobileprogramming.uniweather.R;
import it.univaq.mobileprogramming.uniweather.activity.DetailsActivity;
import it.univaq.mobileprogramming.uniweather.model.ActualWeather;


public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.ViewHolder> {

    private List<ActualWeather> data;

    public AdapterRecycler(List<ActualWeather> data){
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

        ActualWeather cityWeather = data.get(i);
        viewHolder.city.setText(cityWeather.getCity_name());
        viewHolder.temp.setText(Double.toString((Math.round(cityWeather.getTemp() * 10) / 10.0)) + "°C");
        if(cityWeather.getIcon_name().equals("01d"))
            viewHolder.icon.setImageResource(R.drawable.i01d);
        else if(cityWeather.getIcon_name().equals("01n"))
            viewHolder.icon.setImageResource(R.drawable.i01n);
        else if(cityWeather.getIcon_name().equals("02d"))
            viewHolder.icon.setImageResource(R.drawable.i02d);
        else if(cityWeather.getIcon_name().equals("02n"))
            viewHolder.icon.setImageResource(R.drawable.i02n);
        else if(cityWeather.getIcon_name().equals("03d"))
            viewHolder.icon.setImageResource(R.drawable.i03d);
        else if(cityWeather.getIcon_name().equals("03n"))
            viewHolder.icon.setImageResource(R.drawable.i03n);
        else if(cityWeather.getIcon_name().equals("04n"))
            viewHolder.icon.setImageResource(R.drawable.i04n);
        else if(cityWeather.getIcon_name().equals("04d"))
            viewHolder.icon.setImageResource(R.drawable.i04d);
        else if(cityWeather.getIcon_name().equals("09d"))
            viewHolder.icon.setImageResource(R.drawable.i09d);
        else if(cityWeather.getIcon_name().equals("09n"))
            viewHolder.icon.setImageResource(R.drawable.i09n);
        else if(cityWeather.getIcon_name().equals("10d"))
            viewHolder.icon.setImageResource(R.drawable.i10d);
        else if(cityWeather.getIcon_name().equals("10n"))
            viewHolder.icon.setImageResource(R.drawable.i10n);
        else if(cityWeather.getIcon_name().equals("11d"))
            viewHolder.icon.setImageResource(R.drawable.i11d);
        else if(cityWeather.getIcon_name().equals("11n"))
            viewHolder.icon.setImageResource(R.drawable.i11n);
        else if(cityWeather.getIcon_name().equals("13d"))
            viewHolder.icon.setImageResource(R.drawable.i13d);
        else if(cityWeather.getIcon_name().equals("13n"))
            viewHolder.icon.setImageResource(R.drawable.i13n);
        else if(cityWeather.getIcon_name().equals("50d"))
            viewHolder.icon.setImageResource(R.drawable.i50d);
        else if(cityWeather.getIcon_name().equals("50n"))
            viewHolder.icon.setImageResource(R.drawable.i50n);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    // Use ViewHolder Pattern
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView city;
        TextView temp;
        ImageView icon;

        ViewHolder(@NonNull View view) {
            super(view);

            city = view.findViewById(R.id.city);
            temp = view.findViewById(R.id.temp);
            icon = view.findViewById(R.id.icon);

            // Define the click event on item
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // Open another Activity and pass to it the right city
                    ActualWeather city = data.get(getAdapterPosition());
                    Intent intent = new Intent(v.getContext(), DetailsActivity.class);
                    intent.putExtra("ActualWeather", city);
                    v.getContext().startActivity(intent);
                }

            });

        }
    }


}
