package game.construction;

import javafx.scene.transform.Rotate;

/**
 * Created by BobrZlosyn on 01.10.2016.
 */
public interface IShipEquipment {

    void displayEquipment(Placement place, boolean isEnemy);

    default Rotate calculationForRotation(double x, double y, double centerX, double centerY, boolean isEnemy){
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
            return new Rotate(cosinus, centerX, centerY);
        }else{
            return new Rotate(-cosinus, centerX, centerY);
        }
    }

    default void rotateEquipment(double x, double y) {
        return;
    }

    default boolean isWeapon(){
        return false;
    }
}
