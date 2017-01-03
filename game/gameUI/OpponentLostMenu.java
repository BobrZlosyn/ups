package game.gameUI;

import game.startUpMenu.CommonMenu;
import game.static_classes.GlobalVariables;
import game.static_classes.StyleClasses;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * Created by Kanto on 02.12.2016.
 */

public class OpponentLostMenu extends CommonMenu{

    private ProgressIndicator indicator;
    public final static String WAIT_FOR_OPONNENT_RECONNECTION = "Bylo ztraceno spojení se serverem. Čeká se na připojení.";
    public final static String WAIT_FOR_RECONNECTION = "Hráč se nenadále odpojil. Čeká se na znovu připojení hráče";

    private SimpleBooleanProperty timeExpired;
    private final int MAX_WAIT_TIME = 15;
    private SimpleIntegerProperty actualTime;
    private Button quit;
    private Label title, time;
    private final String QUIT = "Ukončit";
    private Timeline endWaitingAnimation;

    public OpponentLostMenu (String text) {
        title = createLabel(text);
        actualTime = new SimpleIntegerProperty(MAX_WAIT_TIME);
        time = createLabel(String.valueOf(MAX_WAIT_TIME));
        time.textProperty().bind(actualTime.asString());
        quit = createButton(QUIT, StyleClasses.EXIT_BUTTON);
        timeExpired = new SimpleBooleanProperty(false);
        indicator = new ProgressIndicator(-1);
        createEndWaitingAnimation();
        createWindow();
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    private void createWindow() {
        menuPane.getColumnConstraints().addAll(generateColumns(3, null));
        menuPane.getRowConstraints().addAll(generateRows(5, null));

        menuPane.add(createBackgroundPane(), 0, 0, GridPane.REMAINING, GridPane.REMAINING);
        menuPane.add(indicator, 1, 2);
        menuPane.add(title, 0, 1, GridPane.REMAINING, 1);
        menuPane.add(time, 1, 2);
        menuPane.add(quit, 1, 3);

        time.setAlignment(Pos.CENTER);
        time.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        title.setAlignment(Pos.CENTER);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setWrapText(true);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        GridPane.setHalignment(quit, HPos.CENTER);
    }

    private void createEndWaitingAnimation() {
        endWaitingAnimation = new Timeline(new KeyFrame(Duration.seconds(1), event -> runEndWaitingAnimation()));
        endWaitingAnimation.setCycleCount(Animation.INDEFINITE);
    }

    private void runAnimation() {
        timeExpired.set(false);
        actualTime.set(MAX_WAIT_TIME);
        endWaitingAnimation.playFromStart();
    }

    private void runEndWaitingAnimation() {
        int newValue = actualTime.get() - 1;
        actualTime.set(newValue);
        if (actualTime.isEqualTo(0).getValue() || GlobalVariables.APLICATION_EXIT) {
            endWaitingAnimation.stop();
            timeExpired.set(true);
        }
    }

    @Override
    public void showWindow(GridPane window) {
        super.showWindow(window);
        runAnimation();
    }

    @Override
    public void clean() {
        super.clean();
        endWaitingAnimation.stop();
    }

    public Button getQuit() {
        return quit;
    }

    public SimpleBooleanProperty getTimeExpiredProperty() {
        return timeExpired;
    }
}
