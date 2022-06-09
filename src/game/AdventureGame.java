package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gameobjects.Actor;
import gameobjects.Dialogue;
import gameobjects.Room;
import gameobjects.Thing;
import globals.Direction;
import globals.HelpMethods;
import globals.ReadFile;
import globals.TextParser;


public class AdventureGame implements java.io.Serializable{

        //Creating the needed assets
        private ArrayList<Room> map; 
        private ArrayList<Thing> allItems;
        private ArrayList<String> roomNames;
        private ArrayList<String> allItemNames;
        private ArrayList<String> allNPCNames;
        private ArrayList<Thing> playerInventory;
        private ArrayList<String> playerAliases;
        private Actor player;

        //ArrayLists for dialogue interaction (for the upcoming dialogue line)
        private ArrayList<Integer> nextDialogueList = new ArrayList<>();
        //for the list of dialogue options the player can choose
        private ArrayList<Dialogue> listOfChoices = new ArrayList<>();

        //hashmap of the rooms and their visit values
        private Map<Room, Integer> playerVisits = new HashMap<>();

        //private int values for checking if a special action has been triggered
        //and also the variant of the special action (anger, fear, relation, or happiness)
        private int isSpecialActionTriggered = 0;

        //private int value for checking the number of times a special action has been triggered
        private int timesSpecialActionWasTriggered = 0;

        //list of recognized commands
        private List<String> commands = new ArrayList<>(Arrays.asList("examine", "move", "talk", "take", "drop", "use", "yes", "no", "quit"));
        private List<String> directions = new ArrayList<>(Arrays.asList("north", "south", "east", "west"));
        private List<String> miscNouns = new ArrayList<>(Arrays.asList("inventory", "i", "around"));

        //constructor
        public AdventureGame(){
            //creating the game map + a list of all scene names by reading the Rooms file
            this.map = ReadFile.createRooms(".\\..\\gamefiles\\Items.txt", ".\\..\\gamefiles\\Rooms.txt", ".\\..\\gamefiles\\Characters.txt");
            this.roomNames = getAllRoomNames();

            //getting the list of all NPC names
            this.allNPCNames = getAllNPCNames();

            //creating a list of all items + all item names by reading the Items file
            this.allItems = ReadFile.createItems(".\\..\\gamefiles\\Items.txt");
            this.allItemNames = HelpMethods.allThingNamesList(allItems);

            //creating the player inventory
            playerInventory = new ArrayList<Thing>();

            //setting the scenes and their visit values in a hashmap
            setRoomVisitMap();

            //player aliases
            playerAliases = new ArrayList<>(Arrays.asList("me", "myself"));

            //creating the player character
            player = new Actor("Vega", "Not worth getting into my backstory.", "Really, nothing noteworthy here.", 
                                map.get(0), playerAliases, playerInventory, false, "");

        }

        //ABOUT/HELP SCREENS
        //reading and printing the about file
        private void showAbout(){
            ReadFile.printFile(".\\..\\gamefiles\\About.txt");
        }

        //reading and printing the commands file
        private void showCommands(){
            ReadFile.printFile(".\\..\\gamefiles\\Commands.txt");

        }


        //DIALOGUE---------------------------------------------------------------------------------
        //method for starting a dialogue interaction
        public void startDialogue(String actorName, String fileName){
            //signals the player is in an active dialogue
            getPlayer().setIfActorIsInDialogue(true);
            //keeps processing until the dialogue is finished
            while(getPlayer().isActorInDialogue()){
                //setting the intended actor's name as the player's inDialogueWith attribute
                getPlayer().setWhoActorInDialogueWith(actorName);
                //printing the active dialogue NPC's description
                System.out.println(getActiveDialogueNPC().getDescription());
                //calling the method to process the dialogue interaction
                processActorScene(1, fileName, getActiveDialogueNPC());
            }
        }

        //method for ending a dialogue interaction
        public void endDialogue(){
            //signals the player has ended a dialogue interaction
            getPlayer().setIfActorIsInDialogue(false);
            //signals the room's dialogue interaction is completed
            getActiveRoom().setIfDialogueIsFinished(true);
        }

