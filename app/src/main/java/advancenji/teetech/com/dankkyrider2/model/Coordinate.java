package advancenji.teetech.com.dankkyrider2.model;

import android.location.Location;

import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pkharche on 16/04/18.
 */
public class Coordinate {
    @PropertyName("landMark")
    String landMark;

    double latitude;

    double longitude;


    public Coordinate()
    {

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @PropertyName("landMark")
    public String getLandMark() {
        return landMark;
    }

    @PropertyName("landMark")
    public void setLandMark(String land) {
        landMark = land;
    }
}
