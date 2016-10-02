package game.shields.shieldModels;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 30.09.2016.
 */
public class ModelSimpleShield {
    private Rectangle shield;

    public ModelSimpleShield(){
        createShiled();
    }

    private void createShiled(){
        shield = new Rectangle(50,50);
        shield.setFill(Color.ORANGE);
    }

    public void setShieldXY(double x, double y){
        shield.setX(x);
        shield.setY(y);
    }

    public Rectangle getShield() {
        return shield;
    }

    public ArrayList<Shape> getParts(){
        ArrayList<Shape> shieldParts = new ArrayList<>();
        shieldParts.add(shield);
        return shieldParts;
    }
}