        //method for setting a dialogue's possible actor attribute changes
        private void addDialogueAttributeChanges(Dialogue aDialogue, ArrayList<String> dialogueLine){
            //getting the attribute changes by splitting the dialogue line
            ArrayList<String> attributeChanges = ReadFile.splitLineByCommas(dialogueLine, 2);
            //setting the respective attribute changes to a variable
            BigDecimal angerChange = new BigDecimal(attributeChanges.get(0));
            BigDecimal fearChange = new BigDecimal(attributeChanges.get(1));
            BigDecimal relationChange = new BigDecimal(attributeChanges.get(2));
            BigDecimal happinessChange = new BigDecimal(attributeChanges.get(3));
            //setting the dialogue's respective attribute changes
            aDialogue.setAngerChange(angerChange);
            aDialogue.setFearChange(fearChange);
            aDialogue.setRelationChange(relationChange);
            aDialogue.setHappinessChange(happinessChange);
            
        }

        //method for processing the actor attribute changes
        private void processAttributeChange(int dialogueOptionIndex){
            getActiveDialogueNPC().calculateAllAttributes(getADialogueOption(dialogueOptionIndex).getAngerChange(), 
                                                    getADialogueOption(dialogueOptionIndex).getFearChange(), 
                                                    getADialogueOption(dialogueOptionIndex).getRelationChange(), 
                                                    getADialogueOption(dialogueOptionIndex).getHappinessChange());
        }
        

        //method for returning the list containing the next lines of dialogue
        public ArrayList<Integer> getNextDialogueList(){
            return this.nextDialogueList;
        }
        
        //recursive method for parsing and getting the dialogue
        public ArrayList<Dialogue> getDialogues(String aLine) throws NumberFormatException, IOException{
            //splitting the dialogue line by the @ symbol
            ArrayList<String> splitDialogue = ReadFile.splitLineByAtSymbol(aLine);
            //splitting the last index of the line to get the linked dialogue
            ArrayList<String> nextDialogueLine = ReadFile.splitLineByCommas(splitDialogue, -1);
            //creating a new dialogue object
            Dialogue myDialogue = new Dialogue();
            //setting the linked dialogue to the nextDialogueLine attribute
            myDialogue.setNextDialogueLine(nextDialogueLine);
            //setting the dialogue text to be shown to the dialogueText attribute
            myDialogue.setDialogueText(splitDialogue.get(1));
            
            //checking if the next linked dialogue is the last one (has a value of -1)
            if(myDialogue.getFirstNextDialogue() != -1){
                //checking if the dialogue is a text or an option
                if(splitDialogue.get(0).equals("text")){
                    //clearing the nextDialogueList
                    nextDialogueList.clear();
                    //printing the dialogue text
                    System.out.println(myDialogue.getDialogueText());
                    System.out.println();
                    //finding the next linked dialogue line in the text file
                    for(String number : myDialogue.getNextDialogueLine()){
                        String nextLine = ReadFile.getSpecificLine(Integer.parseInt(number.trim()), ".\\..\\gamefiles\\Scene.txt");
                        getDialogues(nextLine);//calling the method again
                    }
                }
                else{
                    //if it's a dialogue option, adds the possible attribute changes to the dialogue object
                    addDialogueAttributeChanges(myDialogue, splitDialogue);
                    //adding the next linked dialogue
                    nextDialogueList.add(myDialogue.getFirstNextDialogue());
                    //printing out the dialogue option with their number choice
                    System.out.println(nextDialogueList.size() + ". " + myDialogue.getDialogueText());
                    //adding the dialogue object to the list of choices
                    listOfChoices.add(myDialogue);
                }
            }else{
                //if the next linked dialogue isn't the last, clears the nextDialogueList
                nextDialogueList.clear();
                //prints out the dialogue text
                System.out.println(myDialogue.getDialogueText());
            }
            return listOfChoices;
     
        }

