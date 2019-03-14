package it.univaq.mobileprogramming.uniweather.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import it.univaq.mobileprogramming.uniweather.R;
import it.univaq.mobileprogramming.uniweather.model.WeatherResult;

public class Adapter extends BaseAdapter {

    private WeatherResult[] data;

    public Adapter(WeatherResult[] data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public WeatherResult getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter, parent, false);
            holder = new ViewHolder();

            holder.title = convertView.findViewById(R.id.title);
            holder.subtitle = convertView.findViewById(R.id.subtitle);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        WeatherResult city = getItem(position);
        holder.title.setText(city.getName());
        holder.subtitle.setText(city.getSys().getCountry());

        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView subtitle;
    }
}

