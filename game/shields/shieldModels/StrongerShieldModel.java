package game.shields.shieldModels;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

/**
 * Created by Kanto on 14.11.2016.
 */
public class StrongerShieldModel extends SimpleShieldModel {

    public StrongerShieldModel(){
        super();
    }

    @Override
    public void setDefaultSkin() {

        getShield().setFill(Color.GREEN);
        getShield().setFill(new ImagePattern(
                new Image(getClass().getResource("/game/background/textures/shield2.jpg").toExternalForm()), 0, 0, 1, 1, true));
    }
}
