package game.gameUI;

import client.TcpMessage;
import game.construction.CommonConstruction;
import game.construction.IShipEquipment;
import game.construction.Placement;
import game.shields.CommonShield;
import game.ships.CommonShip;
import game.shots.CommonShot;
import game.startUpMenu.CommonMenu;
import game.static_classes.GlobalVariables;
import game.weapons.CommonWeapon;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

import java.util.ArrayList;


/**
 * Created by BobrZlosyn on 01.10.2016.
 */
public class DamageHandler {

    private CommonShip usersShip;
    private CommonShip enemyShip;
    private boolean toEnemy;
    private int shipDmg;
    private int shieldDmg;
    private String [] attackInformation;
    private Timeline shootingTimeline;
    private Pane gameArea;
    private ArrayList<CommonShot> removeShots, shots;
    private Button sendDataButton;

    public DamageHandler (CommonShip usersShip, CommonShip enemyShip, Pane gameArea, Button sendDataButton){
        this.usersShip = usersShip;
        this.enemyShip = enemyShip;
        this.gameArea = gameArea;
        this.sendDataButton = sendDataButton;

        shots = new ArrayList();
        removeShots = new ArrayList();
    }

    //msg - 1-0;; Shield Damage - 0-N;; Ship damage - 0-N;; Placement position From - 1-N,1-N,Placement position Target - 1-N, 1-N, Damage - 0-N; other placements ...;
    public int doDamage(String msgWithDmg){
        decodeMsg(msgWithDmg);

        if(toEnemy){
            calculateDamage(enemyShip, usersShip);
        }else {
            calculateDamage(usersShip, enemyShip);
        }

        return 0;
    }

    private int decodeMsg(String msgWithDmg){
        String [] msgFields = msgWithDmg.split(";;");

        if(msgFields.length < 3){
            //error
            return -1;
        }

        if(msgFields[0].equals("0")){
            toEnemy = false;
        }else {
            toEnemy = true;
        }

        try {
            shieldDmg = Integer.parseInt(msgFields[1]);
            shipDmg = Integer.parseInt(msgFields[2]);
        }catch (Exception e){
            e.printStackTrace();
            return -2;
        }

        if(msgFields.length == 4) {
            attackInformation = msgFields[3].split(";");
        }else {
            attackInformation = null;
        }


        return 0;

    }

    /**
     *
     * @param targetShip
     * @param attackShip
     * @return
     */
    private int calculateDamage(CommonShip targetShip, CommonShip attackShip){
        if(GlobalVariables.isEmpty(targetShip) || GlobalVariables.isEmpty(attackInformation)){
            return 0;
        }

        Placement [][] targetPlacements = targetShip.getPlacementPositions();
        Placement [][] attackPlacements = attackShip.getPlacementPositions();

        String positions [];

        try {
            for(int i = 0; i < attackInformation.length; i++){
                positions = attackInformation[i].split(",");

                if(positions.length != 6){
                    return -3;
                }

                int iAtacker = Integer.parseInt(positions[0]);
                int jAtacker = Integer.parseInt(positions[1]);
                int iTarget = Integer.parseInt(positions[2]);
                int jTarget = Integer.parseInt(positions[3]);
                int damage = Integer.parseInt(positions[4]);
                int intoShields = Integer.parseInt(positions[5]);


                if(GlobalVariables.isPlayingNow.getValue()){
                    int rowTarget = targetShip.getPlacementPositions().length - 1;
                    int rowAttack = attackShip.getPlacementPositions().length - 1;
                    iAtacker = rowAttack - iAtacker;

                    if(iTarget >= 0){
                        iTarget = rowTarget - iTarget;
                    }
                }

                double x, y;
                Placement placeAttacker = attackPlacements[iAtacker][jAtacker];
                CommonConstruction target;

                if(iTarget == -1 && jTarget == -1){
                    x = targetShip.getCenterX();
                    y = targetShip.getCenterY();
                    target = targetShip;
                }else {
                    Placement placeTarget = targetPlacements[iTarget][jTarget];
                    if(GlobalVariables.isEmpty(placeAttacker) || placeAttacker.isEmpty()
                            || GlobalVariables.isEmpty(placeTarget) || placeTarget.isEmpty()){
                        return -5;
                    }
                    target = (CommonConstruction) placeTarget.getShipEquipment();
                    x = target.getCenterX();
                    y = target.getCenterY();
                }


                if (!GlobalVariables.choosenShip.equals(placeAttacker.getShip())){
                    placeAttacker.getShipEquipment().rotateEquipment(x, y);
                }

                CommonShot commonShot;
                if(intoShields == 1){
                    commonShot = ((CommonWeapon)placeAttacker.getShipEquipment()).getShot(target, damage, true);
                }else {
                    commonShot = ((CommonWeapon)placeAttacker.getShipEquipment()).getShot(target, damage, false);
                }

                commonShot.addShot(gameArea);
                shots.add(commonShot);
            }

            createTimeLineShot();

        }catch (Exception e){
            e.printStackTrace();
            return -4;
        }

        return 0;
    }