        //recursive method to process a dialogue interaction
        public void processActorScene(int count, String fileName, Actor anActor){
            try{
                List<String> outputs = new ArrayList<>();
                String output;
                BufferedReader userReader = new BufferedReader(new InputStreamReader(System.in));
                String nextLine = ReadFile.getSpecificLine(count, fileName);
                listOfChoices = getDialogues(nextLine);
                System.out.println();
                //checking if a special action was triggered
                //a special action can only be triggered once
                isSpecialActionTriggered = checkForActorSpecialActions(anActor); //updates the isSpecialActionTriggered var if triggered
                //if a special action hasn't been triggered, checks for it
                if(timesSpecialActionWasTriggered == 0){
                    switch(isSpecialActionTriggered){
                        //anger special action
                        case 1:
                            processActorSpecialActions(1, anActor);
                            timesSpecialActionWasTriggered = 1;
                            break;
                        //fear special action
                        case 2:
                            processActorSpecialActions(2, anActor);
                            timesSpecialActionWasTriggered = 1;
                            break;
                        //relation special action
                        case 3:
                            processActorSpecialActions(3, anActor);
                            timesSpecialActionWasTriggered = 1;
                            break;
                        //happiness special action
                        case 4:
                            processActorSpecialActions(4, anActor);
                            timesSpecialActionWasTriggered = 1;
                            break;
                        default:
                            break;
                    }
                }
                //if the list of the next dialogue is empty, ends the dialogue
                if(this.getNextDialogueList().isEmpty() || !getPlayer().isActorInDialogue()){
                    endDialogue();
                    System.out.println("This is the end of the " + HelpMethods.capitalizeString(anActor.getName()) + 
                                        " Dialogue.\nYou are now free to roam around the map. Have fun!");
                    timesSpecialActionWasTriggered = 0;
                }
                //next dialogue is still found, continues the interaction
                else{
                    //asking for user input
                    System.out.print("> ");
                    String choice = userReader.readLine();
                    outputs = TextParser.processInput(choice); //parsing the user input
                    output = runCommand(outputs);  //running the command
                    if(!outputs.isEmpty()){  //if the initial outputs isn't empty, continues to run
                        //checks if the input was a numeric choice (dialogue choice)
                        if(HelpMethods.isNumeric(output)){ 
                            count = Integer.parseInt(output);
                            processActorScene(count, fileName, anActor);
                        }
                        //handling input that's not a dialogue choice (movement, examining, etc)
                        else{
                            System.out.println(output);
                            processActorScene(count, fileName, anActor);
                        }
                    }else{ //handling empty/unrecognized inputs
                        System.out.println(output);
                        processActorScene(count, fileName, anActor);
                    }
                }
            } catch (IOException e){
                System.out.println("File could not be accessed, try again!");
            }
        }

        //NPC SPECIAL ACTIONS
        //EDELMAR
        
        //method for checking if an NPC's attribute trigger has been reached
        public int checkForActorSpecialActions(Actor anActor){
            //comparing an actor's current attributes to their respective attribute triggers
            int compareAnger = anActor.getAngerTrigger().compareTo(anActor.getAnger());
            int compareFear = anActor.getFearTrigger().compareTo(anActor.getFear());
            int compareRelation = anActor.getRelationTrigger().compareTo(anActor.getRelation());
            int compareHappiness = anActor.getHappinessTrigger().compareTo(anActor.getHappiness());

            //check if any of the current atrributes qualify for a special action
            
            if(compareAnger == 0|| compareAnger == -1){ //checks if the current anger >= angerTrigger
                isSpecialActionTriggered = 1;
            }else if(compareFear == 0 || compareFear == 1){ //checks if current fear <= fearTrigger
                isSpecialActionTriggered = 2;
            }
            else if(compareRelation == 0 || compareRelation == -1){ //checks if current relation >= relationTrigger
                isSpecialActionTriggered = 3;
            }
            else if(compareHappiness == 0 || compareHappiness == -1){//checks if current happiness >= happinessTrigger
                isSpecialActionTriggered = 4;
            }
            else{ //if no attributes qualify, return 0
                isSpecialActionTriggered = 0;
            }
            return isSpecialActionTriggered;
        }

