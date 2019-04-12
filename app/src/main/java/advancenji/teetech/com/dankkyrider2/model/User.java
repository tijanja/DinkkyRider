package advancenji.teetech.com.dankkyrider2.model;

import com.google.firebase.database.PropertyName;

/**
 * Created by pkharche on 16/04/18.
 */
public class User {

    Coordinate coordinate;

    boolean isOnRide,terms;
    String rideId;
    String id;
    String name;
    String phone;
    String city;
    String email;
    String type;
    Float  rating;

    String imageUrl;

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @PropertyName("is_on_ride")
    public boolean isOnRide() {
        return isOnRide;
    }

    @PropertyName("is_on_ride")
    public void setOnRide(boolean onRide) {
        isOnRide = onRide;
    }

    @PropertyName("ride_id")
    public String getRideId() {
        return rideId;
    }

    @PropertyName("ride_id")
    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    @PropertyName("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @PropertyName("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isTerms() {
        return terms;
    }

    public void setTerms(boolean terms) {
        this.terms = terms;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
