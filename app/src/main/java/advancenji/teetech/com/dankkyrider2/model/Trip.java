package advancenji.teetech.com.dankkyrider2.model;

import com.google.firebase.database.PropertyName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pkharche on 10/04/18.
 */
public class Trip {

    String id,driverToUserPoints;

    Address drop;
    Address pickup;

    StatusEnum status;

    User user;
    Driver driver;

    List<LandMark> landMarks = new ArrayList<>();

    public Trip()
    {

    }


    public String getDriverToUserPoints() {
        return driverToUserPoints;
    }

    public void setDriverToUserPoints(String driverToUserPoints) {
        this.driverToUserPoints = driverToUserPoints;
    }

    public List<LandMark> getLandMarks() {
        return landMarks;
    }

    @PropertyName("landMarks")
    public void setLandMarks(LandMark landMark) {
        this.landMarks.add(landMark);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("drop")
    public Address getDrop() {
        return drop;
    }

    @PropertyName("drop")
    public void setDrop(Address drop) {
        this.drop = drop;
    }

    @PropertyName("pickup")
    public Address getPickup() {
        return pickup;
    }

    @PropertyName("pickup")
    public void setPickup(Address pickup) {
        this.pickup = pickup;
    }


    @PropertyName("status")
    public StatusEnum getStatus() {
        return status;
    }

    @PropertyName("status")
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    @PropertyName("user_info")
    public User getUser() {
        return user;
    }

    @PropertyName("user_info")
    public void setUser(User user) {
        this.user = user;
    }

    @PropertyName("driver")
    public Driver getDriver() {
        return driver;
    }

    @PropertyName("driver")
    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
