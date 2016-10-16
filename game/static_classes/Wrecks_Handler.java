package game.static_classes;


import game.wrecks.CommonWreck;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 16.10.2016.
 */
public class Wrecks_Handler {
    private static Timeline wrecksAnimation;
    private static ArrayList<CommonWreck> wrecks = new ArrayList<>();

    public static void addWrecks(CommonWreck wreck){
        if(GlobalVariables.isEmpty(wreck)){
           return;
        }

        wrecks.add(wreck);
        startAnimation();
    }

    private static void startAnimation(){

        if(!GlobalVariables.isEmpty(wrecksAnimation)){
            resumeAnimation();
            return;
        }

        wrecksAnimation = new Timeline(new KeyFrame(Duration.seconds(0.05), event -> wrecksMovementAnimation()));
        wrecksAnimation.setCycleCount(Animation.INDEFINITE);
        wrecksAnimation.playFromStart();
    }

    private static void wrecksMovementAnimation(){
        for (int i = 0; i < wrecks.size(); i++){

            if(wrecks.get(i).wrecksMovement()){
                wrecks.get(i).removeWrecks();
                wrecks.remove(i);
            }
        }

        if(wrecks.isEmpty()){
            pauseAnimation();
        }
    }

    public static void stopAnimation(){
        if(!GlobalVariables.isEmpty(wrecksAnimation)){
            wrecksAnimation.stop();
            wrecksAnimation = null;
            wrecks.clear();
        }
    }

    private static void pauseAnimation(){
        if(!GlobalVariables.isEmpty(wrecksAnimation)){
            wrecksAnimation.pause();
        }
    }

    private static void resumeAnimation(){
        if(!GlobalVariables.isEmpty(wrecksAnimation)){
            wrecksAnimation.playFromStart();
        }
    }
}
