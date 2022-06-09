package globals;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gameobjects.Actor;
import gameobjects.Room;
import gameobjects.Thing;



public final class ReadFile {

    //private constructor to avoid instanciation
    private ReadFile(){}

    //method for printing out a txt file in its entirety
    public static void printFile(String pathName){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(pathName));
            String line = reader.readLine();

            //while it isn't the end of the file
            while(!line.equals("ENDOFFILE")){
                System.out.println(line);
                line = reader.readLine();
            }

            reader.close();

        } catch (IOException e){
            System.out.println("File could not be accessed, try again!");
        }

    }

    //method for returning a txt file as a String
    public static String fileToString(String pathName){
        String msg = "";
        try{
            BufferedReader reader = new BufferedReader(new FileReader(pathName));
            String line = reader.readLine();
            while(!line.equals("ENDOFFILE")){ //while it isn't the end of the file
                msg += line + "\n";
                line = reader.readLine();
            }
            reader.close();
            return msg;
        } catch (IOException e){
            System.out.println("File could not be accessed, try again!");
        }
        return msg;
    }

    //method to read and create the game items
    public static ArrayList<Thing> createItems(String pathName){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(pathName));
            String line = reader.readLine();
            ArrayList<Thing> items = new ArrayList<Thing>();

            while(!line.equals("ENDOFFILE")){
                //item name
                String name = line;
                name = name.trim().toLowerCase();
                
                line = reader.readLine();

                //item location
                int location = Integer.parseInt(line);
                line = reader.readLine();

                //item aliases (other names for the item)
                ArrayList<String> aliases = new ArrayList<>(Arrays.asList(line.split(",")));
                line = reader.readLine();

                //is the item pickupable?
                boolean isPickupable = Boolean.parseBoolean(line);
                line = reader.readLine();

                //is the item a key item?
                boolean isKeyItem = Boolean.parseBoolean(line);
                line = reader.readLine();

                //item examination (detailed description when you examine a location)
                String description = "";
                while(!line.equals("END")){
                    description += line + '\n';
                    line = reader.readLine();
                }

                //creating a new Thing object
                Thing anItem = new Thing(name, description, location, aliases, isPickupable, isKeyItem);
                //adding the item to the list
                items.add(anItem);

                line = reader.readLine();
            }

            reader.close();
            return items;

        } catch (IOException e){
            System.out.println("File could not be accessed, try again!");
        }
        return null;
    }

    public static ArrayList<Actor> createRoomNPCs(Room aRoom, ArrayList<Thing> allItems, String charactersPathName){
        
        try{
            BufferedReader reader = new BufferedReader(new FileReader(charactersPathName));
            String line = reader.readLine();
            ArrayList<Actor> NPCs = new ArrayList<>();

            while(!line.equals("ENDOFFILE")){
                //actor name
                String name = line;
                name = name.trim().toLowerCase();
                
                line = reader.readLine();

                //actor location
                int location = Integer.parseInt(line);
                line = reader.readLine();

                //actor aliases (other names for the actor)
                ArrayList<String> aliases = new ArrayList<>(Arrays.asList(line.split(",")));
                line = reader.readLine();

                //actor inventory
                ArrayList<String> actorInventoryList = new ArrayList<>(Arrays.asList(line.split(",")));
                ArrayList<Thing> actorInventory = new ArrayList<Thing>();
                for(String itemName : actorInventoryList){
                    actorInventory.add(HelpMethods.thisThing(itemName, allItems));
                }
                line = reader.readLine();
                
                //actor description
                String description = "";
                while(!line.equals("ATTRIBUTES")){
                    description += line + '\n';
                    line = reader.readLine();
                }
                line = reader.readLine();

                //actor attributes
                ArrayList<String> attributesList = new ArrayList<>(Arrays.asList(line.split(",")));
                //base anger
                BigDecimal baseAnger = new BigDecimal(attributesList.get(0));
                //base fear
                BigDecimal baseFear = new BigDecimal(attributesList.get(1));
                //base relation
                BigDecimal baseRelation = new BigDecimal(attributesList.get(2));
                //base happiness
                BigDecimal baseHappiness = new BigDecimal(attributesList.get(3));

                line = reader.readLine();

                //actor tendencies
                ArrayList<String> tendenciesList = new ArrayList<>(Arrays.asList(line.split(",")));
                //angertendency (How prone they are to anger)
                BigDecimal angerTendency = new BigDecimal(tendenciesList.get(0));
                //fearTendency (How prone they are to fear)
                BigDecimal fearTendency = new BigDecimal(tendenciesList.get(1));
                //relationTendency (How prone they are to relation)
                BigDecimal relationTendency = new BigDecimal(tendenciesList.get(2));
                //happinessTendency (How prone they are to happiness)
                BigDecimal happinessTendency = new BigDecimal(tendenciesList.get(3));
                line = reader.readLine();

                //examine response when in dialogue
                ArrayList<String> dialogueResponseList = new ArrayList<>(Arrays.asList(line.split("@")));
                line = reader.readLine();

                //actor's special action attribute triggers
                ArrayList<String> specialActionTriggers = new ArrayList<>(Arrays.asList(line.split(",")));
                //base anger
                BigDecimal angerTrigger = new BigDecimal(specialActionTriggers.get(0));
                //base fear
                BigDecimal fearTrigger = new BigDecimal(specialActionTriggers.get(1));
                //base relation
                BigDecimal relationTrigger = new BigDecimal(specialActionTriggers.get(2));
                //base happiness
                BigDecimal happinessTrigger = new BigDecimal(specialActionTriggers.get(3));

                line = reader.readLine();

                //item examination (detailed description when you examine a location)
                String examination = "";
                while(!line.equals("END")){
                    examination += line + '\n';
                    line = reader.readLine();
                }

                //creating a new Actor object
                Actor anActor = new Actor(name, description, examination, aRoom, location, aliases, actorInventory, baseAnger, baseFear, baseRelation, baseHappiness, angerTendency,
                                    fearTendency, relationTendency, happinessTendency, dialogueResponseList, angerTrigger, fearTrigger, relationTrigger, happinessTrigger);
                //adding the Actor object to the list
                NPCs.add(anActor);

                line = reader.readLine();
            }

            reader.close();
            return NPCs;

        } catch (IOException e){
            System.out.println("File could not be accessed, try again!");
        }
        return null;
    }

    //method to read and create the game rooms
    public static ArrayList<Room> createRooms(String itemsPathName, String roomsPathName, String charactersPathName){

        //calling the createItems to create all in-game items
        ArrayList<Thing> allItems = createItems(itemsPathName);

        try{
            BufferedReader reader = new BufferedReader(new FileReader(roomsPathName));
            String line = reader.readLine();
            ArrayList<Room> rooms = new ArrayList<>();

            while(!line.equals("ENDOFFILE")){
                //location name
                String name = line.trim();
                line = reader.readLine();

                //location index
                String index = line.trim();
                int numIndex = Integer.parseInt(index);
                line = reader.readLine();

                //location exits
                ArrayList<String> neighbors = new ArrayList<>(Arrays.asList(line.split(",")));

                int n = Integer.parseInt(neighbors.get(0));
                int s = Integer.parseInt(neighbors.get(1));
                int e = Integer.parseInt(neighbors.get(2));
                int w = Integer.parseInt(neighbors.get(3));
                line = reader.readLine();
                

                //location description
                String description = "";
                while(!line.equals("EXAMINE")){
                    description += line + '\n';
                    line = reader.readLine();
                }
                line = reader.readLine();

                //location examination (detailed description when you examine a location)
                String examination = "";
                while(!line.equals("END")){
                    examination += line + '\n';
                    line = reader.readLine();
                }

                //finding the corresponding items to be put in the room
                ArrayList<Thing> roomItems = new ArrayList<Thing>();
                for(Thing i : allItems){
                    if(i.getLocation() == numIndex ){
                        roomItems.add(i);
                    }
                }

                //creating a new Room object
                Room aRoom = new Room(name, description, examination, n, s, e, w, roomItems);
                //creating the room NPCs with createRoomNPCs
                ArrayList<Actor> roomNPCs = createRoomNPCs(aRoom, allItems, charactersPathName);
                //adding the room NPCs to the Room object
                aRoom.setRoomNPCs(roomNPCs);
                //adding the Room object to the list
                rooms.add(aRoom);

                line = reader.readLine();

            }

            reader.close();
            return rooms;

        } catch (IOException e){
            System.out.println("File could not be accessed, try again!");
        }
        return null;

    }

    //getting a specific line in a txt file
    public static String getSpecificLine(int lineNum, String fileName) throws IOException{
        //the actual line number needs to be subtracted by one to retrieve the correct line
        String line = Files.readAllLines(Paths.get(fileName)).get(lineNum - 1); 
        return line;
    }

    //parsing a line in a txt by splitting it according to the regex (@)
    public static ArrayList<String> splitLineByAtSymbol(String line){
        ArrayList<String> splitLine = new ArrayList<>(Arrays.asList(line.split("@")));
        splitLine.add(splitLine.get(splitLine.size()-1).trim());
        return splitLine;
    }

    //splitting an ArrayList<String> by commas
    public static ArrayList<String> splitLineByCommas(ArrayList<String> line, int position){
        ArrayList<String> splitLine = new ArrayList<>();
        //for finding indexes from the back of the ArrayList
        if(position < 0){
            List<String> result = Arrays.asList(line.get(line.size() + position).split(","));
            splitLine.addAll(result);
        }
        //for finding indexes normally (counting from the beginning)
        else{
            List<String> result = Arrays.asList(line.get(position).split(","));
            splitLine.addAll(result);
            
        }
        return splitLine;
    }

}

