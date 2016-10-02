package game.construction;
import game.ships.CommonShip;
import javafx.scene.shape.Rectangle;

/**
 * Created by BobrZlosyn on 26.09.2016.
 */
public class Placement {

    private boolean isEmpty;
    private double x, y, size;
    private Rectangle field;
    private IShipEquipment equipment;
    private CommonShip ship;
    private boolean isWeapon;
    private int row, column;

    public Placement (double x, double y, double size, CommonShip ship, int row, int column) {
        setX(x);
        setY(y);
        setSize(size);
        setIsEmpty(true);
        setIsWeapon(false);
        this.ship = ship;
        this.row = row;
        this.column = column;
    }

    public void setIsWeapon(boolean isWeapon) {
        this.isWeapon = isWeapon;
    }

    public void setField(Rectangle field) {
        this.field = field;
    }

    public void setIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setShipEquipment(IShipEquipment equipment) {
        this.equipment = equipment;
    }

    public double getY() {
        return y;
    }

    public double getSize() {
        return size;
    }

    public double getX() {
        return x;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public boolean isWeapon() {
        return isWeapon;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public Rectangle getField() {
        return field;
    }

    public IShipEquipment getShipEquipment() {
        return equipment;
    }

    public CommonShip getShip() {
        return ship;
    }

    public void resize(double x, double y){
        field.setX(x);
        field.setY(y);
        setX(x);
        setY(y);
    }
}
