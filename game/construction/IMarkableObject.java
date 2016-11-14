package game.construction;

import game.static_classes.GlobalVariables;
import javafx.scene.shape.Shape;

/**
 * Created by BobrZlosyn on 28.09.2016.
 */
public interface IMarkableObject {

    void markObject();

    void unmarkObject();

    void target();

    default void cancelTarget() {
        return;
    }

    default void markingHandle(boolean isMarked, CommonConstruction object){
        if(GlobalVariables.isTargeting){
            target();
            return;
        }

        //pokud je oznacen jako cil, ale ma byt nahle oznacen jako normalni konstrukce
        if(this.equals(GlobalVariables.getTargetObject())){
            cancelTarget();
            GlobalVariables.setTargetObject(object);
            markObject();
            return;
        }

        if(!isMarked){
            markObject();
        }else{
            unmarkObject();
        }
    }

}