        //method for processing what happens if you reach Edelmar's special action trigger values
        private void processActorSpecialActions(int typeOfSpecialAction, Actor anActor){
            String actorName = anActor.getName();
            //stating to the player that a special action has been triggered
            String msg = "One of " + HelpMethods.capitalizeString(actorName) + " Special Actions has been triggered.\n\n";
            //anger trigger value reached
            if(typeOfSpecialAction == 1){
                endDialogue();
                msg += ReadFile.fileToString(".\\..\\gamefiles\\NPCs\\"+ actorName + "\\AngerSA.txt");
                moveActorTo(getPlayer(), this.map.get(0));
            } 
            //fear trigger value reached
            else if(typeOfSpecialAction == 2){
                //el talks about your love life, and how it's been deteriorating with each new paramour
                msg += ReadFile.fileToString(".\\..\\gamefiles\\NPCs\\"+ actorName + "\\FearSA.txt");

            }//relation trigger value reached    
            else if(typeOfSpecialAction == 3){
                //el gifts you an old dagger (deer catcher), probably owned by his family
                msg += ReadFile.fileToString(".\\..\\gamefiles\\NPCs\\"+ actorName + "\\RelationSA.txt");
                transferItem(HelpMethods.thisThing("hunting dagger", allItems), getActiveDialogueNPC().getThings(), getPlayer().getThings());
            }else if(typeOfSpecialAction == 4){
                //el asks you about esther
                msg += ReadFile.fileToString(".\\..\\gamefiles\\NPCs\\"+ actorName + "\\HappinessSA.txt");
            }
            System.out.println(msg);
        }

        //Methods to run the intended commands

        //EXAMINING-----------------------------------------------------------------------
        public String examineItem(String itemName){
            String msg = "";
            Thing aRoomItem = HelpMethods.thisThing(itemName, getActiveRoom().getThings());
            Thing anInventoryItem = HelpMethods.thisThing(itemName, getPlayer().getThings());

            //flags for whether a roomItem or an InventoryItem is found
            boolean roomItemFound = true;
            boolean inventoryItemFound = true;

            //checking inventory
            if((itemName.equals("inventory") || itemName.equals("i"))){ // check the i command
                showInventory(getPlayer());
                return msg;
            }

            //if the player enters an itemName that equates to the player character themselves
            else if(playerAliases.contains(itemName) || itemName.equals(getPlayer().getName())){
                msg = getPlayer().getExamination();
                return msg;
            }
            
            //checking if the itemName matches with any NPCs in the game
            else if(allNPCNames.contains(itemName)){ 
                if(getPlayer().isActorInDialogue()){
                    msg += getActiveDialogueNPC().getExamineResponse() + "\n\n";
                    msg += getActiveDialogueNPC().getExamination();
                    return msg;
                }

                if(getActiveRoom().getNPC(itemName).equals(getActiveDialogueNPC())){
                    msg = getActiveRoom().getNPC(itemName).getExamination();
                }else{
                    msg = "They're not in your current area. How would you examine them?\n";
                }
                return msg;
            }

            //examining the active scene
            else if(itemName.equals("around")){
                msg = getActiveRoom().getExamination();
                return msg;
            }

            //checking if the desired item is an inventory item
            if(anInventoryItem == null){
                msg = "There's nothing named " + itemName + " in your inventory.\n";
                inventoryItemFound = false;
            }

            //checking if the desired item is an item in the room
            if(aRoomItem == null){
                msg = "There's nothing named " + itemName + " in this location.\n";
                roomItemFound = false;
    
            }

            //if the item in question is in an NPC's inventory
            if(getActiveDialogueNPC().getThings().contains(HelpMethods.thisThing(itemName, getActiveDialogueNPC().getThings()))){
                msg = "You can't really get a closer look.\n";
  
            }

            //found an item in the inventory
            else if (!roomItemFound && inventoryItemFound && !itemName.equals("around")){
                msg = anInventoryItem.getDescription();
            }

            //found an item in the room
            else if(roomItemFound && !inventoryItemFound && !itemName.equals("around")){
                msg = aRoomItem.getDescription();
            }

            //adding an extra NPC response if the player is in dialogue
            if(getPlayer().isActorInDialogue()){
                getActiveDialogueNPC().calculateAnger(BigDecimal.ONE);
                msg += getActiveDialogueNPC().getExamineResponse() + "\n\n" ;
            }
            
            return msg;
        }
        //EXAMINE END----------------------------------------------------------------------

