package google.hashcode;

/**
 * Created by xq on 18/02/2017.
 */
public class Command {
    public static final char LOAD_COMMAND = 'L';
    public static final char UNLOAD_COMMAND = 'U';
    public static final char DELIVER_COMMAND = 'D';
    public static final char WAIT_COMMAND = 'W';

    private int droneId;
    private char commandType;
    private int orderId;
    private int warehouseId;
    private int productType;
    private int productAccount;
    private int waitTurns;
    private int currentWaitedTurns = 0;
    private boolean commandFinished = false;
    private int distance;

    private int turnLeftBeforeFinish = 0;

    public static Command createWaitCommand(int droneId, int waitTurns){
        Command command = new Command();
        command.setDroneId(droneId);
        command.setWaitTurns(waitTurns);
        command.setCommandType(WAIT_COMMAND);
        command.setTurnLeftBeforeFinish(waitTurns);

        return command;
    }


    public static Command createCommand(int droneId, char commandType, int warehouseOrOrderId, int productType, int productAccount){
        Command command = new Command();
        command.setCommandType(commandType);
        Location location = null;
        if(commandType == LOAD_COMMAND || commandType == UNLOAD_COMMAND){
            command.setWarehouseId(warehouseOrOrderId);
            location = DroneApp.warehouseList.get(warehouseOrOrderId).getLocation();
            if(commandType == LOAD_COMMAND){
                DroneApp.droneList.get(droneId).load(productType, productAccount);
                DroneApp.warehouseList.get(warehouseOrOrderId).loadToDrone(productType, productAccount);
            }
        } else{
            command.setOrderId(warehouseOrOrderId);
            location = DroneApp.orderList.get(warehouseOrOrderId).getLocation();
            DroneApp.orderList.get(warehouseOrOrderId).delivered(productType, productAccount);
        }
        command.setProductType(productType);
        command.setProductAccount(productAccount);
        int distance = DroneApp.droneList.get(droneId).getCurrentLocation().getDistance(location);
        command.setDistance(distance);
        command.setTurnLeftBeforeFinish(distance + 1);

        return command;
    }


    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTurnLeftBeforeFinish() {
        return turnLeftBeforeFinish;
    }

    public void setTurnLeftBeforeFinish(int turnLeftBeforeFinish) {
        this.turnLeftBeforeFinish = turnLeftBeforeFinish;
    }

    public void executeOneTurn(){
        turnLeftBeforeFinish --;
        Drone drone = DroneApp.droneList.get(droneId);
        drone.setAvailable(false);
        drone.setComsumedTurn(drone.getComsumedTurn() - 1);
        if(commandFinished != true){
            if(commandType == WAIT_COMMAND) {
                if(currentWaitedTurns < waitTurns){
                    currentWaitedTurns ++;
                    if(currentWaitedTurns >= waitTurns){
                        commandFinished = true;
                        drone.setAvailable(true);
                    }
                }
            } else if(commandType == UNLOAD_COMMAND){
                if(distance > 1){
                    drone.setCurrentLocationWarehouseOrOrderId(-1);
                    distance --;
                } else {
                    drone.setAtWarehouse(true);
                    drone.setCurrentLocationWarehouseOrOrderId(warehouseId);
                    drone.unload(productType, productAccount);
                    DroneApp.warehouseList.get(warehouseId).unloadFromDrone(productType, productAccount);
                    commandFinished = true;
                    drone.setAvailable(true);
                }
            } else if(commandType == LOAD_COMMAND){
                if(distance > 1){
                    drone.setCurrentLocationWarehouseOrOrderId(-1);
                    distance --;
                } else{
                    drone.setAtWarehouse(true);
                    drone.setCurrentLocationWarehouseOrOrderId(warehouseId);
                    //drone.load(productType, productAccount);
                    //DroneApp.warehouseList.get(warehouseId).loadToDrone(productType, productAccount);
                    commandFinished = true;
                    drone.setAvailable(true);
                }
            } else if(commandType == DELIVER_COMMAND){
                if(distance > 1){
                    drone.setCurrentLocationWarehouseOrOrderId(-1);
                    distance --;
                } else{
                    drone.setAtWarehouse(false);
                    drone.setCurrentLocationWarehouseOrOrderId(orderId);
                    drone.deliver(productType, productAccount);
                    commandFinished = true;
                    drone.setAvailable(true);
                }
            }

        }
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public int getCurrentWaitedTurns() {
        return currentWaitedTurns;
    }

    public void setCurrentWaitedTurns(int currentWaitedTurns) {
        this.currentWaitedTurns = currentWaitedTurns;
    }

    public boolean isCommandFinished() {
        return commandFinished;
    }

    public void setCommandFinished(boolean commandFinished) {
        this.commandFinished = commandFinished;
    }

    public int getWaitTurns() {
        return waitTurns;
    }

    public void setWaitTurns(int waitTurns) {
        this.waitTurns = waitTurns;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public int getProductAccount() {
        return productAccount;
    }

    public void setProductAccount(int productAccount) {
        this.productAccount = productAccount;
    }

    public void printCommand() {
        System.out.print(droneId);
        System.out.print(' ');
        System.out.print(commandType);
        System.out.print(' ');
        if (commandType == WAIT_COMMAND) {
            System.out.print(waitTurns);
        } else{
            System.out.print(orderId);
            System.out.print(' ');
            System.out.print(productType);
            System.out.print(' ');
            System.out.print(productAccount);
        }
        System.out.println();
    }

    public int getDroneId() {return droneId; }
    public void setDroneId(int d) { droneId =d; }

    public char getCommandType() {return commandType; }
    public void setCommandType(char t) { commandType = t; }

}
