package game.shots;

import game.construction.CommonConstruction;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by BobrZlosyn on 02.10.2016.
 */
public class DoubleBallShot extends CommonShot{
    private Circle shot1;
    private Circle shot2;

    public DoubleBallShot(CommonConstruction target, CommonConstruction attacker, int damage, boolean isShields){
        super(target, attacker, damage, isShields);

        shot1 = new Circle(x1, y1, 4, Color.GOLD);
        shot2 = new Circle(x1, y1, 4, Color.GOLD);
        setXY();
    }

    @Override
    public void addShot(Pane gameArea){
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

        return target.containsPosition(x1,y1);
    }
}
