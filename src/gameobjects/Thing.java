package gameobjects;

import java.util.ArrayList;

public class Thing implements java.io.Serializable{
    //Thing object that will be the superclass to all other game objects

    private String name, description, examination;
    private boolean isPickupable, isKeyItem;
    private int location;
    private ArrayList<String> aliases = new ArrayList<>();

    //constructor methods
    public Thing(String aName, String aDescription, String anExamination){
        this.name = aName;
        this.description = aDescription;
        this.examination = anExamination;
    }

    //constructor for the player character
    public Thing(String aName, String aDescription, String anExamination, ArrayList<String> aliases){
        this.aliases = aliases;
        this.name = aName;
        this.description = aDescription;
        this.examination = anExamination;
    }

    //constructor for NPCs
    public Thing(String aName, String aDescription, String anExamination, int aLocation, ArrayList<String> aliases){
        this.aliases = aliases;
        this.name = aName;
        this.description = aDescription;
        this.examination = anExamination;
        this.location = aLocation;
    }

    //constructor for Items
    public Thing(String aName, String aDescription, int aLocation, ArrayList<String> aliases, boolean isPickupable, boolean isKeyItem){
        this.aliases = aliases;
        this.name = aName;
        this.description = aDescription;
        this.isPickupable = isPickupable;
        this.isKeyItem = isKeyItem;
        this.location = aLocation;
    }

    //setters
    //setting the Thing name
    public void setName(String name){
        this.name = name;
    }
    //setting the Thing's location on the map
    public void setLocation(int aLocation){
        this.location = aLocation;
    }
    //setting the Thing's description text
    public void setDescription(String description){
        this.description = description;
    }
    //setting the Thing's examination text
    public void setExamination(String examination){
        this.examination = examination;
    }
    //adding the Thing's aliases
    public void addAlias(String alias){
        this.aliases.add(alias);
    }
    //setting if a Thing is able to be taken
    public void setThingPickupability(boolean isPickupable){
        this.isPickupable = isPickupable;
    }
    //setting if a Thing is a key item
    public void setThingKey(boolean isKeyItem){
        this.isKeyItem = isKeyItem;
    }

    //getters
    //returning all the attributes
    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public String getExamination(){
        return this.examination;
    }

    public ArrayList<String> getAliases(){
        return this.aliases;
    }

    public boolean isThingPickupable(){
        return this.isPickupable;
    }

    public boolean isThingKey(){
        return isKeyItem;
    }

    public int getLocation(){
        return this.location;
    }


}

