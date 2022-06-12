import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import game.AdventureGame;
import globals.ReadFile;
import globals.TextParser;

public class Zwielicht {

    //private constructor to avoid instantiation
    private Zwielicht() {}

    //date and time formatter
    private static DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("E, MMM dd yyyy HH:mm:ss");
    
    //creating a new buffered reader for getting input
    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    //declaring the game object
    public static AdventureGame game;

    //method for checking if a save file exists 
    private static boolean checkSaveExists(String playerName, String saveName) {
        boolean exists;
        File file = new File(".\\..\\gamesaves\\" + playerName + "\\" + saveName + ".sav");
        if (file.exists()) {
            exists = true;
        }else{
            exists = false;
        }
        return exists;
        
    }
    //method for checking if a player folder exists
    private static boolean checkFolderExists(String playerName) {
        boolean folderExist;
        File folder = new File(".\\..\\gamesaves\\" + playerName);
        if (folder.exists()) {
            folderExist = true;
        }else{
            folderExist = false;
        }
        return folderExist;

    }

    //method for formatting string to lower case and trimmed
    private static String lowerTrim(String word){
        return word.trim().toLowerCase();
    }

    //method for getting the last time a file was modified
    private static String getLastTimeFileModified(String playerName, String saveName){
        //creating a new file object from the pathName
        File file = new File(".\\..\\gamesaves\\" + playerName + "\\" + saveName + ".sav");
        //storing the long variable from the lastModified method
        long lastModified = file.lastModified();
        //formatting the long into a date and time
        LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastModified), ZoneId.systemDefault());
        //using myDateFormatter to change it into a more readable output
        String formattedDate = date.format(myDateFormatter);
        return formattedDate;

    }

    //method for getting the current local date/time
    private static String currentDateTime(){
        LocalDateTime dateNow = LocalDateTime.now();
        return dateNow.format(myDateFormatter);

    }

    //method for saving the game
    private static void saveGame( String playerName, String saveName){
        try{
            //creating a file output stream object from the path
            FileOutputStream fos = new FileOutputStream(".\\..\\gamesaves\\" + playerName + "\\" + saveName + ".sav");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            //converting the game object into a byte stream (serializing)
            oos.writeObject(game);
            oos.flush();
            oos.close();
            //message to notify the player the save was successful
            System.out.println("\nYour progress was succesfully saved.\n");
        
        }catch (Exception e){
            //message to notify the player the save failed
            System.out.print("Serialization error. Save was unsuccessful.\n" + e.getClass() + ": " + e.getMessage());
        }
    }
    
    //method for loading a game save
    private static void loadGame( String playerName, String saveName){
        try{
            //creating a file input stream object from the path
            FileInputStream fis = new FileInputStream(".\\..\\gamesaves\\" + playerName + "\\" + saveName + ".sav");
            ObjectInputStream ois = new ObjectInputStream(fis);
            //reading the saved object (deserializing)
            game = (AdventureGame) ois.readObject();
            ois.close();
            //message to notify the player the load was successful
            System.out.println("\nGame successfully loaded.\n");
        
        }catch (Exception e){
            //message to notify the player thata the load failed
            System.out.print("Serialization error. Load was unsuccessful.\n" + e.getClass() + ": " + e.getMessage());
        }
    }

    //method ffor processing and confirming a save
    public static boolean processSave() throws IOException{
        boolean checkFile = false;
        String playerName;
        String saveName;
        List<String> response;

        //prompting the player for the player folder name
        System.out.print("Enter your player name: \n" + "> " );
        playerName = in.readLine();
        //prompting the player for confirmation
        System.out.println("\nWould you like to save this character?");
        System.out.println("Player Name: " + playerName);
        System.out.println(currentDateTime());
        System.out.print("> ");
        response = TextParser.parseInput(in.readLine());

        //checks if a valid response is given
        if(!response.isEmpty()){
            if(response.get(0).equals("yes")){
                //response is yes
                //prompting for save file name
                System.out.println("\nEnter the name of your save:");
                System.out.print("> ");
                saveName = lowerTrim(in.readLine());
                playerName = lowerTrim(playerName);
                //checking if a save already exists
                checkFile = checkSaveExists(playerName, saveName);
                ifSaveExists(playerName, saveName, checkFile);
            }else{
                //response is no
                System.out.println("Got it, save process cancelled.");
            }
        }else{
            //unrecognized option
            System.out.println("I can't understand that, save process has been cancelled.");
        }
        return checkFile;

    }

    //method for processing if similar save data already exists
    public static void ifSaveExists(String playerName, String saveName, boolean checkFile) throws IOException{
        List<String> response;
        if(checkFile){
            //prompting the player wants to overwrite an existing save
            System.out.println("\nA save file with the same name already exists. Overwrite it?");
            System.out.println("Player Name\t: " + playerName);
            System.out.println("Last Save Date\t: " + getLastTimeFileModified(playerName, saveName));
            System.out.print("> ");
            response = TextParser.parseInput(in.readLine());

            //checks if a valid response was given
            if(!response.isEmpty()){
                //save overwritten
                if(response.get(0).equals("yes")){
                    saveGame(playerName, saveName);
                }
                //save not overwritten
                else{
                    System.out.println("\nGot it, save process cancelled");
                }
            }else{
                //unrecognized response
                System.out.println("I can't understand that, save process has been cancelled.");
            }

        }else{
            //creating a new player folder
            File file = new File(".\\..\\gamesaves\\"+ playerName);
            file.mkdir();
            saveGame(playerName, saveName);
        }
    }

    //method for processing and confirming a save
    public static boolean processLoad() throws IOException{
        boolean checkFolder = false;
        boolean checkFile = false;
        String playerName;
        String saveName;
        List<String> response;

        //prompting the player for the player folder name
        System.out.print("Enter your player name: \n" + "> " );
        playerName = in.readLine();
        //checking if a player folder exists
        checkFolder = checkFolderExists(lowerTrim(playerName));
        if(!checkFolder){
            //no player folder with that name found
            System.out.println("There are no saved characters with that name. Please check if you've made a typo.\n");
        }else{
            //player folder is found
            //prompting for confirmation
            System.out.println("\nWould you like to load this character?");
            System.out.println("Player Name: " + playerName);
            System.out.print("> ");
            response = TextParser.parseInput(in.readLine());

            //checks if a valid response is given
            if(!response.isEmpty()){
                if(response.get(0).equals("yes")){
                    //response is yes
                    //prompting player for the save file name
                    System.out.println("\nEnter the name of your save:");
                    System.out.print("> ");
                    saveName = lowerTrim(in.readLine());
                    playerName = lowerTrim(playerName);
                    //checking if a save exists
                    checkFile = checkSaveExists(playerName, saveName);
                    ifLoadExists(playerName, saveName, checkFile);
                }else{
                    //response is no
                    System.out.println("Got it, load process cancelled.");
                }
            }else{
                //invalid response given
                System.out.println("I can't understand that, load process has been cancelled.");
            }
        }
        return checkFolder;
    }

    //method for processing if save data doesn't exist
    public static void ifLoadExists(String playerName, String saveName, boolean checkFile) throws IOException{
        List<String> response;
        if(!checkFile){
            //no save files found
            System.out.println("No save files were found with that name. Please check if you've made a typo.");
        }else{
            //prompting the player for confirmation
            System.out.println("Load this save file?");
            System.out.println("Player Name\t: " + playerName);
            System.out.println("Last Save Date\t: " + getLastTimeFileModified(playerName, saveName));
            System.out.print("> ");
            response = TextParser.parseInput(in.readLine());

            //checks if a valid response is given
            if(!response.isEmpty()){
                if(response.get(0).equals("yes")){
                    //response is yes
                    //calls the loadGame method
                    loadGame(playerName, saveName);
                }else{
                    //response is no
                    System.out.println("\nGot it, load process cancelled");
                }
            }else{
                //invalid response is given
                System.out.println("I can't understand that, load process has been cancelled.");
            }
        }
    }

    //DELETING A SAVE FILE
    //method for deleting a save file
    private static void deleteSave(String playerName, String saveName) {
        String path = ".\\..\\gamesaves\\" + playerName + "\\" + saveName + ".sav";
        File save = new File(path) ;
        //checks whether a save file was successfully deleted or not
        if(save.delete()){
            System.out.println("Save successfully deleted.");
        }else{
            System.out.println("Save could not be deleted.");
        }
    }

    //processing a delete command
    public static boolean processDelete() throws IOException{
        boolean checkFolder = false;
        boolean checkFile = false;
        String playerName;
        String saveName;
        List<String> response;
        
        //prompting the player for the player folder name
        System.out.print("Enter your player name: \n" + "> " );
        playerName = in.readLine();
        checkFolder = checkFolderExists(lowerTrim(playerName));
        //checking if a player folder exists
        if(!checkFolder){
            System.out.println("There are no saved characters with that name. Please check if you've made a typo.\n");
        }else{
            //player folder is found
            //prompting player for confirmation
            System.out.println("\nWould you like to delete this character?");
            System.out.println("Player Name: " + playerName);
            System.out.print("> ");
            response = TextParser.parseInput(in.readLine());

            //checks if a valid response is given
            if(!response.isEmpty()){
                if(response.get(0).equals("yes")){
                    //response is yes
                    //prompting player for the save name
                    System.out.println("\nEnter the name of your save:");
                    System.out.print("> ");
                    saveName = lowerTrim(in.readLine());
                    playerName = lowerTrim(playerName);
                    //checking if the save file exists
                    checkFile = checkSaveExists(playerName, saveName);
                    ifDeleteExists(playerName, saveName, checkFile);
                }else{
                    //response is no
                    System.out.println("Got it, delete process cancelled.");
                }
            }else{
                //invalid response is given
                System.out.println("I can't understand that, delete process has been cancelled.");
            }
        }
        return checkFolder;
    }

    //checking if the specified save file exists
    public static void ifDeleteExists(String playerName, String saveName, boolean checkFile) throws IOException{
        List<String> response;
        if(!checkFile){
            //no save file found
            System.out.println("No save files were found with that name. Please check if you've made a typo.");
        }else{
            //dave file found
            //prompting player for confirmation
            System.out.println("Delete this save file?");
            System.out.println("Player Name\t: " + playerName);
            System.out.println("Save Name\t: " + saveName);
            System.out.println("Last Save Date\t: " + getLastTimeFileModified(playerName, saveName));
            System.out.print("> ");
            response = TextParser.parseInput(in.readLine());
            //checks if a valid response was given
            if(!response.isEmpty()){
                if(response.get(0).equals("yes")){
                    //response is yes
                    deleteSave(playerName, saveName);
                }else{
                    //response is no
                    System.out.println("\nGot it, delete process cancelled");
                }
            }else{
                //invalid response given
                System.out.println("I can't understand that, delete process has been cancelled.");
            }
        }
    }

    //printing out the story intro
    public static void showIntro(){
        ReadFile.printFile(".\\..\\gamefiles\\Intro.txt");
    }

    // Main driver method
    public static void main(String[] args) throws IOException
    {
        showIntro();
        game = new AdventureGame();
        String input;
        String output;
        String checkQuit = "";
        List<String> outputs;

        do {
            output = "";
            System.out.print("> ");
            input = in.readLine();
            outputs = TextParser.processInput(input);
            System.out.println();
            
            //if the output is empty, returns 
            if(outputs.isEmpty()){
                output = "I can't recognize that, sorry.\n"; 

            }else{
                //checking which command is trying to be run
                checkQuit = outputs.get(0); 
                switch (outputs.get(0)) {
                    case "save":
                        processSave();
                        break;
                    case "load":
                        processLoad();
                        break;
                    case "delete":
                        processDelete();
                        break;
                    default:
                        output = game.runCommand(outputs);
                        break;
                //playing out the office scene
                }if(game.getPlayer().getLocation() == 1 
                    && game.getPlayer().getSpecificRoomVisits(game.getPlayer().getRoom()) == 1
                    && !game.getPlayer().getRoom().getIfDialogueIsFinished()){
                    //starts the dialogue    
                    game.startDialogue("edelmar", ".\\..\\gamefiles\\Scene.txt");
                }
                
            }
            //displaying the output
            System.out.println(output);

        } while (!"quit".equals(checkQuit));
      }

}