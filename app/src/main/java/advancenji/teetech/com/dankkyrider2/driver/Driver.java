package advancenji.teetech.com.dankkyrider2.driver;

import com.google.firebase.database.PropertyName;

import advancenji.teetech.com.dankkyrider2.model.Coordinate;

public class Driver {

    String mDriverFname,mDriverId,lmPlaceId;
    double mLat,mLng,lmLat,lmLng;

    Coordinate coordinate;

    public Driver()
    {

    }

    public Driver(String id,String fName,double lat,double lng)
    {
        mDriverId = id;
        mDriverFname = fName;
        mLat=lat;
        mLng=lng;
    }

    public String getmDriverFname() {
        return mDriverFname;
    }

    public String getmDriverId() {
        return mDriverId;
    }

    public double getmLat() {
        return mLat;
    }

    public double getmLng() {
        return mLng;
    }

    @PropertyName("coordinate")
    public Coordinate getCoordinate() {
        return coordinate;
    }


    @PropertyName("coordinate")
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
