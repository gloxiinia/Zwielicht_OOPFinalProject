package gameobjects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Actor extends ThingHolder {

    private BigDecimal anger, fear, relation, happiness, 
                       angerTendency, fearTendency, relationTendency, happinessTendency,  
                       angerTrigger, fearTrigger, relationTrigger, happinessTrigger;

    private ArrayList<String> inDialogueResponses = new ArrayList<>();
    private String inDialogueWith;
    private Room room;
    private boolean isInDialogue;
    private Map<Room, Integer> roomVisits = new HashMap<Room, Integer>();
    
    //constructor for player
    public Actor(String aName, String aDescription, String anExamination, Room aRoom, ArrayList<String> aliases, ArrayList<Thing> tl, boolean isInDialogue, String inDialogueWith){
        super(aName, aDescription, anExamination, aliases, tl);
        this.room = aRoom;
        this.isInDialogue = isInDialogue;
        this.inDialogueWith = inDialogueWith;
    }
    
    //constructor for NPCs
    public Actor(String aName, String aDescription, String anExamination, Room aRoom, int location, ArrayList<String> aliases, ArrayList<Thing> tl, 
                    BigDecimal anger, BigDecimal fear, BigDecimal relation, BigDecimal happiness, 
                    BigDecimal angerTendency, BigDecimal fearTendency, BigDecimal relationTendency, BigDecimal happinessTendency, 
                    ArrayList<String> inDialogueResponses, BigDecimal angerTrigger, BigDecimal fearTrigger, BigDecimal relationTrigger, BigDecimal happinessTrigger){

        super(aName, aDescription, anExamination, location, aliases, tl);
        this.room = aRoom;
        this.anger = anger;
        this.fear = fear;
        this.relation = relation;
        this.happiness = happiness;
        this.angerTendency = angerTendency;
        this.fearTendency = fearTendency;
        this.relationTendency = relationTendency;
        this.happinessTendency = happinessTendency;
        this.inDialogueResponses = inDialogueResponses;
        this.angerTrigger = angerTrigger;
        this.fearTrigger = fearTrigger;
        this.relationTrigger = relationTrigger;
        this.happinessTrigger = happinessTrigger;

    }

    //setters
    public void setRoom(Room aRoom){
        this.room = aRoom;
    }
    //setting the actor's attributes
    public void setAnger(BigDecimal anger){
        this.anger = anger;
    }
    public void setFear(BigDecimal fear){
        this.fear = fear;
    }
    public void setRelation(BigDecimal relation){
        this.relation = relation;
    }
    public void setHappiness(BigDecimal happiness){
        this.happiness = happiness ;
    }

    //setting the actor's attribute tendencies (how prone are they to each attribute)
    public void setAngerTendency(BigDecimal angerTendency){
        this.angerTendency = angerTendency;
    }
    public void setFearTendency(BigDecimal fearTendency){
        this.fearTendency = fearTendency;
    }
    public void setRelationTendency(BigDecimal relationTendency){
        this.relationTendency = relationTendency;
    }
    public void setHappinessTendency(BigDecimal happinessTendency){
        this.happinessTendency = happinessTendency ;
    }

    //methods for adding to their attributes with a multiplier from their tendencies
    public void calculateAnger(BigDecimal angerChange){
        if(!(angerChange.compareTo(BigDecimal.ZERO) == 0)){
            //checking if the value is positive or not
            if(this.angerTendency.compareTo(BigDecimal.ZERO) < 0){
                this.anger = this.anger.subtract(angerChange.multiply(getAngerTendency()));
            }else{
                this.anger = this.anger.add(angerChange.multiply(getAngerTendency()));
            }
        }
    }
    public void calculateFear(BigDecimal fearChange){
        //checking if the value is positive or not
        if(!(fearChange.compareTo(BigDecimal.ZERO) == 0)){
            if(this.fearTendency.compareTo(BigDecimal.ZERO) < 0){
                this.fear = this.fear.subtract(fearChange.multiply(getFearTendency()));
            }else{
                this.fear = this.fear.add(fearChange.multiply(getFearTendency()));
            }
        }
    }
    public void calculateRelation(BigDecimal relationChange){
        //checking if the value is positive or not
        if(!(relationChange.compareTo(BigDecimal.ZERO) == 0)){
            if(this.relationTendency.compareTo(BigDecimal.ZERO) < 0){
                this.relation = this.relation.subtract(relationChange.multiply(getRelationTendency()));
            }else{
                this.relation = this.relation.add(relationChange.multiply(getRelationTendency()));
            }
        }
        
    }

    public void calculateHappiness(BigDecimal happinessChange){
        //checking if the value is positive or not
        if(!(happinessChange.compareTo(BigDecimal.ZERO) == 0)){
            if(this.happinessTendency.compareTo(BigDecimal.ZERO) < 0){
                this.happiness = this.happiness.subtract(happinessChange.multiply(getHappinessTendency()));
            }else{
                this.happiness = this.happiness.add(happinessChange.multiply(getHappinessTendency()));
            }
        }
    }

    //for calculating all attribute changes
    public void calculateAllAttributes(BigDecimal angerChange, BigDecimal fearChange, 
                                       BigDecimal relationChange, BigDecimal happinessChange){
        calculateAnger(angerChange);
        calculateFear(fearChange);
        calculateRelation(relationChange);
        calculateHappiness(happinessChange);
    }

    //setting the actor's special action trigger attributes
    public void setAngerTrigger(BigDecimal angerTrigger){
        this.angerTrigger = angerTrigger;
    }
    public void setFearTrigger(BigDecimal fearTrigger){
        this.fearTrigger = fearTrigger;
    }
    public void setRelationTrigger(BigDecimal relationTrigger){
        this.relationTrigger = relationTrigger;
    }
    public void setHappinessTrigger(BigDecimal happinessTrigger){
        this.happinessTrigger = happinessTrigger;
    }


    //setting whether an actor is in a dialogue
    public void setIfActorIsInDialogue(boolean isInDialogue){
        this.isInDialogue = isInDialogue;
    }
    //setting which other actor this actor is in a dialogue with
    public void setWhoActorInDialogueWith(String aName){
        this.inDialogueWith = aName;
    }
    //adding new rooms and their visiting number to the rooms and visits hashmap
    public void addRoomAndVisits(Room aRoom, int numVisits){
        this.roomVisits.put(aRoom, numVisits);
    }
    //add a visit (increment by 1) to the passed room's value
    public void addVisit(Room aRoom){
        this.roomVisits.merge(aRoom, 1, Integer::sum);
    }


    //getters
    //getting the room the actor is in
    public Room getRoom(){
        return this.room;
    }

    //getting the actor's attributes
    public BigDecimal getAnger(){
        return this.anger;
    }
    
    public BigDecimal getFear(){
        return this.fear;
    }
    
    public BigDecimal getRelation(){
        return this.relation;
    }
    
    public BigDecimal getHappiness(){
        return this.happiness;
    }

    //getting the actor's attribute tendencies
    public BigDecimal getAngerTendency() {
        return this.angerTendency;
    }
    public BigDecimal getFearTendency() {
        return this.fearTendency;
    }
    public BigDecimal getRelationTendency() {
        return this.relationTendency;
    }

    public BigDecimal getHappinessTendency(){
        return this.happinessTendency;
    }
    
    //getting the actor's special action trigger attributes
    public BigDecimal getAngerTrigger(){
        return this.angerTrigger;
    }

    public BigDecimal getFearTrigger(){
        return this.fearTrigger;
    }

    public BigDecimal getRelationTrigger(){
        return this.relationTrigger;
    }

    public BigDecimal getHappinessTrigger(){
        return this.happinessTrigger;
    }

    //getting whether an actor is currently in dialogue
    public boolean isActorInDialogue(){
        return this.isInDialogue;
    }

    //getting the actor's various responses when in dialogue
    public String getExamineResponse(){
        return this.inDialogueResponses.get(0);
    }

    public String getMoveResponse(){
        return this.inDialogueResponses.get(1);
    }

    public String getTakeResponse(){
        return this.inDialogueResponses.get(2);
    }

    public String getDropResponse(){
        return this.inDialogueResponses.get(3);
    }

    //get which actor this actor is engaged in dialogue with
    public String getWhoActorInDialogueWith(){
        return this.inDialogueWith;
    }

    //get how many times a specific room has been visited
    public int getSpecificRoomVisits(Room aRoom){
        for (Room i : this.roomVisits.keySet()) {
            if(i.equals(aRoom)){
                return this.roomVisits.get(i);
            }
          }
        return 0;
    }

    //method to print the current NPC attributes
    public void printNPCCurrentAttributes(){
        System.out.println("Anger\t\t: " + getAnger());
        System.out.println("Fear\t\t:  " + getFear());
        System.out.println("Relation\t:  " + getRelation());
        System.out.println("Happiness\t: " + getHappiness());
    }

    //toString method to return an NPC's profile, consisting of their name and description
    @Override
    public String toString(){
        return "Profile:\nName\t\t: " + this.getName() + "\nProfile\t\t: " + this.getDescription() + "\n";
    }
 




}
