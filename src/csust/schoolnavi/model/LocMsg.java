package csust.schoolnavi.model;

/**
 * Created by QPF on 2015/8/13.
 */
public class LocMsg {
    String name;
    double latitude,longitude;
    public LocMsg(String name, double latitude, double longitude){
        this.name=name;
        this.latitude=latitude;
        this.longitude=longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