        //TAKING AND DROPPING--------------------------------------------------------------

        //method to transfer items from one list to another
        private void transferItem(Thing aThing, ArrayList<Thing> fromList, ArrayList<Thing> toList){
            fromList.remove(aThing);
            toList.add(aThing);
        }

        //method to show a character's inventory
        private void showInventory(Actor anActor){
            System.out.println("--------------------------------------------------INVENTORY--------------------------------------------------\n");
            System.out.println(HelpMethods.describeThings(anActor.getThings()));
            System.out.println("\n-------------------------------------------------------------------------------------------------------------\n");
        }

        //method to take an item and put it in the player's inventory
        public String takeItem(String itemName){
            String msg = "";
            boolean takeSuccess = false; //checks if the take action was successful
            Thing t = HelpMethods.thisThing(itemName, getActiveRoom().getThings());
            if (itemName.equals("")){ //checks if the itemName is an empty string
                itemName = "nothing";
            }if(t == null){ //checks if the itemName doesn't exist
                msg = "There's nothing named " + itemName + " in here.\n";
            }else{
                if(t.isThingPickupable() == true){ //checks if the item is able to be taken
                    transferItem(t, player.getRoom().getThings(), player.getThings());
                    msg =  HelpMethods.capitalizeString(itemName) + " has been taken.";
                    takeSuccess = true;

                }else{ //item cannot be picked up
                    msg = "I don't think you can pick that up.\n";
                }
            }

            //checking if the player is in a dialogue
            if(getPlayer().isActorInDialogue() && takeSuccess){
                //adding 1 point of anger for every time they do this action
                getActiveDialogueNPC().calculateAnger(BigDecimal.ONE);
                msg += getActiveDialogueNPC().getTakeResponse() + "\n" ;
            }

            return msg;
        }

        //method to drop an item from the player's inventory
        public String dropItem(String itemName){
            String msg = "";
            boolean dropSuccess = false; //checks if the drop action was successful
            Thing t = HelpMethods.thisThing(itemName, getPlayer().getThings());
            if (itemName.equals("")){//checks if the itemName is an empty string
                msg = "You have to *name* the object. I can't read your mind.";
            }else if(t == null){//checks if the itemName exists
                msg = "There's nothing named " + itemName + " in your inventory.";
            }else{
                if(t.isThingKey() == false){ //checks if the item is a key item (not droppable)
                    transferItem(t, player.getThings(), player.getRoom().getThings());
                    msg =  "\n" + HelpMethods.capitalizeString(itemName) + " has been dropped.";
                    dropSuccess = true;
                }else{ //key item, item cannot be dropped
                    msg = "I don't think you should drop that.";
                }
            }
            //checking if the player is in a dialogue and if the drop was a success
            if(getPlayer().isActorInDialogue() && dropSuccess){

                //add 1 point of anger if the dropped item doesn't originate from the scene
                if(t.getLocation() == getActiveRoom().getLocation()){
                    getActiveDialogueNPC().calculateAnger(BigDecimal.ONE.negate());
                }
                //negate 1 point of anger if the dropped item originates from the scene
                else{
                    getActiveDialogueNPC().calculateAnger(BigDecimal.ONE);
                }
                msg += getActiveDialogueNPC().getDropResponse() + "\n" ;
            }

            return msg;
        }

