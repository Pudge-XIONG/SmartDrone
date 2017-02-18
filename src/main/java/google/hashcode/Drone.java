package google.hashcode;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xq on 18/02/2017.
 */
public class Drone {
    Location currentLocation = null;
    Map<String, Product> productsMap = new HashMap<>();

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void load(Map<String, Product> productsMap){

    }

    public void unload(Map<String, Product> productsMap){

    }
}
