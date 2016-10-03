package game;

import game.construction.CommonConstruction;
import game.construction.IShipEquipment;
import game.construction.Placement;
import game.shields.SimpleShield;
import game.ships.BattleShip;
import game.ships.CommonShip;
import game.ships.CruiserShip;
import game.shots.CommonShot;
import game.shots.DoubleBallShot;
import game.shots.SimpleBallShot;
import game.weapons.CannonWeapon;
import game.weapons.CommonWeapon;
import game.weapons.DoubleCannonWeapon;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
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

    public DamageHandler (CommonShip usersShip, CommonShip enemyShip, Pane gameArea){
        this.usersShip = usersShip;
        this.enemyShip = enemyShip;
        this.gameArea = gameArea;
        shots = new ArrayList();
        removeShots = new ArrayList();
        doDamage("");
    }

    //msg - 1-0;; Shield Damage - 0-N;; Ship damage - 0-N;; Placement position From - 1-N,1-N,Placement position Target - 1-N, 1-N, Damage - 0-N; other placements ...;
    public int doDamage(String msgWithDmg){
        msgWithDmg = "1;;35;;25;;2,2,2,2,85;";
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
            return -2;
        }

        attackInformation = msgFields[3].split(";");

        return 0;

    }

    private int calculateDamage(CommonShip targetShip, CommonShip attackShip){
        targetShip.damageToShield(shieldDmg);
        targetShip.takeDamage(shipDmg);

        Placement [][] targetPlacements = targetShip.getPlacementPositions();
        Placement [][] attackPlacements = attackShip.getPlacementPositions();

        String positions [];

        try {


            for(int i = 0; i < attackInformation.length; i++){
                positions = attackInformation[i].split(",");

                if(positions.length != 5){
                    return -3;
                }

                int iAtacker = Integer.parseInt(positions[0]);
                int jAtacker = Integer.parseInt(positions[1]);
                int iTarget = Integer.parseInt(positions[2]);
                int jTarget = Integer.parseInt(positions[3]);
                int damage = Integer.parseInt(positions[4]);

                Placement placeAttacker = attackPlacements[iAtacker][jAtacker];
                Placement placeTarget = targetPlacements[iTarget][jTarget];
                if(GlobalVariables.isEmpty(placeAttacker) || placeAttacker.isEmpty()
                        || GlobalVariables.isEmpty(placeTarget) || placeTarget.isEmpty()){
                    return -5;
                }
                CommonConstruction target = (CommonConstruction) placeTarget.getShipEquipment();
                double x = target.getCenterX();
                double y = target.getCenterY();
                placeAttacker.getShipEquipment().rotateEquipment(x, y);


                CommonShot commonShot = ((CommonWeapon)placeAttacker.getShipEquipment()).getShot(target, damage);
                commonShot.addShot(gameArea);
                shots.add(commonShot);
            }

            createTimeLineShot();

        }catch (Exception e){
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
                simpleBallShot1.removeShot(gameArea);
                simpleBallShot1.getTarget().takeDamage(simpleBallShot1.getDamage());
                removeShots.add(simpleBallShot1);
            }
        });

        removeShots.forEach(simpleBallShot1 -> shots.remove(simpleBallShot1));

        if(shots.isEmpty()){
            shootingTimeline.stop();
            shootingTimeline = null;
        }
    }

    private String exportShooting(Placement [][] placements){
        StringBuilder shooting = new StringBuilder();
        Placement placement, target;

        for(int i = 0; i < placements.length; i++){
            for(int j = 0; j < placements[i].length; j++){
                placement = placements[i][j];

                if(GlobalVariables.isEmpty(placement) && !placement.isWeapon()){
                    continue;
                }

                shooting.append(i + "," + j + ","); // pridani souradnic utocnika
                target = ((CommonWeapon) placement.getShipEquipment()).getTarget(); //ziskavani cile
                shooting.append(target.getRow() + "," + target.getColumn()); //pridani cile
                shooting.append(";"); //ukonceni zbrane
            }
        }

        shooting.append(";;"); // ukonceni strelby

        return shooting.toString();
    }


}
