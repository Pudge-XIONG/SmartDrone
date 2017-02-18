package google.hashcode;

/**
 * Created by xq on 18/02/2017.
 */
public class ProductType {
    String type = null;
    int weight = 0;

    public ProductType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
