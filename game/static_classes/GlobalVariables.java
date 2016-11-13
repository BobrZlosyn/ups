package game.static_classes;

import game.construction.CommonConstruction;
import game.ships.CommonShip;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;


/**
 * Created by BobrZlosyn on 28.09.2016.
 */
public class GlobalVariables {
    public static SimpleStringProperty name = new SimpleStringProperty();
    public static CommonConstruction targetObject;
    public static CommonConstruction markedObject;
    public static boolean isTargeting = false;
    public static SimpleBooleanProperty canTarget = new SimpleBooleanProperty(false);
    public static SimpleBooleanProperty isSelected = new SimpleBooleanProperty(false);
    private static boolean isUsersShieldUp = false;
    private static boolean isEnemyShieldUp = false;
    public static SimpleBooleanProperty gameIsFinished = new SimpleBooleanProperty(false);
    public static SimpleBooleanProperty enemyLost = new SimpleBooleanProperty(false);
    public static SimpleBooleanProperty isPlayingNow = new SimpleBooleanProperty(false);
    public static CommonShip choosenShip;
    public static Color damageHit = Color.RED;
    public static double damageHitDuration = 0.2;
    public static String shipDefinition = "";
    public static String enemyshipDefinition = "";
    public static String startingID = "";
    public static SimpleStringProperty attackDefinition = new SimpleStringProperty("");
    public static SimpleStringProperty equipmentStatus = new SimpleStringProperty("");
    public static String expectedMsg = "";
    public static String receivedMsg = "";
    public static String errorMsg = "";


    public static void setMarkedObject(CommonConstruction markedObject) {
        if(!isEmpty(markedObject) && !isEmpty(GlobalVariables.markedObject)){
            GlobalVariables.markedObject.unmarkObject();
        }

        GlobalVariables.markedObject = markedObject;
        if(!isEmpty(markedObject)){
            setIsSelected(true);
        }else{
            if(!isEmpty(targetObject)){
                setTargetObject(targetObject);
            }
            setIsSelected(false);
        }

    }

    public static void setTargetObject(CommonConstruction targetObject) {
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

    public static void setIsUsersShieldUp(boolean isUsersShieldUp) {
        GlobalVariables.isUsersShieldUp = isUsersShieldUp;
    }

    public static void setIsEnemyShieldUp(boolean isEnemyShieldUp) {
        GlobalVariables.isEnemyShieldUp = isEnemyShieldUp;
    }

    public static void setCanTarget(boolean canTarget) {
        GlobalVariables.canTarget.set(canTarget);
    }

    public static void setName(String name) {
        GlobalVariables.name.set(name);
    }

    public static CommonConstruction getMarkedObject() {
        return markedObject;
    }

    public static CommonConstruction getTargetObject() {
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

    public static boolean isEnemyShieldUp() {
        return isEnemyShieldUp;
    }

    public static boolean isUsersShieldUp() {
        return isUsersShieldUp;
    }

    public static void setGameIsFinished(boolean gameIsFinished) {
        GlobalVariables.gameIsFinished.set(gameIsFinished);
    }

    public static SimpleBooleanProperty gameIsFinishedProperty() {
        return gameIsFinished;
    }
}
