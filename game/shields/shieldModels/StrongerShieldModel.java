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
    }
}
