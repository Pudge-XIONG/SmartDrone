package google.hashcode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * A Camel Application
 */
public class DroneApp {

    public static int MAX_ROW;
    public static int MAX_COLUMN;

    public static int DRONE_ACCOUNT;
    public static int MAX_TURN;
    public static int MAX_LOAD;
    public static int WAREHOUSE_ACCOUNT;
    public static int ORDER_ACCOUNT;

    private static final int PRODUCT_DES_LINES = 2;
    private static final int ORDER_DES_LINES = 3;

    private static final String SEPERATOR = " ";
    public static ProductType[] productTypes;
    public static Map<Integer,Integer> sortedProductTypeMap = new HashMap<>();
    public static List<Order> orderList = new ArrayList<>();
    public static List<Warehouse> warehouseList = new ArrayList<>();
    public static List<Drone> droneList = new ArrayList<>();
    public static List<Command> commandList = new ArrayList<>();

    private static int centreWarehouseId;

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        loadFile("redundancy.in");

        centreWarehouseId = getCentreWarehouseId();
        // now excute commands
        int turn = 0;
        while(turn < MAX_TURN){


            turn ++;
        }

    }

    public static List<Command> generateCommand(){
        List<Command> newCommandList = new ArrayList<>();
        List<Drone> availableDroneList = getAvailableDrones();
        List<Order> remainOrderList = getRemainOrders();
        Map<Integer, Integer> allRemainProductToDeliver = getAllRemainProductToDeliver(remainOrderList, warehouseList.get(centreWarehouseId).getProductMap());

        Map<Integer, Integer> sortedAllRemainProductToDeliver = sortProductMapByWeight(allRemainProductToDeliver);

        for(int productType : sortedAllRemainProductToDeliver.keySet()){
            int weight = productTypes[productType].getWeight();
            int account = sortedAllRemainProductToDeliver.get(productType);
            Warehouse nearestWarehouse = getNearestWarehouseContainsProduct(productType);
            Drone drone = getNearestAvailableDroneFromWarehouse(availableDroneList, nearestWarehouse);

            int availablePayload = drone.getAvailablePayload();
            int canCarryProductAccount = availablePayload/weight;
            canCarryProductAccount = canCarryProductAccount > account? account:canCarryProductAccount;
            int commandWarehouseId;
            int loadAccount;
            int productAccountAtWarehouse;
            if(drone.isAtWarehouse){
                Warehouse wh = warehouseList.get(drone.getCurrentLocationWarehouseOrOrderId());
                Map<Integer, Integer> currentProductMap = wh.getProductMap();


                if(currentProductMap.containsKey(productType)){
                    commandWarehouseId = wh.getId();
                    productAccountAtWarehouse = wh.getProductMap().get(productType);
                } else{
                    commandWarehouseId = nearestWarehouse.getId();
                    productAccountAtWarehouse = nearestWarehouse.getProductMap().get(productType);
                }

            } else{
                commandWarehouseId = nearestWarehouse.getId();
                productAccountAtWarehouse = nearestWarehouse.getProductMap().get(productType);
            }
            loadAccount = canCarryProductAccount < commandWarehouseId?canCarryProductAccount : productAccountAtWarehouse;
            Command command = Command.createCommand(drone.getId(), Command.LOAD_COMMAND, commandWarehouseId, productType, loadAccount);
            commandList.add(command);
            command.executeOneTurn();
        }



        return newCommandList;
    }



    public static Warehouse getNearestWarehouseContainsProduct(int productType){
        Warehouse nearestWarehouse = null;
        int distance = -1;
        for(Warehouse wh : warehouseList){
            if(wh.getProductMap().containsKey(productType)){
                int currentDistance = wh.getLocation().getDistance(warehouseList.get(centreWarehouseId).getLocation());
                if(distance == -1 || distance > currentDistance){
                    distance = currentDistance;
                    nearestWarehouse = wh;
                }
            }
        }

        return nearestWarehouse;
    }


    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o1.getValue()).compareTo( o2.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }



    public static Map<Integer, Integer> sortProductMapByWeight(Map<Integer, Integer> remainProductMap){
        Map<Integer, Integer> sortedRemainProductMap = new HashMap<>();
        for(int productType : sortedProductTypeMap.keySet()){
            if(remainProductMap.containsKey(productType)){
                sortedRemainProductMap.put(productType, remainProductMap.get(productType));
            }
        }
        return sortedRemainProductMap;
    }


    public static Map<Integer, Integer> getAllRemainProductToDeliver(List<Order> remainOrderList, Map<Integer, Integer> readyProductMap){
        Map<Integer, Integer> allremainProductToDeliver = new HashMap<>();
        for(Order order : remainOrderList){
            Map<Integer, Integer> orderProductMap = order.getProductMap();
            for(int productType : orderProductMap.keySet()){
                if(allremainProductToDeliver.containsKey(productType)){
                    int currentAccount = allremainProductToDeliver.get(productType);
                    allremainProductToDeliver.put(productType, currentAccount + orderProductMap.get(productType));
                } else{
                    allremainProductToDeliver.put(productType, orderProductMap.get(productType));
                }
            }
        }

        for(int productType : readyProductMap.keySet()){
            int readyAccount = readyProductMap.get(productType);
            if(allremainProductToDeliver.containsKey(productType)){
                int account = allremainProductToDeliver.get(productType);
                if(account > readyAccount){
                    allremainProductToDeliver.put(productType, account - readyAccount);
                } else{
                    allremainProductToDeliver.remove(productType);
                }
            }
        }

        return allremainProductToDeliver;
    }


    public static Drone getNearestAvailableDroneFromWarehouse(List<Drone> availableDroneList, Warehouse wh){
        int distance = -1;
        Drone drone = null;
        for(Drone currentDrone: availableDroneList){
            int currentDistance = currentDrone.getCurrentLocation().getDistance(wh.getLocation());
            if(distance == -1 || distance > currentDistance){
                distance = currentDistance;
                drone = currentDrone;
            }
        }
        return drone;
    }

    /*
    public Warehouse getNearestWarehouseForOrder(Order order){
        Warehouse nearestWarehouse = null;
        Location orderLocation = order.getLocation();
        Map<Integer, Integer> orderProductMap = order.getProductMap();
        int distance = -1;
        for(Warehouse wh : warehouseList){
            Location whLocation = wh.getLocation();
            Map<Integer, Integer> whProductMap = wh.getProductMap();
            boolean contains = false;
            for(int productTyp : orderProductMap.keySet()){
                if(whProductMap.get(productTyp) >= orderProductMap.get(productTyp)){
                    contains = true;
                    break;
                }
            }
            if()

            if(contains){
                int currentDistance = whLocation.getDistance(orderLocation);
                if(distance == -1 || distance > currentDistance){
                    distance = currentDistance;
                    nearestWarehouse = wh;
                }
            }
        }

        return nearestWarehouse;
    }
    */

    public static List<Order> getRemainOrders(){
        List<Order> remainOrderList = new ArrayList<>();

        for(Order order : orderList){
            if(order.getProductMap().size() >= 1){
                remainOrderList.add(order);
            }
        }

        return remainOrderList;
    }


    public static List<Drone> getAvailableDrones(){
        List<Drone> availableDroneList = new ArrayList<>();
        for(Drone drone : droneList){
            if(drone.isAvailable()){
                availableDroneList.add(drone);
            }
        }
        return availableDroneList;
    }


    private static int getCentreWarehouseId(){
        int warehouseId = -1;
        int shortestDistance = -1;
        for(int id = 0; id < WAREHOUSE_ACCOUNT; id++){
            Warehouse wh = warehouseList.get(id);
            int totalDistance = 0;
            for(Order order : orderList){
                int distance = wh.getLocation().getDistance(order.getLocation());
                totalDistance += distance;
            }
            if(shortestDistance == -1 || (shortestDistance > totalDistance)){
                shortestDistance = totalDistance;
                warehouseId = id;
            }
        }
        return warehouseId;
    }


    private static Map<Integer, Integer> getAllOrderProducts(){
        Map<Integer, Integer> allOrderProductMap = new HashMap<>();
        for(Order order : orderList){
            Map<Integer, Integer> orderProductMap = order.getProductMap();
            for(int productType : orderProductMap.keySet()){
                int productAccount = orderProductMap.get(productType);
                if(allOrderProductMap.containsKey(productType)){
                    int account = allOrderProductMap.get(productType);
                    allOrderProductMap.put(productType, account + productAccount);
                } else{
                    allOrderProductMap.put(productType, productAccount);
                }
            }
        }

        return allOrderProductMap;
    }

    private static void loadFile(String filePath){
        String line;
        int warehouseId = 0;
        try {
            InputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.US_ASCII);
            BufferedReader br = new BufferedReader(isr);

            int line_num = 0;
            Location warehouseLocation = null;
            int itemsAccount = 0;
            Location destination = null;

            while ((line = br.readLine()) != null) {
                // Deal with the line
                //System.out.println(line);
                String[] values = line.split(SEPERATOR);
                if(line_num == 0){
                    // first line
                    MAX_ROW = Integer.parseInt(values[0]);
                    MAX_COLUMN = Integer.parseInt(values[1]);
                    DRONE_ACCOUNT = Integer.parseInt(values[2]);
                    MAX_TURN = Integer.parseInt(values[3]);
                    MAX_LOAD = Integer.parseInt(values[4]);
                } else if(line_num == 1) {
                    int productTypeAccount = Integer.parseInt(values[0]);
                    productTypes = new ProductType[productTypeAccount];
                } else if(line_num == 2){
                    for(int i = 0; i < productTypes.length; i ++){
                        productTypes[i] = new ProductType(i);
                        productTypes[i].setType(i);
                        productTypes[i].setWeight(Integer.parseInt(values[i]));
                    }

                } else if(line_num ==3 ){
                    WAREHOUSE_ACCOUNT = Integer.parseInt(values[0]);
                } else if(line_num > 3  && line_num <= 3 + WAREHOUSE_ACCOUNT * PRODUCT_DES_LINES ){

                    if((line_num - 4)%PRODUCT_DES_LINES == 0){
                        // location
                        warehouseLocation = new Location(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
                    } else{
                        Warehouse wh = new Warehouse();
                        wh.setLocation(warehouseLocation);
                        for(int i = 0; i < productTypes.length; i ++){
                            wh.getProductMap().put(i, Integer.parseInt(values[i]));
                        }
                        wh.setId(warehouseId);
                        warehouseId ++;
                        warehouseList.add(wh);
                    }
                } else if(line_num == 3 + WAREHOUSE_ACCOUNT * PRODUCT_DES_LINES + 1){
                    ORDER_ACCOUNT = Integer.parseInt(values[0]);
                } else if( line_num > 3 + WAREHOUSE_ACCOUNT * PRODUCT_DES_LINES + 1
                        && line_num <= 3 + WAREHOUSE_ACCOUNT * PRODUCT_DES_LINES + 1 + ORDER_ACCOUNT * ORDER_DES_LINES){

                    int index = line_num - (3 + WAREHOUSE_ACCOUNT * PRODUCT_DES_LINES + 1) - 1;
                    if(index%ORDER_DES_LINES == 0){
                        // destinatoin
                        destination = new Location(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
                    } else if(index%ORDER_DES_LINES == 1){
                        itemsAccount = Integer.parseInt(values[0]);
                    } else if(index%ORDER_DES_LINES == 2){
                        Order order = new Order();
                        order.setLocation(destination);
                        for(int i = 0; i < itemsAccount; i++){
                            int productType = Integer.parseInt(values[i]);
                            int value = 0;
                            if(order.getProductMap().containsKey(productType)){
                                value = order.getProductMap().get(productType);
                            }
                            order.getProductMap().put(productType, value + 1);
                        }
                        orderList.add(order);
                    }

                }
                line_num ++;
            }

            Location initLocation = warehouseList.get(0).getLocation();
            Location location = new Location(initLocation.getRow(), initLocation.getColumn());
            for(int i = 0; i < DRONE_ACCOUNT; i++){
                Drone drone = new Drone();
                drone.setId(i);
                droneList.add(drone);
            }

            Map<Integer, Integer> productTypeMap = new HashMap<>();
            for(ProductType pt : productTypes){
                productTypeMap.put(pt.getType(), pt.getWeight());
            }
            sortedProductTypeMap = sortByValue(productTypeMap);

        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Error while read line : " + e.getMessage());
        }
    }

}

