package gameobjects;

import java.util.ArrayList;

public class ThingHolder extends Thing{

    private ArrayList<Thing> things = new ArrayList<>();

    //constructor for rooms
    public ThingHolder(String aName, String aDescription, String anExamination, ArrayList<Thing> tl){
        super(aName, aDescription, anExamination);
        this.things = tl;
    }

    //constructor for the player
    public ThingHolder(String aName, String aDescription, String anExamination, ArrayList<String> aliases, ArrayList<Thing> tl){
        super(aName, aDescription, anExamination, aliases);
        this.things = tl;
    }

    //constructor for NPCs
    public ThingHolder(String aName, String aDescription, String anExamination,  int aLocation, ArrayList<String> aliases, ArrayList<Thing> tl){
        super(aName, aDescription, anExamination, aLocation, aliases);
        this.things = tl;
    }

    //setter
    public void setThings(ArrayList<Thing> things){
        this.things = things;
    }

    //getter
    public ArrayList<Thing> getThings(){
        return things;
    }
}
