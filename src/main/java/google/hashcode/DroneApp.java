package google.hashcode;

import org.apache.camel.main.Main;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * A Camel Application
 */
public class DroneApp {

    private static int MAX_ROW;
    private static int MAX_COLUMN;

    private static int DRONE_ACCOUNT;
    private static int MAX_TURN;
    private static int MAX_LOAD;
    private static int PRODUCT_TYPE_ACCOUNT;
    private static int WAREHOUSE_ACCOUNT;
    private static int ORDER_ACCOUNT;

    private static final int PRODUCT_DES_LINES = 2;
    private static final int ORDER_DES_LINES = 3;

    private static final String SEPERATOR = " ";
    private static Map<String, ProductType> ProductTypeMap = new HashMap<>();

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {

    }


    private void loadFile(String filePath){
        String line;
        try {
            InputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.US_ASCII);
            BufferedReader br = new BufferedReader(isr);
            int warehouseIndex = 0;

            int line_num = 0;
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
                    for(int i = 0; i < productTypeAccount; i++){
                        ProductType pt = new ProductType(i+"");
                        ProductTypeMap.put(i+"", pt);
                    }
                } else if(line_num == 2){
                    int begin = 0;
                    for(ProductType pt : ProductTypeMap.values()){
                        pt.setWeight(Integer.parseInt(values[begin]));
                        begin ++;
                    }

                } else if(line_num ==3 ){
                    WAREHOUSE_ACCOUNT = Integer.parseInt(values[0]);
                } else if(line_num > 3  && line_num <= 3 + WAREHOUSE_ACCOUNT * PRODUCT_DES_LINES ){
                    warehouseIndex = (line_num - 4)/PRODUCT_DES_LINES;
                    Warehouse wh = new Warehouse();
                    if((line_num - 4)%PRODUCT_DES_LINES == 0){
                        // location
                        Location location = new Location(Integer.parseInt(values[0], Integer.parseInt(values[1]);
                        wh.setLocation(location);
                    } else{
                        Map<String, Product> productMap = new HashMap<>();
                        for(int i = 0; i < PRODUCT_TYPE_ACCOUNT; i ++){
                            Product product = new Product();
                            product.setAccount(Integer.parseInt(values[i]));
                            ProductType pt = ProductTypeMap.get(i+"");
                            product.setType(pt);
                            productMap.put(pt.getType(), product);
                        }
                        wh.setProductsMap(productMap);
                    }
                } else if(line_num == 3 + WAREHOUSE_ACCOUNT * PRODUCT_DES_LINES + 1){
                    ORDER_ACCOUNT = Integer.parseInt(values[0]);
                } else if( line_num > 3 + WAREHOUSE_ACCOUNT * PRODUCT_DES_LINES + 1
                        && line_num <= 3 + WAREHOUSE_ACCOUNT * PRODUCT_DES_LINES + ORDER_ACCOUNT * ORDER_DES_LINES){

                }

                line_num ++;
            }
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Error while read line : " + e.getMessage());
        }
    }

}

