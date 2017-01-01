package game.exportImportDataHandlers;

import game.static_classes.GlobalVariables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by BobrZlosyn on 14.08.2016.
 */
public class LoadSettings {

    private static String separator = "=";
    private static int valueIndex = 1;

    public static void loadSettings(int numberOfTry){
        File file = new File("./settings/config.cfg");

        if(!file.exists() && file.isDirectory()){
            WriteSettings.writeSettings();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            loadServerInformation(br);

        }catch (Exception e){
            if(numberOfTry == 0){
                WriteSettings.writeSettings();
                loadSettings(1);
            }
        }
    }
/*
    private static void loadLanguage(BufferedReader br) throws IOException {
        String line = br.readLine();
        String language = line.split(separator)[valueIndex];
        LoadFXMLWithResourceBundle.resourceBundleExists(new Locale(language));
        SettingsValues.locale = language;
    }
*/
    private static void loadServerInformation(BufferedReader br) throws Exception{
        String line = br.readLine();
        GlobalVariables.volumeSound.set(Double.parseDouble(line.split(separator)[valueIndex]));

        line = br.readLine();
        String ipAddress = line.split(separator)[valueIndex];
        if(checkIPAdress(ipAddress)){
            GlobalVariables.serverIPAdress.set(ipAddress.trim());
        }


        line = br.readLine();
        String port = line.split(separator)[valueIndex];
        if(checkPort(port)){
            GlobalVariables.serverPort.set(port.trim());
        }

        try {
            line = br.readLine();
            int id = Integer.parseInt(line.split(separator)[valueIndex]);
            GlobalVariables.playerID = String.valueOf(id);
        }catch (Exception e){
            GlobalVariables.playerID = "0";
        }
    }

    public static boolean checkIPAdress(String ipAddress){

        String ipText = ipAddress.trim();
        String [] numbers = ipText.split("\\.");
        if ( numbers.length != 4) {
            return false;
        }

        try {
            for (int i = 0; i < numbers.length; i++){
                int number = Integer.parseInt(numbers[i]);
                if (number > 255 || number < 0) {
                    return false;
                }
            }

        }catch (Exception e){
            return false;
        }

        return true;
    }

    public static boolean checkPort(String portS){
        try {
            String port = portS.trim();
            int number = Integer.parseInt(port);
            if (number > 56666 || number < 0) {
                return false;
            }

        }catch (Exception e){
            return false;
        }

        return true;
    }

}
