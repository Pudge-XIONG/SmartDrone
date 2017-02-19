package google.hashcode;


/**
 * Created by xq on 18/02/2017.
 */
public class Drone {
    Location currentLocation = null;
    private int[] products;

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public int[] getProducts() {
        return products;
    }

    public void setProducts(int[] products) {
        this.products = products;
    }
}
