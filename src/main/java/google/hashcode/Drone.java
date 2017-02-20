package google.hashcode;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by xq on 18/02/2017.
 */
public class Drone {
    private int id;
    private Map<Integer, Integer> productMap = new HashMap<>();
    int comsumedTurn = DroneApp.MAX_TURN;
    boolean isAvailable = true;
    boolean isAtWarehouse = true;
    int currentLocationWarehouseOrOrderId = 0;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAtWarehouse() {
        return isAtWarehouse;
    }

    public void setAtWarehouse(boolean atWarehouse) {
        isAtWarehouse = atWarehouse;
    }

    public int getCurrentLocationWarehouseOrOrderId() {
        return currentLocationWarehouseOrOrderId;
    }

    public void setCurrentLocationWarehouseOrOrderId(int currentLocationWarehouseOrOrderId) {
        this.currentLocationWarehouseOrOrderId = currentLocationWarehouseOrOrderId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getComsumedTurn() {
        return comsumedTurn;
    }

    public void setComsumedTurn(int comsumedTurn) {
        this.comsumedTurn = comsumedTurn;
    }

    public Map<Integer, Integer> getProductMap() {
        return productMap;
    }

    public Location getCurrentLocation() {
        if(currentLocationWarehouseOrOrderId == -1){
            return null;
        } else{
            if(isAtWarehouse){
                return DroneApp.warehouseList.get(currentLocationWarehouseOrOrderId).getLocation();
            } else{
                return DroneApp.orderList.get(currentLocationWarehouseOrOrderId).getLocation();
            }
        }
    }

    public int getAvailablePayload(){
        int availablePayload = DroneApp.MAX_LOAD;
        for(int key : productMap.keySet()){
            int payload = productMap.get(key) * DroneApp.productTypes[key].getWeight();
            availablePayload -= payload;
        }

        return availablePayload;
    }


    public void unload(int productType, int productAccount){
        if(productMap.containsKey(productType)){
            int droneHasProductAccount = productMap.get(productType);
            productMap.put(productType, droneHasProductAccount + productAccount);
        } else{
            productMap.put(productType, productAccount);
        }
    }


    public void deliver(int productType, int productAccount){
        unload(productType, productAccount);
    }


    public void load(int productType, int productAccount){
        int droneHasProductAccount = productMap.get(productType);
        int droneProductAccountAfterUnload = droneHasProductAccount - productAccount;
        if (droneProductAccountAfterUnload > 0) {
            productMap.put(productType, droneProductAccountAfterUnload);
        } else {
            productMap.remove(productType);
        }
    }
}
