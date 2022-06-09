package gameobjects;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Dialogue implements java.io.Serializable {

    private ArrayList<String> nextDialogueLine;
    private String dialogueText;
    private BigDecimal angerChange, fearChange, relationChange, happinessChange;

    public Dialogue(){}

    //setters
    //setting the next dialogue line that is linked to the current one
    public void setNextDialogueLine(ArrayList<String> nextDialogueLine){
        this.nextDialogueLine = nextDialogueLine;
    }

    //setting the actual text of dialogue as a String
    public void setDialogueText(String dialogue){
        this.dialogueText = dialogue;
    }

    //setting the attribute changes for the dialogue
    //if the dialogue is found to be a dialogue option
    public void setAngerChange(BigDecimal angerChange){
        this.angerChange = angerChange;
    }

    public void setFearChange(BigDecimal fearChange){
        this.fearChange = fearChange;
    }

    public void setRelationChange(BigDecimal relationChange){
        this.relationChange = relationChange;
    }

    public void setHappinessChange(BigDecimal happinessChange){
        this.happinessChange = happinessChange;
    }

    
    //getters
    //returning the next linked dialogue lines as a String ArrayList
    public ArrayList<String> getNextDialogueLine(){
        return this.nextDialogueLine;
    }

    //returning the first dialogue from the list of the next linked dialogue as an int
    //the int represents which line in the text file the dialogue will be read from
    public int getFirstNextDialogue(){
        int firstnextDialogueLine = Integer.parseInt(this.nextDialogueLine.get(0));
        return firstnextDialogueLine;
    }

    //returning the dialogue text as a String
    public String getDialogueText(){
        return this.dialogueText;
    }

    //returning the attribute changes as a BigDecimal
    //if the dialogue is found to be a dialogue option
    public BigDecimal getAngerChange(){
        return this.angerChange;
    }

    public BigDecimal getFearChange(){
        return this.fearChange;
    }

    public BigDecimal getRelationChange(){
        return this.relationChange;
    }

    public BigDecimal getHappinessChange(){
        return this.happinessChange;
    }


}

