package globals;

import java.util.ArrayList;
import gameobjects.Thing;


public final class HelpMethods{

    //private constructor to avoid instantiation
    private HelpMethods(){}

    //public method to capitalize the first letter of a String
    public static String capitalizeString(String lower){
        String output = lower.substring(0, 1).toUpperCase()  + lower.substring(1);
        return output;
    }

    
    //check if a String can be parsed into an int
    public static boolean isNumeric(String aString) {
        int intValue;
            
        if(aString == null || aString.equals("")) {
            return false;
        }
        
        try {
            intValue = Integer.parseInt(aString);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }

    //return the list + description of each item in the ThingList
    public static String describeThings(ArrayList<Thing> tl){
        String s = "";
        if(tl.size() == 0){
            s = "There's nothing here, it's empty.";
        }
        else{
            for(Thing i: tl){
                if(i.isThingKey()){
                    s += "KEY ITEM\n";
                }
                s += HelpMethods.capitalizeString(i.getName()) + ": \n" + i.getDescription() + '\n';
            }
        }
        return s;
    }

    //returns an ArrayList<String> of all item names in the game
    public static ArrayList<String> allThingNamesList(ArrayList<Thing> tl){
        ArrayList<String> allThingNames = new ArrayList<>();
        ArrayList<String> thingAliases = new ArrayList<>();
        for(Thing t : tl){
            allThingNames.add(t.getName());
            thingAliases = t.getAliases();
            for(String alias : thingAliases){
                allThingNames.add(alias);
            }
        }
        return allThingNames;
    }

    //return a specific thing from an ArrayList of Things
    public static Thing thisThing(String aName, ArrayList<Thing> tl){
        Thing something = null;
        String thingName = "";
        ArrayList<String> thingAliases;
        for (Thing i : tl){
            thingAliases = i.getAliases();
            thingName = i.getName().trim().toLowerCase();
            if(thingName.equals(aName) || thingAliases.contains(aName)){
                something = i;
            }
        }
        return something;
    }   
    
    


    
}
