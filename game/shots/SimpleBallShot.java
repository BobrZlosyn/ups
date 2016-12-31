package game.shots;

import game.construction.CommonConstruction;
import game.shots.wrecksShot.SimpleShotWreck;
import game.startUpMenu.CommonMenu;
import game.static_classes.GlobalVariables;
import game.weapons.CommonWeapon;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by BobrZlosyn on 02.10.2016.
 */
public class SimpleBallShot extends CommonShot{

    private Circle shot;

    public SimpleBallShot(CommonConstruction target, CommonConstruction attacker, int damage, boolean intoShields){
        super(target, attacker, damage, intoShields);
        shot = new Circle(x1, y1, 4, Color.GOLD);
        setXY();
    }

    @Override
    public void addShot(Pane gameArea) {
        ((CommonWeapon) attacker).shotFired();
        setShotFiredStatus(true);
        gameArea.getChildren().add(shot);
    }

    @Override
    public void removeShot(Pane gameArea) {
        gameArea.getChildren().remove(shot);
    }


    public void setXY(){
        for(int i = 0; i < 20; i++){
            pocitatTrasu();
        }
    }

    public boolean pocitatTrasu(){
        returnToPreparePosition(25);

        double [] coordinates = rovnicePrimka(x1,y1,target.getCenterX(),target.getCenterY());

        x1 = coordinates[0];
        y1 = coordinates[1];

        shot.setCenterX(x1);
        shot.setCenterY(y1);

        if(isIntoShields() && target.getPlacement().getShip().getShieldActualLife() != 0){
            return target.getPlacement().getShip().isOnShield(x1, y1);
        }else {
            setIntoShields(false);
        }

        return  shotInPosition(x1, target.getCenterX(),y1, target.getCenterY());
    }

    @Override
    public SimpleShotWreck getWreck() {

        SimpleShotWreck shotWreck = new SimpleShotWreck(attacker.isEnemy());
        shotWreck.addWrecksToPane((Pane)shot.getParent(), x1, y1);
        return shotWreck;
    }

    @Override
    public void soundOfShot() {
        AudioClip audioClip = new AudioClip(CommonMenu.class.getResource("/game/resources/sounds/cannon_gun.wav").toExternalForm());
        audioClip.volumeProperty().bind(GlobalVariables.volumeSound);
        audioClip.play();
    }

}
