package google.hashcode;

/**
 * Created by xq on 18/02/2017.
 */
public class ProductType {
    int type = 0;
    int weight = 0;

    public ProductType(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
