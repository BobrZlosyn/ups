package game.construction;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Kanto on 30.09.2016.
 */
public class CommonDraggableObject {


    protected Placement findPosition(Placement[][] placements, double x, double y, double xAdd1, double xAdd2, double yAdd1, double yAdd2 ){
        Placement place = null;
        for(int i = 0; i < placements.length; i++){
            for (int j = 0; j < placements[i].length; j++){
                if(placements[i][j] == null){
                    continue;
                }
                if(!placements[i][j].isEmpty()){
                    continue;
                }

                Rectangle pokus = placements[i][j].getField();
                //pravy horni roh, levy horni roh, PH dolu, LH dolu
                if(pokus.contains(x - xAdd1, y + yAdd1) || pokus.contains(x - xAdd2, y + yAdd1)
                        || pokus.contains(x - xAdd1, y + yAdd2) || pokus.contains(x - xAdd2, y + yAdd2)){
                    pokus.setFill(Color.BLUE);
                    place = placements[i][j];
                }else{
                    pokus.setFill(Color.WHITE);
                }
            }
        }
        return place;
    }
}
