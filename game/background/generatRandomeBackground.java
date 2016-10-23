package game.background;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by BobrZlosyn on 26.09.2016.
 */
public class generatRandomeBackground {

    public generatRandomeBackground(){

    }

    public void findImages(){
        File file = new File("images");
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(file.listFiles()));
        files.forEach(file1 -> System.out.println(file1.toString()));
    }

}
