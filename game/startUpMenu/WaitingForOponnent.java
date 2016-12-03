package game.startUpMenu;

import game.static_classes.GlobalVariables;
import game.static_classes.StyleClasses;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Created by BobrZlosyn on 17.10.2016.
 * obrazovka ktera ma zabavit uzivatele nez se pripoji druhy hrac
 */
public class WaitingForOponnent extends CommonMenu{

    private Label title, hint;
    private Button cancel;
    private Pane starMap;
    private Timeline starAnimation;
    private ArrayList<Star> stars ;

    public static final String WAITING_FOR_OPONENT = "ČEKÁNÍ NA PROTIHRÁČE";
    public static final String CONNECTING_TO_SERVER = "PŘIPOJUJI SE K SEVERU";
    public static final String CREATING_GAME = "VYTVÁŘÍM MÍSTNOST S HROU";
    public static final String STARTING_GAME = "PŘESUNUJI SE NA SOUŘADNICE STŘETNUTÍ";
    private static final String CANCEL_SEARCH = "Zrušit";

    public WaitingForOponnent(){
        createPane();
        stars = new ArrayList<>();
    }

    private void createPane(){
        menuPane.setStyle("-fx-background-color:black;");
        menuPane.getColumnConstraints().addAll(generateColumns(3, null));
        menuPane.getRowConstraints().addAll(generateRows(7, null));

        title = new Label(WAITING_FOR_OPONENT);
        title.setTextFill(Color.WHITE);
        title.setMaxWidth(Double.MAX_VALUE);
        title.setMaxHeight(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setWrapText(true);
        title.setFont(Font.font(18));

        cancel = createButton(CANCEL_SEARCH, StyleClasses.EXIT_BUTTON);

        hint = new Label();
        hint.setTextFill(Color.WHITE);
        hint.setMaxWidth(Double.MAX_VALUE);
        hint.setMaxHeight(Double.MAX_VALUE);
        hint.setAlignment(Pos.CENTER);
        hint.setFont(Font.font(16));
        starMap = new Pane();
        starMap.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        menuPane.add(starMap, 0, 0, GridPane.REMAINING, GridPane.REMAINING);
        menuPane.add(cancel, 1, 6);
        menuPane.add(title, 0, 3, GridPane.REMAINING, 1);
        menuPane.add(hint, 0, 5, GridPane.REMAINING, 1);
        menuPane.getColumnConstraints().get(1).setHalignment(HPos.CENTER);
        GridPane.setValignment(cancel, VPos.TOP);
        GridPane.setHalignment(title, HPos.CENTER);
        GridPane.setValignment(title, VPos.CENTER);

        menuPane.getRowConstraints().get(0).setPercentHeight(15); //15
        menuPane.getRowConstraints().get(1).setPercentHeight(1); //16
        menuPane.getRowConstraints().get(2).setPercentHeight(1); //17
        menuPane.getRowConstraints().get(3).setPercentHeight(56); // 77
        menuPane.getRowConstraints().get(4).setPercentHeight(5); // 78
        menuPane.getRowConstraints().get(5).setPercentHeight(7); // 85
        menuPane.getRowConstraints().get(6).setPercentHeight(15); // 100
    }


    public void showWaitingForOponnent(GridPane window) {
        if(GlobalVariables.isEmpty(window)
                || GlobalVariables.isEmpty(menuPane)){
            return;
        }

        if(window.getChildren().contains(menuPane)) {
            return;
        }

        window.add(menuPane, 0, 0, GridPane.REMAINING, GridPane.REMAINING);
        hint.setText(pickRandomHint());
        if (stars.isEmpty()) {
            createStar();
        }

        starsAnimeation();
    }


    public Button getCancel() {
        return cancel;
    }

    private String pickRandomHint() {

        ArrayList <String> hints = new ArrayList <>();
        hints.add("RADA! Každý modul se štítem lze libovolně aktivovat a deaktiovat.");
        hints.add("RADA! Čím méně má loď života, tím méně odolná proti poškození.");
        hints.add("RADA! Pokud je zničeno vybavení na lodi, integrita trupu obdrží poškození též.");
        hints.add("RADA! Laserové dělo ignoruje aktivované štíty a přímo zásahne cíl.");
        hints.add("RADA! Hra končí jakmile život lodi klesne na nulu, nemusíte proto zničit všechno vybavení.");
        hints.add("RADA! Štíty můžete aktivovávat postupně, aby jste ušetřili energii.");

        int index = (int) (Math.random() * (hints.size()));

        return hints.get(index);
    }


    public void setTitleText(String text){
        Platform.runLater(() -> title.setText(text));
    }


    private void createStar(){
        int width = (int) ((GridPane)starMap.getParent().getParent()).getWidth();
        int height = (int)((GridPane)starMap.getParent().getParent()).getHeight();
        for(int i = 0; i < 1500; i++) {
            Star star = new Star(width, height);
            starMap.getChildren().addAll(star.getStar(), star.getLine());
            stars.add(star);
        }
    }

    @Override
    public void clean() {
        ((GridPane)menuPane.getParent()).getChildren().remove(menuPane);
        stopAnimation();
    }

    private void starsAnimeation(){
        if (GlobalVariables.isNotEmpty(starAnimation)) {
            starAnimation.playFromStart();
            return;
        }

        starAnimation = new Timeline(new KeyFrame(Duration.seconds(0.03), event -> {

             if (stars.isEmpty() || GlobalVariables.APLICATION_EXIT) {
                starAnimation.stop();
                return;
            }

            stars.forEach(circle -> {

                circle.show();
                circle.update();

            });

            if(GlobalVariables.isEmpty(stars.get(0).getStar().getParent())){
                starAnimation.stop();
            }

        }));
        starAnimation.setCycleCount(Animation.INDEFINITE);
        starAnimation.playFromStart();

    }

    public void stopAnimation(){
        starAnimation.stop();
    }
}



class Star {

