package google.hashcode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * A Camel Application
 */
public class DroneApp {

    private static int MAX_ROW;
    private static int MAX_COLUMN;

    private static int DRONE_ACCOUNT;
    private static int MAX_TURN;
    private static int MAX_LOAD;
    private static int WAREHOUSE_ACCOUNT;
    private static int ORDER_ACCOUNT;

    private static final int PRODUCT_DES_LINES = 2;
    private static final int ORDER_DES_LINES = 3;

    private static final String SEPERATOR = " ";
    private static ProductType[] productTypes;

    private static List<Order> orderList = new ArrayList<>();
    private static List<Warehouse> warehouseList = new ArrayList<>();
    private static List<Drone> droneList = new ArrayList<>();

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        loadFile("example.in");
    }


    private static void loadFile(String filePath){
        String line;
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
                        wh.setProducts(new int[productTypes.length]);
                        for(int i = 0; i < productTypes.length; i ++){
                            wh.getProducts()[i] = Integer.parseInt(values[i]);
                        }
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
                        order.setProducts(new int[productTypes.length]);
                        for(int i = 0; i < itemsAccount; i++){
                            order.getProducts()[Integer.parseInt(values[0])] ++;
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
                drone.setProducts(new int[productTypes.length]);
                drone.setCurrentLocation(location);
                droneList.add(drone);
            }
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Error while read line : " + e.getMessage());
        }
    }

}

