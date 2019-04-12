package advancenji.teetech.com.dankkyrider2.model;

import android.location.Location;

import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pkharche on 16/04/18.
 */
public class Address {
    double lat,lng;

    public Address()
    {

    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