    private float x, y, z;
    private float pz;
    private Circle star;
    private Line line;
    private float width, height;

    Star(float width, float height){
        star = new Circle(0.5);
        star.setTranslateX(width/2);
        star.setTranslateY(height/2);
        star.setFill(Color.WHITE);
        star.setCenterX( randomNumberBetween(-width, width));
        star.setCenterY( randomNumberBetween(-height, height));
        line = new Line();
        line.setTranslateX(width/2);
        line.setTranslateY(height/2);
        line.setStrokeWidth(0.1);
        line.setStroke(Color.WHITE);
        //randomColors();
        this.x = (float) star.getCenterX();
        this.y = (float) star.getCenterY();
        this.z = randomNumberBetween(0, width);
        pz = z;
    }

    private void randomColors() {
        int red = (int) (Math.random() * 255);
        int green = (int) (Math.random() * 255);
        int blue = (int) (Math.random() * 255);
        line.setStroke(Color.rgb(red, green, blue));
        star.setFill(Color.rgb(red, green, blue));
    }

    void update(){
        z = z - 35;
        if (z < 1) {
            z = width/2;
            x = randomNumberBetween(-width, width);
            y = randomNumberBetween(-height, height);
            pz = z;
        }
    }

    private float randomNumberBetween(float min, float max){
        float range = max - min;
        return (float)(Math.random() * range) + min;
    }

    private float map(float value, float istart, float istop, float ostart, float ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }

    void show() {

        width = (float) ((Pane) star.getParent()).getWidth();
        height = (float) ((Pane) star.getParent()).getHeight();

        float sx = map(x / z, 0, 1, 0, width/2);
        float sy = map(y / z, 0, 1, 0, height/2);

        float r = map(z, 0, width/2, 1, 0);
        star.setRadius(r);

        star.setCenterX(sx);
        star.setCenterY(sy);


        float px = map(x / pz, 0, 1, 0, width/2);
        float py = map(y / pz, 0, 1, 0, height/2);
        pz = z;

        line.setStartX(px);
        line.setStartY(py);
        line.setEndX(sx);
        line.setEndY(sy);
    }

    Circle getStar() {
        return star;
    }

    Line getLine() {
        return line;
    }
}
