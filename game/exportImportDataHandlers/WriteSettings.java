package game.exportImportDataHandlers;

import game.static_classes.GlobalVariables;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by BobrZlosyn on 14.08.2016.
 */
public class WriteSettings {


    public static void writeSettings(){
        createFolder("settings");

        File file = new File("settings/config.cfg");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            writeSound(bw);
            writeServerInformation(bw);

        }catch (Exception e){
            System.err.println("chyba ve vytvareni nastaveni");
        }
    }

    private static void createFolder(String pathToFolder){
        File dir = new File(pathToFolder);
        if(!dir.exists()){
            dir.mkdir();
        }
    }
/*
    private static void writeLanguage(BufferedWriter bw) throws IOException {
        bw.write("language=" + SettingsValues.locale);
        bw.newLine();
    }
*/
    private static void writeSound(BufferedWriter bw) throws IOException{
        bw.write("sound=" + GlobalVariables.volumeSound.get());
        bw.newLine();
    }

    private static void writeServerInformation(BufferedWriter bw) throws IOException{
        bw.write("server=" + GlobalVariables.serverIPAdress.get());
        bw.newLine();
        bw.write("port=" + GlobalVariables.serverPort.get());
        bw.newLine();
        bw.write("id=" + GlobalVariables.playerID);
        bw.newLine();
    }

}