        //TAKING AND DROPPING END--------------------------------------------------------------
        
        //MOVEMENT-----------------------------------------------------------------------------
        //Methods for moving the player around the map
        private void goN() {
            moveHandler(movePlayerTo(Direction.NORTH));
        }
    
        private void goS() {
            moveHandler(movePlayerTo(Direction.SOUTH));
        }
    
        private void goW() {
            moveHandler(movePlayerTo(Direction.WEST));
        }
    
        private void goE() {
            moveHandler(movePlayerTo(Direction.EAST));
        }

        //method to move an Actor (NPC, PC) to a room
        public int moveTo(Actor anActor, Direction aDirection){
            Room activeRoom = anActor.getRoom();
            int exit;
            switch(aDirection){
                case NORTH:
                    exit = activeRoom.getNorth();
                    break;
                case SOUTH:
                    exit = activeRoom.getSouth();
                    break;
                case EAST:
                    exit = activeRoom.getEast();
                    break;
                case WEST:
                    exit = activeRoom.getWest();
                    break;
                default:
                    exit = Direction.NOEXIT; 
                    break;
            }
            if (exit != Direction.NOEXIT && !anActor.isActorInDialogue()){
                moveActorTo(anActor, map.get(exit));
            }
            return exit;
        }
        
        //method to try a room/location change
        //returns a special message if unsuccessful (NOEXIT)
        //returns the scene name + description if successful
        private void moveHandler(int roomNumber) {      
            String msg;

            //if the character is in a dialogue
            if(getPlayer().isActorInDialogue()){
                getActiveDialogueNPC().calculateAnger(BigDecimal.ONE);
                msg = "You're currently talking to someone, finish up first.\n\n";
                msg += getActiveDialogueNPC().getMoveResponse() + "\n" ;

            }
            //if no exits were found
            else if (roomNumber == Direction.NOEXIT) {
                msg = "You can't travel that way, sorry.\n";
            }else{
                //move is a success, exit was found
                getPlayer().setLocation(roomNumber);
                getPlayer().addVisit(getActiveRoom()); //increments visit value in the hashmap by 1
                msg = "You are now in " + getActiveRoom().getName() + ".\n " + '\n'+ getActiveRoom().getDescription();
            }
            System.out.print(msg);
        }


        //method to call the moveTo method for an Actor (NPC, PC)
        public void moveActorTo(Actor anActor, Room aRoom) {
            anActor.setRoom(aRoom);
        }

        //method to call the moveTo method for the player character (PC)
        public int movePlayerTo(Direction dir){
            return moveTo(player, dir);
            
        }

        //MOVEMENT END--------------------------------------------------------------------------

        //SETTERS AND GETTERS-------------------------------------------------------------------------------
        //method to return the player object
        public Actor getPlayer() {
            return player;
        }

        //method to return the active room
        public Room getActiveRoom(){
            return getPlayer().getRoom();
        }

        //method for getting the NPC that the player character is interacting with
        public Actor getActiveDialogueNPC(){
            return getActiveRoom().getNPC(getPlayer().getWhoActorInDialogueWith());
        }

        //method to return each of the room's names
        public ArrayList<String> getAllRoomNames(){
            ArrayList<String> allRoomNames = new ArrayList<>();
            for(Room s : this.map){
                allRoomNames.add(s.getName().toLowerCase());
            }
            return allRoomNames;
        }

        //method to return each of the NPC names
        public ArrayList<String> getAllNPCNames(){
            ArrayList<String> allNPCNames = new ArrayList<>();
            for(Room s : this.map){
                allNPCNames.addAll(s.getNPCNames());
            }
            return allNPCNames;
        }

        //method to return one specific dialogue that is an option for the player
        public Dialogue getADialogueOption(int index){
            return this.listOfChoices.get(index);
        }

        //method to assign the Rooms a visit value in the hashmap
        public void setRoomVisitMap(){
            for(Room s : this.map){
                playerVisits.put(s, 0);
            }
        }


