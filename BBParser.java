//package com.konradsobczak.bbeat;

/**
  * BBParser - parses BareBones statements to more useful form
  *
  * @author Konrad Sobczak
  */

import java.util.ArrayList;
import java.util.Arrays;

public class BBParser {
    private final String source;
    private final ArrayList<BBStatement> statements;
    private int currentStatement = 0;

    /**
      *
      * @param source BareBones source to be parsed
      */
    public BBParser(String source){
        this.source = source;
        this.statements = new ArrayList<BBStatement>();
        for(String statement : source.split(";")){
            ArrayList<String> words = new ArrayList<String>(Arrays.asList(statement.split(" ")));
            words.removeIf(a -> a.equals(""));
            this.statements.add(new BBStatement(words));
        }
    }

    /**
      * Get next statement
      *
      * @return next BBStatement or null if doesn't exist
      */
    public BBStatement nextStatement(){
        try {
            BBStatement statement = this.statements.get(currentStatement);
            currentStatement++;
            return statement;
        } catch (Exception e){
            return null;
        }
    }

    /**
      * Create a branch
      *
      * @param address Address to jump to
      */
    public void branch(int address){
        this.currentStatement = address;
    }

    /**
      * Get current statement number
      *
      * @return int Statement number
      */
    public int currentAddress(){
        return this.currentStatement;
    }

    /**
      * Get all statements
      *
      * @return ArrayList of all statements
      */
    public ArrayList<BBStatement> getStatements(){
        return this.statements;
    }

    /**
      * Reset the addres to 0
      */
    public void reset(){
        currentStatement = 0;
    }
}