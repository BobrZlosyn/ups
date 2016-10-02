package game;

import game.construction.IMarkableObject;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by BobrZlosyn on 28.09.2016.
 */
public class GlobalVariables {
    public static SimpleStringProperty name = new SimpleStringProperty();
    public static IMarkableObject targetObject;
    public static IMarkableObject markedObject;
    public static boolean isTargeting = false;
    public static SimpleBooleanProperty canTarget = new SimpleBooleanProperty(false);
    public static SimpleBooleanProperty isSelected = new SimpleBooleanProperty(false);
    private static boolean isShieldUp = false;

    public static void setMarkedObject(IMarkableObject markedObject) {
        if(!isEmpty(markedObject) && !isEmpty(GlobalVariables.markedObject)){
            GlobalVariables.markedObject.unmarkObject();
        }
        GlobalVariables.markedObject = markedObject;
        if(!isEmpty(markedObject)){
            setIsSelected(true);
        }else{
            setIsSelected(false);
        }

    }

    public static void setTargetObject(IMarkableObject targetObject) {
        if(!isEmpty(GlobalVariables.targetObject)){
            GlobalVariables.targetObject.cancelTarget();

            if(targetObject.equals(GlobalVariables.targetObject)){
                GlobalVariables.targetObject = null;
                return;
            }
        }
        GlobalVariables.targetObject = targetObject;
    }

    public static void setIsSelected(boolean isSelected) {
        GlobalVariables.isSelected.set(isSelected);
    }

    public static void setIsTargeting(boolean isTargeting) {
        GlobalVariables.isTargeting = isTargeting;
    }

    public static void setIsShieldUp(boolean isShieldUp) {
        GlobalVariables.isShieldUp = isShieldUp;
    }

    public static void setCanTarget(boolean canTarget) {
        GlobalVariables.canTarget.set(canTarget);
    }

    public static void setName(String name) {
        GlobalVariables.name.set(name);
    }

    public static IMarkableObject getMarkedObject() {
        return markedObject;
    }

    public static IMarkableObject getTargetObject() {
        return targetObject;
    }

    public static boolean getIsSelected() {
        return isSelected.get();
    }

    public static String getName() {
        return name.get();
    }

    public static boolean isTargeting() {
        return isTargeting;
    }

    public static boolean isEmpty(Object object){
        return object == null;
    }

    public static boolean isShieldUp() {
        return isShieldUp;
    }
}
