package google.hashcode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xq on 18/02/2017.
 */
public class Order {
    Location location = null;
    Map<String, Product> productsMap = new HashMap<>();

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Map<String, Product> getProductsMap() {
        return productsMap;
    }

    public void setProductsMap(Map<String, Product> productsMap) {
        this.productsMap = productsMap;
    }
}
