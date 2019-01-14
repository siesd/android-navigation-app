package com.saskpolytech.cst142;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


/**
 * This class will be used to display a list of SavedLocation objects in a listView.
 * @author Dylan Sies (cst142)
 */
public class LocationAdapter extends ArrayAdapter<SavedLocation>
{

    //Constructor
    public LocationAdapter(Context context, List<SavedLocation> objects)
    {
        super(context, R.layout.location_detail, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //check if there is a view that we can reuse instead of making a new one
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.location_detail, parent, false);
        }

        //re-use a already inflated view if available
        View locationItemView = convertView;

        //Get all of the controls from the layout
        TextView tvLon = locationItemView.findViewById(R.id.tvLonDetail);
        TextView tvLat = locationItemView.findViewById(R.id.tvLatDetail);
        TextView tvName = locationItemView.findViewById(R.id.tvNameDetail);

        //get the SavedLocation object that pertains to the current listview item
        SavedLocation location = getItem(position);

        //set the textView text to the corresponding values in the person object
        tvLon.setText(location.sLongitude);
        tvLat.setText(location.sLatitude);
        tvName.setText(location.sName);

        return locationItemView;
    }


}
