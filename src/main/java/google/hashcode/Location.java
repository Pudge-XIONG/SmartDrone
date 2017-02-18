package google.hashcode;

/**
 * Created by xq on 18/02/2017.
 */
public class Location {
    private int row = -1;
    private int column = -1;

    public Location(int row, int column){
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
