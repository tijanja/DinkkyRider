package advancenji.teetech.com.dankkyrider2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private View view;
    public MapInfoWindowAdapter(Context cotxt)
    {
        context = cotxt;
        view = LayoutInflater.from(context).inflate(R.layout.map_custom_infowindow, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindow(marker,view);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindow(marker,view);
        return view;
    }

    public void renderWindow(Marker marker,View view)
    {
        TextView place = view.findViewById(R.id.place);

        place.setText(marker.getTitle());
    }
}