    private void createTimeLineShot(){
        if(!GlobalVariables.isEmpty(shootingTimeline)){
            return;
        }

        shootingTimeline = new Timeline(new KeyFrame(Duration.seconds(0.03), e -> shooting()));
        shootingTimeline.setCycleCount(Animation.INDEFINITE);
        shootingTimeline.playFromStart();
    }

    private void shooting(){
        shots.forEach(simpleBallShot1 -> {
            if(simpleBallShot1.pocitatTrasu()){

                if(simpleBallShot1.isIntoShields()){
                    simpleBallShot1.getTarget().getPlacement().getShip().damageToShield(simpleBallShot1.getDamage());
                    simpleBallShot1.getWreck();
                }else {
                    if(simpleBallShot1.getTarget().getActualLife() > 0){
                        AudioClip audioClip = new AudioClip(CommonMenu.class.getResource("/game/resources/sounds/hit.wav").toExternalForm());
                        audioClip.volumeProperty().bind(GlobalVariables.volumeSound);
                        audioClip.play();

                        simpleBallShot1.getTarget().takeDamage(simpleBallShot1.getDamage());
                    }
                }

                simpleBallShot1.removeShot(gameArea);

                if(simpleBallShot1.getTarget().getActualLife() == 0
                        && !GlobalVariables.isEmpty((simpleBallShot1.getAttacker()))
                        ){

                    simpleBallShot1.getTarget().cancelTarget();
                    ((CommonWeapon)simpleBallShot1.getAttacker()).setTarget(null);
                    ((CommonWeapon)simpleBallShot1.getAttacker()).rotateToDefaultPosition();

                    int cost = simpleBallShot1.getAttacker().getEnergyCost();
                    simpleBallShot1.getAttacker().getPlacement().getShip().setActualEnergy(-cost);

                    //obnoveni informacich na vybranem objektu
                    if(GlobalVariables.canTarget.get()){
                        GlobalVariables.setCanTarget(false);
                        GlobalVariables.setCanTarget(true);
                    }
                }

                removeShots.add(simpleBallShot1);
            }
        });

        removeShots.forEach(simpleBallShot1 -> shots.remove(simpleBallShot1));

        if(shots.isEmpty()){
            sendDataButton.setDisable(!GlobalVariables.isPlayingNow.getValue());
            shootingTimeline.stop();
            shootingTimeline = null;
        }
    }

    public String exportShooting(Placement [][] placements){
        StringBuilder shooting = new StringBuilder();
        Placement placement, target;
        for(int i = 0; i < placements.length; i++){
            for(int j = 0; j < placements[i].length; j++){
                placement = placements[i][j];

                if(GlobalVariables.isEmpty(placement) || !placement.isWeapon()){
                    continue;
                }

                CommonWeapon weapon = ((CommonWeapon) placement.getShipEquipment());
                target = weapon.getTarget(); //ziskavani cile

                if(GlobalVariables.isEmpty(target)){
                    continue;
                }

                int min = weapon.getMinStrength();
                int max = weapon.getMaxStrength();
                int damage = (int) (Math.random() * (max - min)) + min;

                shooting.append(i + "," + j + ","); // pridani souradnic utocnika
                shooting.append(target.getRow() + "," + target.getColumn()); //pridani cile
                shooting.append("," + damage); //damage
                shooting.append(",1");
                shooting.append(";"); //ukonceni zbrane
            }
        }

        shooting.append(";"); // ukonceni strelby

        return shooting.toString();
    }


}
