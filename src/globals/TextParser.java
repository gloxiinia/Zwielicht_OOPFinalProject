package globals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public final class TextParser {

    //private constructor to avoid instantiation
    private TextParser(){}

    //method to filter out any punctuation/special characters
    public static String FilterDelims(String input) {
        //setting the characters + white space as delimiters
        String delims = " \t,.:;?!\"'><()[]{}/|-_=+&*%$#@~`";
        List<String> wordList = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(input, delims);
        String t;

        //adding the leftover words into the wordList
        while (tokenizer.hasMoreTokens()) {
            t = tokenizer.nextToken();
            wordList.add(t);
        }

        //rejoining the wordList back into a String
        String listString = String.join(" ", wordList);
        return listString;
    }

    //method to parse/determine the command input by the user
    public static List<String> parseInput(String userInput){

        //converting the user input to lower case
        String command = userInput.toLowerCase();
        
        //boolean values to see if a specific command word has been found
        boolean foundExamineWords = false;
        boolean foundMoveWords = false;
        boolean foundTakeWords = false;
        boolean foundDropWords = false;


        //storing the user input into an ArrayList
        List<String> words = new ArrayList<>(Arrays.asList(command.split(" ")));

        //lists containing possible words for specific in-game commands or possible inputs
        List<String> examineActions = new ArrayList<>(Arrays.asList("look", "check", "inspect", "examine", "study"));
        List<String> moveActions = new ArrayList<>(Arrays.asList("move", "go", "walk", "travel"));
        List<String> takeActions = new ArrayList<>(Arrays.asList("take", "grab", "nab"));
        List<String> quitActions = new ArrayList<>(Arrays.asList("quit", "exit", "q"));
        List<String> directions = new ArrayList<>(Arrays.asList("north", "n", "south", "s", "east", "e", "west", "w"));
        List<String> yesActions = new ArrayList<>(Arrays.asList("yes", "correct", "y", "yeah", "yea", "yep", "mhm", "right", "true"));
        List<String> noActions = new ArrayList<>(Arrays.asList("no", "n", "nope", "wrong", "nuhuh", "nah", "incorrect", "false"));

        int remainingWordsindex = 0;
        String remainingWords;
        List<String> result = new ArrayList<>();

        if(words.size() > 0){

            //EXAMINE
            //Parsing words for the examine command
            if(words.get(0).equals("look") && words.get(1).equals("at")){
                remainingWordsindex = 2;
                foundExamineWords = true;
                
            }
            else if (examineActions.contains(words.get(0))) {
                remainingWordsindex = 1;
                foundExamineWords = true;
                
            }

            if(foundExamineWords == true){
                if (words.size() > remainingWordsindex){
                    remainingWords = "";
                    for(int i = remainingWordsindex; i < words.size(); i++){
                        remainingWords += words.get(i);

                        //for objects/nouns with more than one word (e.g. book of spells)
                        if (i < words.size() - 1){
                            remainingWords += " ";
                        }
                    }
                    command = "examine";
                    result.add(command);
                    result.add(remainingWords);
                    return result;
                } 
            }
            
            //Parsing words for checking inventory without examine command
            if(words.size()==1 && (words.get(0).equals("inventory") || words.get(0).equals("i"))){
                remainingWordsindex = 0;
                command = "examine";
                result.add(command);
                result.add("inventory");
                return result;
            }

            //MOVE
            //Parsing the words for the move command
            //Parsing words for one word commands for moving
            if(words.size()==1 && directions.contains(words.get(0))){
                remainingWordsindex = 0;
                foundMoveWords = true;
            }

            if(moveActions.contains(words.get(0)) && words.get(1).equals("to")){
                remainingWordsindex = 2;
                foundMoveWords = true;
                
            }
            else if (moveActions.contains(words.get(0))) {
                remainingWordsindex = 1;
                foundMoveWords = true;
                
            }


            if(foundMoveWords == true){
                if (words.size() > remainingWordsindex){
                    remainingWords = "";
                    for(int i = remainingWordsindex; i < words.size(); i++){
                        remainingWords += words.get(i);

                        //for objects/nouns with more than one word (e.g. book of spells)
                        if (i < words.size() - 1){
                            remainingWords += " ";
                        }

                    }
                    command = "move";
                    result.add(command);
                    result.add(remainingWords);
                    return result; 
                } 
            }

            //TAKE
            //Parsing the words for the take command
            if (takeActions.contains(words.get(0))) {
                remainingWordsindex = 1;
                foundTakeWords = true;
                
            }
            if(words.get(0).equals("pick") && words.get(1).equals("up")){
                remainingWordsindex = 2;
                foundTakeWords = true;
                
            }

            if(foundTakeWords == true){
                if (words.size() > remainingWordsindex){
                    remainingWords = "";
                    for(int i = remainingWordsindex; i < words.size(); i++){
                        remainingWords += words.get(i);
                        //for objects/nouns with more than one word (e.g. book of spells)
                        if (i < words.size() - 1){
                            remainingWords += " ";
                        }

                    }
                    command = "take";
                    result.add(command);
                    result.add(remainingWords);
                    return result;
                } 
            }

            //DROP
            //Parsing the words for the drop command
            if (words.get(0).equals("drop")) {
                remainingWordsindex = 1;
                foundDropWords = true;
                
            }
            if((words.get(0).equals("set") || words.get(0).equals("put")) && words.get(1).equals("down")){
                remainingWordsindex = 2;
                foundDropWords = true;
                
            }

            if(foundDropWords == true){
                if (words.size() > remainingWordsindex){
                    remainingWords = "";
                    for(int i = remainingWordsindex; i < words.size(); i++){
                        remainingWords += words.get(i);
                        //for objects/nouns with more than one word (e.g. book of spells)
                        if (i < words.size() - 1){
                            remainingWords += " ";
                        }
                    }
                    command = "drop";
                    result.add(command);
                    result.add(remainingWords);
                    return result;
                } 
            }

            //YES
            //Parsing the words for the yes command
            if (yesActions.contains(words.get(0))) {
                
                command = "yes";
                result.add(command);
                return result;
            }

            //NO
            //Parsing the words for the no command
            if (noActions.contains(words.get(0))) {
                
                command = "no";
                result.add(command);
                return result;
            }

            //SAVING
            //Parsing the words for the save game
            //save game, save progress, save 
            if(words.size() == 1 && (words.get(0).equals("save"))){
                command = "save";
                result.add(command);
                return result;
            }

            if(words.get(0).equals("save") && (words.get(1).equals("game") || 
                                                               words.get(1).equals("progress"))){
                command = "save";
                result.add(command);
                return result;
            }


            //LOADING
            //Parsing the words for the load game command
            //load game, load progress, continue save, load
            if(words.size() == 1 && (words.get(0).equals("load"))){
                command = "load";
                result.add(command);
                return result;
            }

            if(words.size() == 2 && words.get(0).equals("load") && (words.get(1).equals("progress") || 
                                                                                    words.get(1).equals("game") || 
                                                                                    words.get(1).equals("save"))){
                command = "load";
                result.add(command);
                return result;
            }

            if(words.size() == 2 &&  words.get(0).equals("continue") && (words.get(1).equals("progress") || 
                                                                                         words.get(1).equals("save") || 
                                                                                         words.get(1).equals("game"))){
                command = "load";
                result.add(command);
                return result;
            }

            //DELETING
            if(words.size() == 1 && (words.get(0).equals("delete"))){
                command = "delete";
                result.add(command);
                return result;
            }

            if(words.size() == 2 && words.get(0).equals("delete") && (words.get(1).equals("progress") || 
                                                                                      words.get(1).equals("game") || 
                                                                                      words.get(1).equals("save"))){
                command = "delete";
                result.add(command);
                return result;
            }
            
            //QUIT
            //Parsing the words for the quit command
            if(quitActions.contains(words.get(0))){
                command = "quit";
                result.add(command);
                return result;
                
            }

            //ABOUT and COMMANDS
            if(words.size() == 1 && words.get(0).equals("commands")){
                command = "commands";
                result.add(command);
                return result;
            }

            if(words.size() == 1 && words.get(0).equals("about")){
                command = "about";
                result.add(command);
                return result;
            }

            if(words.size() == 1 && words.get(0).equals("help")){
                command = "about";
                result.add(command);
                return result;
            }
        }

        return result; 
    }

    //method to check whether the inputted String is a valid number choice (for dialogue choices)
    public static boolean isNumberChoiceValid(String input){
        //listing the possible number inputs for dialogue choices (at max 3)
        List<String> possibleChoices = new ArrayList<>(Arrays.asList("1","2","3"));
        if(!possibleChoices.contains(input)){
            return false;
        }else{
            return true;
        }
    }

    //method to check if inputted string is a positive integer
    static boolean isInputPositiveInteger(String input) {
        String regex = "[0-9]+";
        return input.matches(regex);
    }

    //method to fully process/parse the inputted string
    //filters out delims, parses the command
    public static List<String> processInput(String input){
        List<String> parsed = new ArrayList<>();
        input = FilterDelims(input).trim();
        
        if(!isInputPositiveInteger(input)){
            parsed = parseInput(input);
        }else{
            parsed.add(input);
            return parsed;
        }
        return parsed;
    }

}