        //SETTERS AND GETTERS END-------------------------------------------------------------------------

        //PROCESSING INPUT--------------------------------------------------------------------------------

        
        //method for processing one word commands
        public String processVerb(List<String> wordList){
            String verb;
            String msg = "";
            verb = wordList.get(0);

            //if the command is quit
            if(verb.equals("quit")){
                msg += "Thank you for playing the demo!";
            }
            //if the command is a valid number choice (for dialogue choices)
            else if(getPlayer().isActorInDialogue() && TextParser.isNumberChoiceValid(verb)){
                if(verb.equals("1") && getPlayer().isActorInDialogue()){
                    msg = String.valueOf(this.getNextDialogueList().get(0));
                    //changing the NPC according to the selected dialogue option's attribute changes
                    processAttributeChange(0);
                    listOfChoices.clear(); //resetting the list of dialogue choices
                    System.out.println();

                }else if(verb.equals("2") && getPlayer().isActorInDialogue()){
                    msg = String.valueOf(this.getNextDialogueList().get(1));
                    //changing the NPC according to the selected dialogue option's attribute changes
                    processAttributeChange(1);
                    listOfChoices.clear(); //resetting the list of dialogue choices
                    System.out.println();
                    
                }else if(this.getNextDialogueList().size() == 3 && verb.equals("3") && getPlayer().isActorInDialogue()){
                    msg = String.valueOf(this.getNextDialogueList().get(2));
                    //changing the NPC according to the selected dialogue option's attribute changes
                    processAttributeChange(2);
                    listOfChoices.clear(); //resetting the list of dialogue choices
                    System.out.println();
                }
            }
            //if the command was commands (prints the command list)
            else if(verb.equals("commands")){
                showCommands();
            }
            //if the command was about (prints the text talking about the game)
            else if(verb.equals("about")){
                showAbout();
            }
            //if the command wasn't able to be recognized
            else{
                msg = "That's not a recognized action.\n";
                return msg;
            }
            return msg;
        }

        //method for processing two word commands
        public String processVerbNoun(List<String> wordlist){
            String verb;
            String noun;
            String msg = "";
            boolean error = false;
            verb = wordlist.get(0);
            noun = wordlist.get(1);

            //if the command isn't found in the commands list
            if(!commands.contains(verb)){
                msg = "You either made a typo, or " + verb +" isn't a recognized action.\n";
                error = true;
            }
            //if the noun doesn't exist
            if(!allItemNames.contains(noun) && !directions.contains(noun) && !miscNouns.contains(noun) && !allNPCNames.contains(noun)){
                msg += "You either made a typo, or " + noun + " isn't a recognized noun.\n";
                error = true;
            }
            //if the previous checks were passed, checks for which action the player wants to do
            if(!error){
                //processing move actions
                if(verb.equals("move") && directions.contains(noun)){ //processing move commands
                    switch(noun){
                        case "north":
                            goN();
                            break;
                        case "south":
                            goS();
                            break;
                        case "east":
                            goE();
                            break;
                        case "west":
                            goW();
                            break;
                        default:
                            msg = "You either made a typo, or " + noun + " isn't a recognized direction.\n";
                            break;
                    }
                }
                //processing examine actions
                else if(verb.equals("examine")){ //processing examine commands
                    msg = examineItem(noun);
                }
                //processing take actions
                else if(verb.equals("take")){ //processing take commands
                    msg = takeItem(noun);
    
                }
                //processing drop actions
                else if(verb.equals("drop")){ //processing drop commands
                    msg = dropItem(noun);
    
                }
            }

            return msg;
        }

        //method to call the processVerbNoun method and run the player's intended command
        public String runCommand(List<String> inputstr) {
            String s;  
            if(inputstr.size() == 2){
                s = processVerbNoun(inputstr);
            }else if(inputstr.size() == 1){
                s = processVerb(inputstr);
            }else{
                s = "That's not a recognized action.\n";
            }    
            
            return s;
        }

        //PROCESSING INPUT END-------------------------------------------------------------------------

}