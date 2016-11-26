package game.background;

import game.static_classes.GlobalVariables;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by BobrZlosyn on 26.09.2016.
 */
public class GeneratRandomBackground {
    private ArrayList<File> files;
    private String portImage;
    private String welcomeImage;
    private String activeImage;

    public GeneratRandomBackground(){
        welcomeImage = "";
        portImage = "";
        activeImage = "";
    }

    public void findImages(){
        String path = getClass().getResource("images").getPath();
        File file = new File(path);
        files = new ArrayList<>(Arrays.asList(file.listFiles()));
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
        activeImage = path;
    }

    public void resizeImage(GridPane pane, double newWidth, double newHeight){
        pane.setStyle("-fx-background-image: url('./game/background/" + activeImage + "'); -fx-background-repeat: stretch; -fx-background-size: " + newWidth + " " + newHeight +";");
    }

    public void showSpacePort(GridPane pane){
        portImage = loadImage(pane, "imagesSpacePort", portImage);
    }

    public void showWelcomeImage(GridPane pane){
        welcomeImage = loadImage(pane, "welcomeImage", welcomeImage);
        portImage = "";
    }


    private String loadImage(GridPane pane, String folder, String selectedImage){

        String path1;
        if (selectedImage.isEmpty()) {
            String path = getClass().getResource(folder).getPath();
            File file = new File(path);
            files = new ArrayList<>(Arrays.asList(file.listFiles()));
            if(GlobalVariables.isEmpty(files) || files.isEmpty() ){
                return "";
            }

            int size = files.size();
            int index = 0 + (int)(Math.random() * size);
            path1 =  folder + "/" + files.get(index).getName();
        }else {
            path1 = selectedImage;
        }

        activeImage = path1;
        double width = pane.getWidth();
        double height = pane.getHeight();

        pane.setStyle("-fx-background-image: url('./game/background/" + path1 + "'); -fx-background-repeat: stretch; -fx-background-size: " + width + " " + height +";");
        return path1;
    }

}
