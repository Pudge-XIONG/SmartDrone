package google.hashcode;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by xq on 18/02/2017.
 */
public class Drone {
    Location currentLocation = null;
    private Map<Integer, Integer> productMap = new HashMap<>();

    public Map<Integer, Integer> getProductMap() {
        return productMap;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
