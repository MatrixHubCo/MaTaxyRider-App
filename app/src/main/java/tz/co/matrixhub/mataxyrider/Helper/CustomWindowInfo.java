package tz.co.matrixhub.mataxyrider.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import tz.co.matrixhub.mataxyrider.R;

/**
 * Created by lawrance on 30/11/2018.
 * © 2018
 */
public class CustomWindowInfo implements GoogleMap.InfoWindowAdapter {

    View myView;

    public CustomWindowInfo(Context context) {
        myView = LayoutInflater.from(context)
                .inflate(R.layout.custom_rider_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        TextView txtPickupTitle = ((TextView)myView.findViewById(R.id.txtPickupInfo));
        txtPickupTitle.setText(marker.getTitle());

        TextView txtPickupSnippet = ((TextView)myView.findViewById(R.id.txtPickupSnippet));
        txtPickupSnippet.setText(marker.getSnippet());

        return myView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
