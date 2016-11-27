package game.shields.shieldModels;

import game.construction.CommonModel;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 30.09.2016.
 */
public class SimpleShieldModel extends CommonModel {
    private Rectangle shield;

    public SimpleShieldModel(){
        createShiled();
    }

    private void createShiled(){
        shield = new Rectangle(50,50);
        setDefaultSkin();

        addCursorHandClass();
        strokeOnMouseOver();
    }

    public Rectangle getShield() {
        return shield;
    }

    @Override
    public ArrayList<Shape> getParts(){
        ArrayList<Shape> shieldParts = new ArrayList<>();
        shieldParts.add(shield);
        return shieldParts;
    }

    @Override
    public Pane getParent() {
        return (Pane)shield.getParent();
    }

    @Override
    public void setModelXY(double x, double y) {
        shield.setX(x - getWidth()/2);
        shield.setY(y - getWidth()/2);
    }

    @Override
    public double getWidth() {
        return shield.getWidth();
    }

    @Override
    public double getHeight() {
        return shield.getHeight();
    }

    @Override
    public double getCenterX() {
        return shield.getX() + getWidth()/2;
    }

    @Override
    public double getCenterY() {
        return shield.getY() - getHeight();
    }

    @Override
    public void setDefaultSkin() {
        shield.setFill(Color.ORANGE);
        //shield.setFill(new ImagePattern(
          //      new Image(getClass().getResource("/game/background/textures/shield1.jpg").toExternalForm()), 0, 0, 1, 1, true));

    }

    @Override
    public boolean containsPosition(double x, double y) {
        return shield.contains(x, y);
    }

}
