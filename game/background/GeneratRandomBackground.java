package game.background;

import game.startUpMenu.CommonMenu;
import game.static_classes.GlobalVariables;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by BobrZlosyn on 26.09.2016.
 */
public class GeneratRandomBackground {
    private String portImage;
    private String welcomeImage;
    private String activeImage;
    private final String DEFAULT_PICTURE = "welcomeImage/uvod2.jpg";

    public GeneratRandomBackground(){
        welcomeImage = "";
        portImage = "";
        activeImage = "";
    }

    public void chooseImage(GridPane pane, String id){
        String folder = "images";
        ArrayList list = fillArrayWithImages(folder);

        int index;
        int size = list.size();
        try {
            index = Integer.parseInt(id) % size;
        }catch (Exception e){
            index = 0 + (int)(Math.random() * size);
        }
        loadImage(list, index, folder, pane);
    }

    public void resizeImage(GridPane pane, double newWidth, double newHeight){
        if(!activeImage.isEmpty()) {
            Image image = new Image(getClass().getResource(activeImage).toExternalForm(), newWidth, newHeight,false ,true);
            BackgroundImage myBI= new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                    BackgroundSize.DEFAULT);
            pane.setBackground(new Background(myBI));
        }
    }

    public void showSpacePort(GridPane pane){
        portImage = loadImagesInFolder(pane, "imagesSpacePort");
    }

    public void showWelcomeImage(GridPane pane){
        welcomeImage = loadImagesInFolder(pane, "welcomeImage");
        portImage = "";
    }


    private String loadImagesInFolder(GridPane pane, String folder){

        ArrayList list = fillArrayWithImages(folder);

        int size = list.size();
        int index = 0 + (int)(Math.random() * size);
        loadImage(list, index, folder, pane);
        return activeImage;
    }

    private void loadImage(ArrayList list, int index, String folder, Pane pane){
        activeImage = DEFAULT_PICTURE;
        if (!list.isEmpty()) {
            activeImage =  folder + "/" + list.get(index);
        }

        Platform.runLater(() -> {
            double width = pane.getWidth();
            double height = pane.getHeight();
            Image image = new Image(getClass().getResource(activeImage).toExternalForm(), width, height, false ,true);
            BackgroundImage myBI= new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
            pane.setBackground(new Background(myBI));
        });
    }

    private ArrayList fillArrayWithImages(String folder){
        ArrayList list = new ArrayList();
        FileSystem fileSystem = null;
        Stream<Path> walk = null;
        try {
            URI uri = getClass().getResource(folder).toURI();
            Path myPath;

            if (uri.getScheme().equals("jar")) {
                fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                myPath = fileSystem.getPath("/game/background/" + folder);
            } else {
                myPath = Paths.get(uri);
            }

            walk = Files.walk(myPath, 1);
            Iterator<Path> it = walk.iterator();

            it.next();

            while (it.hasNext()){
                list.add(it.next().getFileName());
            }

            fileSystem.close();
            walk.close();

        } catch (Exception e) {
            if (GlobalVariables.isNotEmpty(fileSystem)) {
                try {
                    fileSystem.close();
                } catch (IOException e1) {}
            }

            if (GlobalVariables.isNotEmpty(walk)) {
                walk.close();
            }
        }

        return list;
    }
}