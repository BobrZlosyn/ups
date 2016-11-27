package game.shots;

import game.construction.CommonConstruction;
import game.construction.CommonWreck;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;

/**
 * Created by Kanto on 15.11.2016.
 */
public class LaserShot extends CommonShot {
    private Line shot;
    private int countHit;

    public LaserShot(CommonConstruction target, CommonConstruction attacker, int damage, boolean intoShields) {
        super(target, attacker, damage, intoShields);
        setIntoShields(false);
        shot = new Line();
        countHit = 0;
        setXY();
        shot.setStrokeWidth(3);
        shot.setFill(Color.RED);
        shot.setStroke(Color.rgb(255,0,0,0.7));
    }

    public void setXY(){
        //posunuti pred kanon
        for(int i = 0; i < 15; i++){
            double [] coordinates = rovnicePrimka(x1,y1,target.getCenterX(),target.getCenterY());
            x1 = coordinates[0];
            y1 = coordinates[1];
        }

        shot.setStartX(x1);
        shot.setStartY(y1);
        shot.setEndX(x1);
        shot.setEndY(y1);
    }

    @Override
    public void addShot(Pane gameArea) {
        gameArea.getChildren().add(shot);
    }

    @Override
    public void removeShot(Pane gameArea) {
        gameArea.getChildren().remove(shot);
    }

    @Override
    public boolean pocitatTrasu() {
        for (int i = 0; i < 3; i++){
            returnToPreparePosition(25);
            if(countHit != 0){
                break;
            }

            double [] coordinates = rovnicePrimka(x1,y1,target.getCenterX(),target.getCenterY());
            x1 = coordinates[0];
            y1 = coordinates[1];
        }

        shot.setEndX(x1);
        shot.setEndY(y1);

        if(countHit <= 25 && target.containsPosition(x1,y1) ){
            countHit ++;
            hittingTarget();
            return false;
        } else if (countHit > 25) {
            target.getModel().setDefaultSkin();
            return true;
        }

        return false;
    }

    private void hittingTarget(){
        if(countHit % 5 == 0) {
            target.getModel().getParts().forEach(shape -> shape.setFill(Color.RED));
        }else {
            target.getModel().setDefaultSkin();
        }
    }


    @Override
    public CommonWreck getWreck() {
        return null;
    }
}
