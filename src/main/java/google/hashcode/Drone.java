package google.hashcode;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by xq on 18/02/2017.
 */
public class Drone {
    Location currentLocation = null;
    private Map<Integer, Integer> productMap = new HashMap<>();
    int currentTurn;

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public Map<Integer, Integer> getProductMap() {
        return productMap;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public int availablePayload(){
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
