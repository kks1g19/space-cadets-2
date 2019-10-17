//package com.konradsobczak.bbeat;

/**
  * BBStatement - parsed BareBones statement
  *
  * @author Konrad Sobczak
  */

import java.util.ArrayList;

public class BBStatement{
    private final String operation;
    private final ArrayList<String> arguments;

    /**
      * Create new statement
      *
      * @param words Words to be parsed into statement
      */
    public BBStatement(ArrayList<String> words){
        this.operation = words.get(0);
        words.remove(0);
        this.arguments = words;
    }

    /**
      * Gets the arguments
      *
      * @return ArrayList of arguments
      */
    public ArrayList getArguments(){
        return this.arguments;
    }

    /**
      * Gets the operation
      *
      * @return String operation name
      */
    public String getOperation(){
        return this.operation;
    }
}