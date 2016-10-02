package game;

import game.construction.CommonConstruction;
import game.construction.Placement;
import game.weapons.CommonWeapon;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 * Created by BobrZlosyn on 26.09.2016.
 */
public class BottomPanel {

    private Button quit;
    private Button targeting;
    private GridPane panel;
    private Label name, lifeLabel;
    private boolean setTarget;
    private ProgressBar lifeProgress;



    public BottomPanel(Button sendOrders){
        createButtonQuit();
        createButtonSend(sendOrders);
        createName();
        createButtonTargeting();
        createLifeProgressBar();
        createPanel(sendOrders);

    }


    private void createButtonSend(Button sendOrders){
        sendOrders.setText("DÁT ROZKAZ K ÚTOKU");
        sendOrders.setMaxWidth(Double.MAX_VALUE);
        sendOrders.setMaxHeight(Double.MAX_VALUE);

    }

    private void createLifeProgressBar(){
        lifeProgress = new ProgressBar(1);
        lifeProgress.setMaxWidth(Double.MAX_VALUE);
        lifeProgress.setMaxHeight(Double.MAX_VALUE);

        lifeLabel = new Label("život konstrukce");
        lifeLabel.getStyleClass().add("statusLabel");
        lifeLabel.setMaxWidth(Double.MAX_VALUE);
        lifeLabel.setAlignment(Pos.CENTER);
        lifeProgress.visibleProperty().bind(GlobalVariables.isSelected);
        lifeLabel.visibleProperty().bind(GlobalVariables.isSelected);

        //nastavi binding na zivot kliknuteho objektu nebo binding odstrani
        lifeProgress.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.booleanValue()){
                CommonConstruction construction = ((CommonConstruction) GlobalVariables.getMarkedObject());
                lifeProgress.progressProperty().bind(construction.getActualLifeBinding());
            }else {
                lifeProgress.progressProperty().unbind();
            }
        });
    }

    private void createButtonTargeting(){
        targeting = new Button();
        targeting.setText("Zaměřit cíl");
        targeting.visibleProperty().bind(GlobalVariables.canTarget);
        targeting.setMaxWidth(Double.MAX_VALUE);
        targeting.setMaxHeight(Double.MAX_VALUE);

        targeting.setOnAction(event -> {
            if(!setTarget){
                setTarget = true;
                GlobalVariables.setIsTargeting(true);
                targeting.setText("Potvrdit cíl");
            }else {

                if(!GlobalVariables.isEmpty(GlobalVariables.getTargetObject())){
                    CommonWeapon weapon = (CommonWeapon) GlobalVariables.getMarkedObject();
                    CommonConstruction construction = GlobalVariables.getTargetObject();
                    weapon.rotateEquipment(construction.getCenterX(), construction.getCenterY());
                    System.out.println(construction.getPlacement());
                    weapon.setTarget(construction.getPlacement());
                    GlobalVariables.setTargetObject(construction);
                    weapon.unmarkObject();
                }

                setTarget = false;
                GlobalVariables.setIsTargeting(false);
                targeting.setText("Zaměřit cíl");
            }

        });

    }

    private void createButtonQuit(){
        quit = new Button("UTÉCT Z BOJE");
        quit.setMaxWidth(Double.MAX_VALUE);
        quit.setMaxHeight(Double.MAX_VALUE);
    }

    private void createName(){
        name = new Label("Název kliknutého objektu");
        name.textProperty().bind(GlobalVariables.name);
        name.getStyleClass().add("statusLabel");
    }

    private void createPanel(Button sendOrders){
        panel = new GridPane();
        panel.setMaxWidth(Double.MAX_VALUE);
        panel.getStyleClass().add("bottomPanel");

        RowConstraints rowConstraints1 = new RowConstraints(50);
        RowConstraints rowConstraints2 = new RowConstraints(50);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(40);
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setPercentWidth(20);
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setPercentWidth(20);
        ColumnConstraints columnConstraints3 = new ColumnConstraints();
        columnConstraints3.setPercentWidth(20);
        columnConstraints3.setHalignment(HPos.RIGHT);


        panel.getColumnConstraints().addAll(columnConstraints, columnConstraints1, columnConstraints2, columnConstraints3);
        panel.getRowConstraints().addAll(rowConstraints1, rowConstraints2);

        panel.add(name, 1,0);
        panel.add(sendOrders, 3, 0);
        panel.add(quit, 3, 1);
        panel.add(targeting, 2, 1);
        panel.add(lifeProgress, 0, 1);
        panel.add(lifeLabel, 0, 1);

        panel.setMargin(sendOrders, new Insets(15,10,5,10));
        panel.setMargin(quit, new Insets(10,10,10,10));
        panel.setMargin(targeting, new Insets(10,10,10,10));
        panel.setMargin(lifeProgress, new Insets(10,10,10,10));
    }

    public void showPanel(GridPane window){
        window.add(panel,0,1,GridPane.REMAINING,GridPane.REMAINING);
    }

    public void removePanle(GridPane window){
        window.getChildren().remove(panel);
    }
}
