package google.hashcode;


/**
 * Created by xq on 18/02/2017.
 */
public class Order {
    private Location location = null;
    private int[] products;
    private boolean stat = false;

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

    public boolean isStat() {
        return stat;
    }

    public void setStat(boolean stat) {
        this.stat = stat;
    }
}
