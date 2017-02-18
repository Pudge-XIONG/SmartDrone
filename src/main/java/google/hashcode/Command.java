package google.hashcode;

/**
 * Created by xq on 18/02/2017.
 */
public class Command {
    int drone;
    char type;
    int[] details = new int[3];

    public void printCommand() {
        System.out.print(drone);
        System.out.print(' ');
        System.out.print(type);
        System.out.print(' ');
        System.out.print(details[0]);
        if (type!='W') {
            System.out.print(' ');
            System.out.print(details[1]);
            System.out.print(' ');
            System.out.print(details[2]);
        }
        System.out.println();
    }

    public int getDrone () {return drone; }
    public void setDrone (int d) { drone=d; }

    public char getType () {return type; }
    public void setType (char t) { type = t; }

    public int[] getDetails () {return details; }
    public void setDetails (int[] d) {
        int s;
        s=d.length <3 ? 1 : 3;
        for (int i=0; i<s; i++) { details[i] = d[i];}
    }
}
