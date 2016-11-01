package game.background;

import game.ExportImportShip;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by BobrZlosyn on 26.09.2016.
 */
public class GeneratRandomBackground {
    private ArrayList<File> files;
    private String selectedImage;
    public GeneratRandomBackground(){

    }

    public void findImages(){
        String path = getClass().getResource("images").getPath();
        File file = new File(path);
        files = new ArrayList<File>(Arrays.asList(file.listFiles()));
    }

    public void chooseImage(GridPane pane, String id){
        if(files == null || files.isEmpty() ){
            return;
        }

        int index;
        int size = files.size();
        try {
            index = Integer.parseInt(id) % size;
        }catch (Exception e){
            index = 0 + (int)(Math.random() * size);
        }


        String path = "images/" + files.get(index).getName();
        double width = pane.getWidth();
        double height = pane.getHeight();
        pane.setStyle("-fx-background-image: url('./game/background/" + path + "'); -fx-background-repeat: stretch; -fx-background-size: " + width + " " + height +";");
        selectedImage = path;
    }

    public void resizeImage(GridPane pane, double newWidth, double newHeight){
        pane.setStyle("-fx-background-image: url('./game/background/" + selectedImage + "'); -fx-background-repeat: stretch; -fx-background-size: " + newWidth + " " + newHeight +";");
    }

    public void showSpacePort(GridPane pane){
        String path = getClass().getResource("imagesSpacePort").getPath();
        File file = new File(path);
        files = new ArrayList<File>(Arrays.asList(file.listFiles()));
        if(files == null || files.isEmpty() ){
            return;
        }

        int size = files.size();
        int index = 0 + (int)(Math.random() * size);
        String path1 = "imagesSpacePort/" + files.get(index).getName();

        double width = pane.getWidth();
        double height = pane.getHeight();

        pane.setStyle("-fx-background-image: url('./game/background/" + path1 + "'); -fx-background-repeat: stretch; -fx-background-size: " + width + " " + height +";");
        selectedImage = path1;
    }

}
