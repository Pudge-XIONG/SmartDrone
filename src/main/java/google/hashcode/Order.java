package google.hashcode;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by xq on 18/02/2017.
 */
public class Order {
    private Location location = null;
    private boolean stat = false;
    private Map<Integer, Integer> productMap = new HashMap<>();

    public Map<Integer, Integer> getProductMap() {
        return productMap;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isStat() {
        return stat;
    }

    public void setStat(boolean stat) {
        this.stat = stat;
    }
}
