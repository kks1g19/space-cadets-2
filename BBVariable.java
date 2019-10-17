//package com.konradsobczak.bbeat;

/**
  * BBVariable - Variables in BareBones
  *
  * @author Konrad Sobczak
  */


public class BBVariable {
    private Integer integerValue;
    private String stringValue;
    private Double doubleValue;
    private String type;
    public BBVariable(String value){
        try {
            this.integerValue = Integer.parseInt(value);
            this.type = "int";
            if(this.integerValue < 0){
              throw new Exception("Invalid value");
            }
        } catch (Exception e){
            /*try {
                this.doubleValue = Double.parseDouble(value);
                this.type = "double";
            } catch (Exception e2){
                this.stringValue = value;
                this.type = "str";
            }*/
            System.out.println("Invalid value");
            System.exit(1);
        }
    }


    /**
      * Get the value of the variable
      *
      * @return int|double|String value
      */
    public <T> T getValue(){
        //if(integerValue != null){
            return (T) integerValue;
        /*} else if(doubleValue != null){
            return (T) doubleValue;
        } else {
            return (T) stringValue;
        }*/
    }

    /**
      * Get type of the variable
      *
      * @return String type
      */
    public String getType(){
        return this.type;
    }

    /**
      * increment this variable
      *
      * @return true
      */
    public boolean increment(){
        if(type.equals("int")){
            this.integerValue++;
        } /*else if(type.equals("double")){
            this.doubleValue++;
        } else {
            return false;
        }*/
        return true;
    }

    /**
      * decrement this variable
      *
      * @return true
      */
    public boolean decrement(){
        if(type.equals("int") && this.integerValue >= 1){
            this.integerValue--;
        } /*else if(type.equals("double")){
            this.doubleValue--;
        } else {
            return false;
        }*/
        return true;
    }

    /**
      * clear this variable
      *
      * @return true
      */
    public boolean clear(){
        if(type.equals("int")){
            this.integerValue = 0;
        } /*else if(type.equals("double")){
            this.doubleValue = 0.0;
        } else {
            return false;
        }*/
        return true;
    }

    public String toString(){
        return this.getValue().toString() /*+ ":" + this.getType()*/;
    }
}