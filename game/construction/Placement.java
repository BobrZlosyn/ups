package game.construction;
import game.ships.CommonShip;
import game.static_classes.GlobalVariables;
import javafx.scene.paint.Color;
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
    private boolean isShield;
    private int row, column;
    private final Color WARNING = Color.color(1,0.2,0.2,0.9);
    private final Color FULL_PLACE = Color.rgb(14,100,160,0.9);

    private final Color EMPTY_PLACE = Color.color(1,1,1,0.15);

    public Placement (double x, double y, double size, CommonShip ship, int row, int column) {
        setX(x);
        setY(y);
        setSize(size);
        setIsEmpty(true);
        setIsWeapon(false);
        setIsShield(false);
        this.ship = ship;
        this.row = row;
        this.column = column;

    }

    public void setIsWeapon(boolean isWeapon) {
        this.isWeapon = isWeapon;
    }

    public void setIsShield(boolean isShield) {
        this.isShield = isShield;
    }

    public void setField(Rectangle field) {
        this.field = field;
        setClearPlaceColor();
    }

    public void setClearPlaceColor(){
        this.field.setFill(EMPTY_PLACE);
    }

    public void setFullPlaceColor(){
        this.field.setFill(FULL_PLACE);
    }

    public void setErrorPlaceColor(){
        this.field.setFill(WARNING);
    }

    public boolean isWarningColorSet(){
        return field.getFill().equals(WARNING);
    }

    public boolean isFullColorSet(){
        return field.getFill().equals(FULL_PLACE);
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
        return GlobalVariables.isEmpty(equipment);
    }

    public boolean isWeapon() {

        if(isEmpty()){
            return false;
        }else{
            return equipment.isWeapon();
        }
    }

    public boolean isShield() {
        if(isEmpty()){
            return false;
        }else{
            return equipment.isShield();
        }
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
