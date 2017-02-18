package google.hashcode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xq on 18/02/2017.
 */
public class Warehouse {
    private Location location = null;

    private int[] products = new int[10000];

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int[] getProducts() {
        return products;
    }

    public void setProducts(int[] products) {
        this.products = products;
    }
}
