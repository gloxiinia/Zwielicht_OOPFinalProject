package gameobjects;

import java.util.ArrayList;

public class Room extends ThingHolder{

    private int north, south, east, west;
    private boolean isDialogueFinished;
    private ArrayList<Actor> roomNPCs = new ArrayList<>();

    //constructor method for a room
    public Room(String aName, String aDescription, String anExamination, int n, int s, int e, int w, ArrayList<Thing> tl){
        super(aName, aDescription, anExamination, tl);
        this.north = n;
        this.south = s;
        this.east = e;
        this.west = w;
    }

    //setters
    //directional exits
    public void setNorth(int north){
        this.north = north;
    }

    public void setSouth(int south){
        this.south = north;
    }

    public void setEast(int east){
        this.east = east;
    }

    public void setWest(int west){
        this.west = west;
    }

    //setting the room NPCs as an ArrayList of actors
    public void setRoomNPCs(ArrayList<Actor> roomNPCs){
        this.roomNPCs = roomNPCs;
    }

    //setting whether the specific dialogue is finished or not
    public void setIfDialogueIsFinished(boolean check){
        this.isDialogueFinished = check;
    }


    //getters
    //getting the directional exits
    public int getNorth(){
        return this.north;
    }

    public int getSouth(){
        return this.south;
    }

    public int getEast(){
        return this.east;
    }
    
    public int getWest(){
        return this.west;
    }

    //getting the room's NPCs
    public ArrayList<Actor> getRoomNPCs(){
        return this.roomNPCs;
    }

    //getting a specific room NPC by searching their name
    public Actor getNPC(String aName){
        for(Actor npc : this.roomNPCs){
            if(aName.equals(npc.getName()) || npc.getAliases().contains(aName));
            return npc;
        }
        return null;
    }

    //getting all room NPC's names
    public ArrayList<String> getNPCNames(){
        ArrayList<String> roomNPCNames = new ArrayList<>();
        for(Actor npc : this.roomNPCs){
            roomNPCNames.add(npc.getName().toLowerCase());
            for(String alias : npc.getAliases()){
                roomNPCNames.add(alias.toLowerCase());
            }
        }
        return roomNPCNames;
    }

    //getting whether a dialogue is finished or not
    public boolean getIfDialogueIsFinished(){
        return this.isDialogueFinished;
    }

    
}
