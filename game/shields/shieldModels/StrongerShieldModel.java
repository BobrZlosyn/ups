package game.shields.shieldModels;

import javafx.scene.paint.Color;

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
