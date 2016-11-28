package game.static_classes;

import javafx.scene.Cursor;

/**
 * Created by BobrZlosyn on 27.11.2016.
 */
public class StyleClasses {

    public static final String MENU_BUTTONS = "menuButtons";
    public static final String SHIELD_STATUS = "shieldStatus";
    public static final String STATUS_LABEL = "statusLabel";
    public static final String EXIT_BUTTON = "exitButton";
    public static final String BOTTOM_PANEL = "bottomPanel";
    public static final String LIFE_STATUS = "lifeStatus";


    public static final Cursor NORMAL_CURSOR = Cursor.cursor(StyleClasses.class.getResource("/game/background/textures/cursor.png").toExternalForm());
    public static final Cursor ENEMY_CURSOR = Cursor.cursor(StyleClasses.class.getResource("/game/background/textures/cursorEnemy.png").toExternalForm());
    public static final Cursor HAND_CURSOR = Cursor.cursor(StyleClasses.class.getResource("/game/background/textures/cursorSelect.png").toExternalForm());
}
