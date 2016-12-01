package game.construction;


/**
 * Created by BobrZlosyn on 01.10.2016.
 */
public interface IShipEquipment {

    void displayEquipment(Placement place, boolean isEnemy);

    CommonModel getModel ();

    default double calculationForRotation(double x, double y, double centerX, double centerY, boolean isEnemy){
        double countCx = (x - centerX)*(x - centerX);
        double countAx = (x - centerX)*(x - centerX);
        double countAy = (y - centerY)*(y - centerY);
        double sideC = Math.sqrt(countCx);
        double sideA = Math.sqrt(countAx + countAy);

        double cosinus = Math.toDegrees(Math.acos(sideC / sideA));
        if(y > centerY){
            cosinus *= -1;
        }

        if(isEnemy){
            return cosinus;
        }else{
            return -cosinus;
        }
    }

    default void rotateEquipment(double x, double y) {
        return;
    }

    default void rotateToDefaultPosition(){
        return;
    }

    default boolean isWeapon(){
        return false;
    }

    default boolean isShield(){
        return false;
    }

}
