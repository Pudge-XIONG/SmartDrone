package google.hashcode;

import java.util.HashMap;
import java.util.Map;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by xq on 18/02/2017.
 */
public class Warehouse {
    private Location location = null;

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



    public boolean unload(Order order) {
        for (int i : order.getProductMap().keySet()) {
            if (this.productMap.keySet().contains(i)) {
                if (order.getProductMap().get(i) > this.getProductMap().get(i) )
                {
                    return false;
                }
            }
            else return false;
        }

        for (int i : order.getProductMap().keySet()) {
            this.productMap.put(i, this.productMap.get(i) - order.getProductMap().get(i));
        }



        return true;
    }
}
