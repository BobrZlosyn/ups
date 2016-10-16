package game.wrecks;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Created by BobrZlosyn on 16.10.2016.
 */
public class BattleShipWreck extends CommonShipWreck {

    private Timeline flashAnimation;
    private Circle flashCircle;
    public BattleShipWreck(double x, double y){
        flashCircle = new Circle(1, Color.WHITE);
        flashCircle.setCenterX(x);
        flashCircle.setCenterY(y);
    }

    public Circle getFlashCircle() {
        return flashCircle;
    }

    public void flash(){
        flashAnimation = new Timeline(new KeyFrame(Duration.seconds(0.02), event -> {
            double radius = flashCircle.getRadius() + 25;

            if(radius > 1050){
                double opacity = flashCircle.getOpacity() - 0.01;
                flashCircle.setOpacity(opacity);
                if(opacity <= 0 ){
                    flashAnimation.stop();
                }
            }else{
                flashCircle.setRadius(radius);
            }
        }));

        flashAnimation.setCycleCount(Animation.INDEFINITE);
        flashAnimation.playFromStart();
    }

    @Override
    public boolean wrecksMovement() {
        return false;
    }

    @Override
    public void removeWrecks() {

    }
}
