package google.hashcode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xq on 18/02/2017.
 */
public class Warehouse {
    private int id;
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


    public void unloadFromDrone(int productType, int productAccount){
        if (productMap.containsKey(productType)) {
            int warehouseHasProductAccount = productMap.get(productType);
            productMap.put(productType, warehouseHasProductAccount + productAccount);
        } else {
            productMap.put(productType, productAccount);
        }
    }

    public void loadToDrone(int productType, int productAccount){
        int warehouseHasProductAccount = productMap.get(productType);
        int warehouseProductAccountAfterLoad = warehouseHasProductAccount - productAccount;
        if(warehouseProductAccountAfterLoad > 0){
            productMap.put(productType, warehouseProductAccountAfterLoad);
        } else{
            productMap.remove(productType);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductAccountAfterNTurn(int productType, int turn){
        int productAccountAfterNTurn = 0;
        for(Command command : DroneApp.commandList){
            if(!command.isCommandFinished()
                    && (command.getCommandType()==Command.LOAD_COMMAND || command.getCommandType()==Command.UNLOAD_COMMAND)
                    && command.getWarehouseId() == this.id && command.getProductType() == productType){
                if(command.getTurnLeftBeforeFinish() <= turn){
                    if(command.getCommandType()==Command.LOAD_COMMAND){
                        productAccountAfterNTurn = productMap.get(productType) - command.getProductAccount();
                    } else{
                        productAccountAfterNTurn = productMap.get(productType) + command.getProductAccount();
                    }
                }
            }
        }

        return productAccountAfterNTurn;
    }

}
