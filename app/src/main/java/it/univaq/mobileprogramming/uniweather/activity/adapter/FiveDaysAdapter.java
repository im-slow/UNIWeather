package it.univaq.mobileprogramming.uniweather.activity.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import it.univaq.mobileprogramming.uniweather.R;
import it.univaq.mobileprogramming.uniweather.activity.DetailsActivity;
import it.univaq.mobileprogramming.uniweather.model.Forecast;


public class FiveDaysAdapter extends RecyclerView.Adapter<FiveDaysAdapter.ViewHolder> {

    private List<Forecast> data;

    public FiveDaysAdapter(List<Forecast> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.five_days_adapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Forecast forecast = data.get(i);
        viewHolder.day.setText(forecast.getTimestamp());
        viewHolder.temp.setText(Double.toString((Math.round(forecast.getTemp() * 10) / 10.0)) + "°C");
        if(forecast.getIcon().equals("01d"))
            viewHolder.icon.setImageResource(R.drawable.i01d);
        else if(forecast.getIcon().equals("01n"))
            viewHolder.icon.setImageResource(R.drawable.i01n);
        else if(forecast.getIcon().equals("02d"))
            viewHolder.icon.setImageResource(R.drawable.i02d);
        else if(forecast.getIcon().equals("02n"))
            viewHolder.icon.setImageResource(R.drawable.i02n);
        else if(forecast.getIcon().equals("03d"))
            viewHolder.icon.setImageResource(R.drawable.i03d);
        else if(forecast.getIcon().equals("03n"))
            viewHolder.icon.setImageResource(R.drawable.i03n);
        else if(forecast.getIcon().equals("04n"))
            viewHolder.icon.setImageResource(R.drawable.i04n);
        else if(forecast.getIcon().equals("04d"))
            viewHolder.icon.setImageResource(R.drawable.i04d);
        else if(forecast.getIcon().equals("09d"))
            viewHolder.icon.setImageResource(R.drawable.i09d);
        else if(forecast.getIcon().equals("09n"))
            viewHolder.icon.setImageResource(R.drawable.i09n);
        else if(forecast.getIcon().equals("10d"))
            viewHolder.icon.setImageResource(R.drawable.i10d);
        else if(forecast.getIcon().equals("10n"))
            viewHolder.icon.setImageResource(R.drawable.i10n);
        else if(forecast.getIcon().equals("11d"))
            viewHolder.icon.setImageResource(R.drawable.i11d);
        else if(forecast.getIcon().equals("11n"))
            viewHolder.icon.setImageResource(R.drawable.i11n);
        else if(forecast.getIcon().equals("13d"))
            viewHolder.icon.setImageResource(R.drawable.i13d);
        else if(forecast.getIcon().equals("13n"))
            viewHolder.icon.setImageResource(R.drawable.i13n);
        else if(forecast.getIcon().equals("50d"))
            viewHolder.icon.setImageResource(R.drawable.i50d);
        else if(forecast.getIcon().equals("50n"))
            viewHolder.icon.setImageResource(R.drawable.i50n);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    // Use ViewHolder Pattern
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView day;
        TextView time;
        TextView temp;
        TextView desc;
        ImageView icon;

        ViewHolder(@NonNull View view) {
            super(view);

            day = view.findViewById(R.id.day);
            time = view.findViewById(R.id.time);
            temp = view.findViewById(R.id.temp);
            desc = view.findViewById(R.id.desc);
            icon = view.findViewById(R.id.icon);
        }
    }


}
