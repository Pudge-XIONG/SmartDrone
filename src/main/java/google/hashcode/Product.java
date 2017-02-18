package google.hashcode;


/**
 * Created by xq on 18/02/2017.
 */
public class Product {
    ProductType type = null;

    int account = -1;


    public String getType() {
        return type.getType();
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }
}
