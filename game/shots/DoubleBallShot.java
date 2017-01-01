package game.shots;

import game.construction.CommonConstruction;
import game.construction.Placement;
import game.shots.wrecksShot.DoubleShotWreck;
import game.shots.wrecksShot.SimpleShotWreck;
import game.startUpMenu.CommonMenu;
import game.static_classes.GlobalVariables;
import game.weapons.CommonWeapon;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

import java.time.LocalTime;

/**
 * Created by BobrZlosyn on 02.10.2016.
 */
public class DoubleBallShot extends CommonShot{
    private Circle shot1;
    private Circle shot2;

    public DoubleBallShot(CommonConstruction target, CommonConstruction attacker, int damage, boolean isShields){
        super(target, attacker, damage, isShields);
        RadialGradient gradient1 = new RadialGradient(0,
                .1,
                x1,
                y1,
                10,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.GOLD),
                new Stop(1, Color.GREEN));

        shot1 = new Circle(x1, y1, 10, gradient1);
        shot2 = new Circle(x1, y1, 10, gradient1);
        setXY();
    }

    @Override
    public void addShot(Pane gameArea){
        ((CommonWeapon) attacker).shotFired();
        setShotFiredStatus(true);
        gameArea.getChildren().addAll(shot1, shot2);
    }

    @Override
    public void removeShot(Pane gameArea){
        gameArea.getChildren().removeAll(shot1, shot2);
    }

    public void setXY(){
        //posunuti pred kanon
        for(int i = 0; i < 20; i++){
            pocitatTrasu();
        }
    }

    @Override
    public boolean pocitatTrasu(){
        returnToPreparePosition(25);
        double [] coordinates = rovnicePrimka(x1,y1,target.getCenterX(),target.getCenterY());

        x1 = coordinates[0];
        y1 = coordinates[1];

        shot1.setCenterX(x1 );
        shot1.setCenterY(y1 - 5);

        shot2.setCenterX(x1 + 2);
        shot2.setCenterY(y1 + 5);

        if(isIntoShields() && target.getPlacement().getShip().getShieldActualLife() != 0){
            return target.getPlacement().getShip().isOnShield(x1, y1);
        }else {
            setIntoShields(false);
        }

        return shotInPosition(x1, target.getCenterX(),y1, target.getCenterY());
    }

    @Override
    public DoubleShotWreck getWreck() {

        DoubleShotWreck shotWreck = new DoubleShotWreck(x1, y1 -5, x1 + 2, y1 + 5, attacker.isEnemy());
        shotWreck.addWrecksToPane((Pane)shot1.getParent(), x1, y1);
        return shotWreck;
    }

    @Override
    public void soundOfShot() {
        AudioClip audioClip = new AudioClip(getClass().getResource("/game/resources/sounds/cannon_gun.wav").toExternalForm());
        audioClip.volumeProperty().bind(GlobalVariables.volumeSound);
        audioClip.play();
    }
}
