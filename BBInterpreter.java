//package com.konradsobczak.bbeat;

/**
  * BBInterpreter - executes BareBones statements
  *
  * @author Konrad Sobczak
  */

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Arrays;

public class BBInterpreter {
    private HashMap<String, BBVariable> variables = new HashMap<String, BBVariable>();
    private ArrayDeque<BBLoopCondition> loops = new ArrayDeque<BBLoopCondition>();
    private final BBParser parser;
    private boolean verbose;

    private final List invalidNames = Arrays.asList(new String[]{"clear", "copy", "decr", "do", "end", "incr", "init", "not", "to", "while"});

    /**
      * Create a new BB Interpreter with given source code
      *
      * @param source BareBones source code
      * @param args Argumenst given to main function
      */
    public BBInterpreter(String source, String[] args){
        this.parser = new BBParser(source);

        if(Arrays.asList(args).contains("-v")){
            this.verbose = true;
        }
        for(String arg : args){
            if(arg.charAt(0) != '-' && arg != args[args.length -1]){
                String name = arg.substring(0, arg.lastIndexOf("=")).trim();
                String value = arg.substring(arg.lastIndexOf("=") + 1, arg.length()).trim();
                setVariable(name, new BBVariable(value));
            }
        }
        if(this.verbose){
            System.out.println("Initialised variables: " + variables);
        }
    }

    /**
      * Execute entire program
      * @return true if all statements executed correctly
      */
    public boolean executeSource(){
        boolean success = true;
        BBStatement statement;
        while((statement = this.parser.nextStatement()) != null) {
            success = success ? this.execute(statement) : false;
            if(verbose && !statement.getOperation().equals("while") && !statement.getOperation().equals("end")){
                System.out.println(variables);
            }
        }
        if(verbose){
            System.out.println("Finished with variables: " + variables);
        }
        return success;
    }

    /**
      * Execute a single statement
      * @param statement BBStatement to be executed
      * @return True if statement executed correctly
      */
    private boolean execute(BBStatement statement){
        boolean success = false;
        switch (statement.getOperation()){
            case "incr":
                success = incr(statement.getArguments());
                break;
            case "decr":
                success = decr(statement.getArguments());
                break;
            case "copy":
                success = copy(statement.getArguments());
                break;
            case "clear":
                success = clear(statement.getArguments());
                break;
            case "init":
                success = init(statement.getArguments());
                break;
            case "while":
                success = startLoop(statement.getArguments());
                break;
            case "end":
                success = end();
                break;
            default:
                System.out.println("Error: no such operation " + statement.getOperation());
                System.exit(1);
        }
        return success;
    }

    /**
      * Perform incr operation on argument
      * @param args name of variable to be incremented
      * @return true if successful
      */
    private boolean incr(ArrayList<String> args){
        String arg = args.get(0);
        return getVariable(arg).increment();
    }

    /**
      * Perform decr operation on argument
      * @param args name of variable to be decremented
      * @return true if successful
      */
    private boolean decr(ArrayList<String> args){
        String arg = args.get(0);
        return getVariable(arg).decrement();
    }

    /**
      * Perform copy operation on two arguments
      *
      * @param args name of variables to be copied
      * @return true if successful
      */
    private boolean copy(ArrayList<String> args){
        String arg = args.get(0);
        String target = args.get(2);
        BBVariable variable = getVariable(arg);
        if(variable != null){
            setVariable(target, new BBVariable(variable.getValue().toString()));
            return true;
        } else {
            System.out.print("Variable not found");
            return false;
        }
    }

    /**
      * Perform clear operation on argument
      *
      * @param args name of variable to be cleared
      * @return true if successful
      */
    private boolean clear(ArrayList<String> args){
        String arg = args.get(0);
        BBVariable variable = getVariable(arg);
        if(variable != null){
            return variable.clear();
        } else {
            setVariable(arg);
            return true;
        }
    }

    /**
      * Init variable with argument
      * @param args name of variable to be incremented
      * @return true if successful
      */
    private boolean init(ArrayList<String> args){
        String arg = args.get(0);
        BBVariable variable = getVariable(arg);
        if(variable == null){
            setVariable(arg, new BBVariable(args.get(2)));
            return true;
        } else {
            System.out.println("Error: trying to initialise an existing variable " + arg);
            System.exit(1);
            return false;
        }
    }

    /**
      * Create a new BBLoopCondition
      * @param args Arguments to the while loop
      * @return true if loop was successful
      */
    private boolean startLoop(ArrayList<String> args){
        BBVariable loopVarible = getVariable(args.get(0));
        BBVariable loopConstant = new BBVariable(args.get(2));
        BBLoopCondition loopCondition = new BBLoopCondition(loopVarible, loopConstant, this.parser.currentAddress() - 1);
        if(loopCondition.finished()){
            this.parser.branch(findLoopEnd(this.parser.currentAddress()) + 1);
        } else {
            loops.push(loopCondition);
        }
        return true;
    }

    /**
      * Check whether a loop ended
      *
      * @return true 
      */
    private boolean end() {
        if(!loops.isEmpty()){
            BBLoopCondition loopCondition = loops.pop();
            if(!loopCondition.finished()){
                this.parser.branch(loopCondition.getBranch());
            }
        }
        return true;
    }

    /**
      * Find corresponding end statement
      @param address Address of while
      @return Address of end
      */
    private int findLoopEnd(int address){
        ArrayList<BBStatement> statements = this.parser.getStatements();
        int depth = 0;
        for(int i = address - 1; i < statements.size(); i++){
            String operation = statements.get(i).getOperation();
            if(operation == "while"){
                depth++;
            } else if (operation == "end"){
                depth--;
                if(depth == 0){
                    return i;
                }
            }
        }
        return -1;
    }

    private void setVariable(String name, BBVariable value){
        if(!invalidNames.contains(name)){
            variables.put(name, value);
        } else {
            System.out.println("Error: Invalid name of variable: " + name);
            System.exit(1);
        }
    }

    private void setVariable(String name){
        setVariable(name, new BBVariable("0"));
    }

    private BBVariable getVariable(String name){
        return variables.get(name);
    }
}