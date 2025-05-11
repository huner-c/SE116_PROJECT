
///                              IF CODING IS AN ART, THEN I AM THE MONA LISA

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class Main
{
    public static void main(String[] args)
    {
        FSM fsm = new FSM();
        fsm.START(args);
    }
}
class FSM implements Serializable
{

    private transient int inputCount = 0;
    private transient PrintWriter logWriter = null;
    private transient String fileName = "";

    private final transient List<String> logErrorMessages = new ArrayList<>();

    private ArrayList<String> uniqueKeysList = new ArrayList<>();
    private ArrayList<String> targetStatesList = new ArrayList<>();

    private final ArrayList<String> warningsForTransitions = new ArrayList<>();

    private String initialState = "";
    private ArrayList<String> statesList = new ArrayList<>();
    private ArrayList<String> symbolsList = new ArrayList<>();
    private ArrayList<String> finalStatesList = new ArrayList<>();


    private transient String[] arrayInputs = null;
    private transient String stringInputs = "";

    private transient boolean isLogging;


    private void addLOG(String msg)
    {
        if (isLogging && logWriter != null)
        {
            logWriter.println(msg);
            logWriter.flush();
        }
    }
    private void LOG()
    {
        if (isLogging && logWriter != null)
        {
            if (!logErrorMessages.isEmpty())
            {

                addLOG("\nErrors in the log:");
                for (String errorMessage : logErrorMessages)
                {
                    addLOG(errorMessage);
                }
            }
            logWriter.close();
            isLogging = false;
            logErrorMessages.clear();

            System.out.println("STOPPED LOGGING");
        }
        else
        {
            System.out.println("LOGGING was not enabled");
        }
    }
    private void LOGwInput()
    {
        try
        {
            logWriter = new PrintWriter(new FileWriter(fileName, false), true); //append mode ve  autoFlush
            isLogging = true;
            addLOG(stringInputs + ";");
        }
        catch (IOException e)
        {
            System.out.println("Log file cannot be created: " + e.getMessage());
        }
    }
    public void START(String[] args)
    {
        VERSION_CONTROL();
        if (args.length != 0)
        {
            processCommandsFromFile(args[0]);
        }
        else
        {
            takeInput();
        }
    }
    private void processCommandsFromFile(String fileName)
    {
        inputCount = 0;
        try
        {
            if (fileName == null || fileName.trim().isEmpty())
            {
                String errorMessage = "WARNING: File name cannot be null or empty.";
                if(isLogging)
                {
                    logErrorMessages.add("Line " + inputCount + ": " + errorMessage);
                }
                throw new InvalidFileNameException(errorMessage);
            }
            if (fileName.matches(".*[<>:\"/\\\\|?*].*") || fileName.contains("\0"))
            {
                String errorMessage = "WARNING: File name contains invalid characters: " + fileName;
                if(isLogging)
                {
                    logErrorMessages.add("Line " + inputCount + ": " + errorMessage);
                }
                throw new InvalidFileNameException(errorMessage);
            }

            try (Scanner scanner = new Scanner(new File(fileName)))
            {
                while (scanner.hasNextLine())
                {
                    String line = scanner.nextLine().trim();
                    inputCount++;
                    if (line.isEmpty())
                    {
                        continue;
                    }

                    if (line.contains(";"))
                    {
                        addLOG(line);
                        String commandOnly = line.split(";", 2)[0].trim();
                        if (commandOnly.isEmpty()) return;
                        stringInputs = commandOnly;
                        arrayInputs = commandOnly.split("\\s+");
                        try
                        {
                            processCommand();
                        }
                        catch (Exception e)
                        {
                            String errorMessage = "Error: " + e.getMessage();
                            if(isLogging)
                            {
                                logErrorMessages.add("Line " + inputCount + ": " + errorMessage);
                            }
                        }
                    }
                    else
                    {
                        String errorMessage = "WARNING: Invalid command format (missing semicolon): " + line;
                        if(isLogging)
                        {
                            logErrorMessages.add("Line " + inputCount + ": " + errorMessage);
                        }
                    }
                }
                takeInput();
            }
            catch (FileNotFoundException e)
            {
                String errorMessage = "WARNING: Cannot access the file: " + fileName + ". File not found.";
                if(isLogging)
                {
                    logErrorMessages.add("Line " + inputCount + ": " + errorMessage);
                }
                throw new FileAccessException(errorMessage);
            }
        }
        catch (InvalidFileNameException | FileAccessException e)
        {
            System.out.println(e.getMessage());
            if(isLogging)
            {
                logErrorMessages.add("Line " + inputCount + ": " + e.getMessage());
            }
        }

        if (!logErrorMessages.isEmpty())
        {
            addLOG("\nErrors in the log:");
            for (String errorMessage : logErrorMessages)
            {
                addLOG(errorMessage);
            }
        }
    }
    private void takeInput()
    {
        Scanner sc = new Scanner(System.in);
        StringBuilder builder = new StringBuilder();
        while (true)
        {
            System.out.print("? ");
            String currentLine = sc.nextLine();
            builder.append(currentLine.trim());


            if (currentLine.contains(";"))
            {
                stringInputs = builder.toString();
                stringInputs = stringInputs.replaceAll("\n", " ");
                addLOG(stringInputs);
                stringInputs = stringInputs.split(";", 2)[0].trim();
                arrayInputs = stringInputs.split("\\s+");

                if(isLogging)
                {
                    inputCount++;
                }

                processCommand();
                builder.setLength(0);
            }
            else
            {
                builder.append(" ");
            }
        }
    }
    private void processCommand()
    {
        if(arrayInputs.length>=2)
        {
            if(arrayInputs[0].equalsIgnoreCase("LOG"))
            {
                if(arrayInputs[1].endsWith(".txt"))
                {
                    fileName= arrayInputs[1];
                }else
                {
                    fileName= arrayInputs[1]+".txt";
                }
                LOGwInput();
            }
            else if (arrayInputs[0].equalsIgnoreCase("SYMBOLS"))
            {
                SYMBOLSwInput(arrayInputs);
            }
            else if (arrayInputs[0].equalsIgnoreCase("STATES"))
            {
                STATESwInput(arrayInputs);
            }
            else if(arrayInputs[0].equalsIgnoreCase("INITIAL-STATE") && arrayInputs.length==2)
            {
                INITIAL_STATE();
            }
            else if(arrayInputs[0].equalsIgnoreCase("FINAL-STATES")){
                FINAL_STATES();
            }
            else if(arrayInputs[0].equalsIgnoreCase("TRANSITIONS"))
            {
                TRANSITIONS(stringInputs);
            }
            else if (arrayInputs[0].equalsIgnoreCase("PRINT") && arrayInputs.length==2)
            {
                PRINT();
            }
            else if(arrayInputs[0].equalsIgnoreCase("COMPILE") && arrayInputs.length==2)
            {
                try
                {
                    COMPILE(arrayInputs[1]);
                }
                catch (FileCreationException | InvalidFileNameException e1Compile)
                {
                    System.out.println(e1Compile.getMessage());
                }
            }
            else if(arrayInputs[0].equalsIgnoreCase("LOAD"))
            {
                LOAD(arrayInputs[1]);
            }
            else if(arrayInputs[0].equalsIgnoreCase("EXECUTE"))
            {
                EXECUTE(arrayInputs);
            }
            else
            {
                System.out.println("Invalid Command");
            }
        }
        else
        {
            if (stringInputs.equalsIgnoreCase("TRANSITIONS"))
            {
                try
                {
                    logErrorMessages.add("WARNING Line Number: " + inputCount + " TRANSITIONS method wants an input after the command");
                    throw new NeedsInputException("WARNING: TRANSITIONS method wants an input after the command");
                }
                catch(NeedsInputException e1Transitions)
                {
                    System.out.println(e1Transitions.getMessage());
                }
            }
            else if (stringInputs.equalsIgnoreCase("LOAD"))
            {
                try
                {
                    logErrorMessages.add("WARNING: LOAD method wants a fileName after the command");
                    throw new NeedsFileNameException("WARNING: LOAD method wants a fileName after the command");
                }
                catch(NeedsFileNameException e1LOAD)
                {
                    System.out.println(e1LOAD.getMessage());
                }
            }
            else if (stringInputs.equalsIgnoreCase("EXIT"))
            {
                EXIT();
            }
            else if (stringInputs.equalsIgnoreCase("LOG"))
            {
                LOG();
            }
            else if (stringInputs.equalsIgnoreCase("STATES"))
            {
                STATES();
            }
            else if (stringInputs.equalsIgnoreCase("COMPILE"))
            {
                try
                {
                    throw new InvalidFileNameException("WARNING: Compile method wants a fileName after the command");
                }
                catch(NeedsFileNameException e1COMPILE)
                {
                    System.out.println(e1COMPILE.getMessage());
                }
            }
            else if (stringInputs.equalsIgnoreCase("SYMBOLS"))
            {
                SYMBOLS();
            }
            else if (stringInputs.equalsIgnoreCase("CLEAR"))
            {
                CLEAR();
            }
            else if (stringInputs.equalsIgnoreCase("PRINT"))
            {
                PRINT();
            }
            else if (stringInputs.equalsIgnoreCase("INITIAL-STATE"))
            {
                try
                {
                    throw new NeedsInputException("WARNING: INITIAL-STATE method wants an input after the command");
                }
                catch(NeedsInputException e1INITIAL_STATE)
                {
                    System.out.println(e1INITIAL_STATE.getMessage());
                }
            }
            else
            {
                System.out.println("Invalid Command");
            }
        }
    }
    private void VERSION_CONTROL()
    {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        System.out.println("FSM DESIGNER <Update FINAL>  "+formattedDateTime);
    }
    private void EXIT()
    {
        if(isLogging)
        {
            LOG();
        }
        System.exit(0);
    }
    private void SYMBOLSwInput(String[] inputsArray)
    {
        for(int i = 1; i < inputsArray.length; i++)
        {
            String symbol = inputsArray[i].toUpperCase();
            if(isAlphaNumber(symbol))
            {
                boolean isDeclearedBefore = false;
                for(String symbolInTheList: symbolsList)
                {
                    if(symbolInTheList.equals(symbol))
                    {
                        isDeclearedBefore = true;
                        break;
                    }
                }
                if(isDeclearedBefore)
                {
                    System.out.println(symbol+ " is already exists.");
                    if(isLogging)
                    {
                        logErrorMessages.add("Line " + inputCount + ": WARNING " + symbol + " is already exists.");
                    }
                }
                else
                {
                    symbolsList.add(symbol);
                }
            }
            else
            {
                System.out.println("WARNING " + symbol + " is not an alphanumeric thing");
                if(isLogging)
                {
                    logErrorMessages.add("Line " + inputCount + ": WARNING " + symbol + " is not an alphanumeric thing");
                }
            }
        }
    }
    private void SYMBOLS()
    {
        System.out.print("SYMBOLS: ");
        for(String symbolInTheList: symbolsList)
        {
            System.out.print(symbolInTheList+" ");
        }
        System.out.println();
    }
    private void STATESwInput(String[] inputsArray)
    {
        for(int i = 1; i< inputsArray.length; i++)
        {
            if(isAlphaNumeric(inputsArray[i].toUpperCase()))
            {
                if(statesList.isEmpty())
                {
                    statesList.add(inputsArray[i].toUpperCase());
                    initialState = inputsArray[i].toUpperCase();

                    continue;
                }

                boolean isDeclearedBefore = false;
                for(String stateInTheList : statesList)
                {
                    if(stateInTheList.equals(inputsArray[i].toUpperCase()))
                    {
                        isDeclearedBefore = true;
                        break;
                    }
                }
                if(isDeclearedBefore)
                {
                    System.out.println("WARNING: " + inputsArray[i].toUpperCase() + " is already exists");

                    if(isLogging)
                    {
                        logErrorMessages.add("Line " + inputCount + ": WARNING " + inputsArray[i].toUpperCase() + " is already exists");
                    }
                    continue;
                }
                statesList.add(inputsArray[i].toUpperCase());

            }
            else
            {
                if(isLogging)
                {
                    logErrorMessages.add("Line " + inputCount + ": WARNING " +inputsArray[i] + " is not an alphanumeric thing");
                }
                System.out.println("WARNING " +inputsArray[i] + " is not an alphanumeric thing");
            }
        }
    }
    private void STATES()
    {
        System.out.print("STATES: ");
        for (String state : statesList)
        {
            String output = state;

            boolean isInitial = state.equalsIgnoreCase(initialState);
            boolean isFinal = false;

            for (String finalState : finalStatesList)
            {
                if (state.equalsIgnoreCase(finalState))
                {
                    isFinal = true;
                    break;
                }
            }

            if (isInitial && isFinal)
            {
                output += "***";
            } else if (isFinal)
            {
                output += "**";
            } else if (isInitial)
            {
                output += "*";
            }
            System.out.print(output + " ");
        }
        System.out.println();
    }
    private void INITIAL_STATE()
    {
        String state = arrayInputs[1].toUpperCase();

        if (!state.matches("[a-zA-Z0-9]+"))
        {
            System.out.println("WARNING: " + state + " is not an alphanumeric thing");
            if(isLogging)
            {
                logErrorMessages.add("Line " + inputCount + ": WARNING " + state + " is not an alphanumeric thing");
            }
            return;
        }
        if (!statesList.contains(state))
        {
            statesList.add(state);
            System.out.println("WARNING: " + state + " has not been declared yet, THUS added to list");
        }
        this.initialState = state;
    }
    private void FINAL_STATES()
    {
        String states = stringInputs.substring(stringInputs.indexOf(" ") + 1).trim().toUpperCase();
        String[] stateArray = states.split("[,\\s]+");

        for (String state : stateArray) {
            state = state.trim();

            if (!state.matches("[a-zA-Z0-9]+")) {
                System.out.println("Warning: invalid final state name: " + state);
                if(isLogging)
                {
                    logErrorMessages.add("Line " + inputCount + ": WARNING " + state + " has invalid final state name");
                }
                continue;
            }

            if (!statesList.contains(state)) {

                System.out.println("Warning: final state not declared previously, added to states list: " + state);
                statesList.add(state);
            }

            if (!finalStatesList.contains(state)) {
                finalStatesList.add(state);
            }
        }
    }
    private void TRANSITIONS(String inputLine)
    {

        boolean isDeclearedBefore=false;

        String ways = inputLine.substring(11);
        String[] waysArray = ways.trim().split(",");

        for(String transition : waysArray)
        {
            transition = transition.trim(); //3 Q1 Q2
            String[] digits = transition.split(" ");

            try
            {
                if(digits.length!=3)
                {
                    throw new ArrayIndexOutOfBoundsException("Symbols or states were entered incompletely");
                }



                if(!symbolsList.contains(digits[0].toUpperCase()))
                {
                    if(!warningsForTransitions.contains("Invalid symbols:" + digits[0].toUpperCase()))
                    {
                        warningsForTransitions.add("Invalid symbols:"+ digits[0].toUpperCase());
                        if(isLogging)
                        {
                            logErrorMessages.add("Line " + inputCount + ": WARNING " + digits[0].toUpperCase() + " is invalid symbol");
                        }
                    }
                }

                if(!statesList.contains(digits[1].toUpperCase()))
                {
                    if(!warningsForTransitions.contains("Invalid state: "+ digits[1].toUpperCase()))
                    {
                        warningsForTransitions.add("Invalid state: "+ digits[1].toUpperCase());
                        if(isLogging)
                        {
                            logErrorMessages.add("Line " + inputCount + ": WARNING " + digits[1].toUpperCase() + " is invalid state");
                        }
                    }
                }

                if(!statesList.contains(digits[2].toUpperCase()))
                {
                    if(!warningsForTransitions.contains("Invalid state: "+ digits[2].toUpperCase()))
                    {
                        warningsForTransitions.add("Invalid state: "+ digits[2].toUpperCase());
                        if(isLogging)
                        {
                            logErrorMessages.add("Line " + inputCount + ": WARNING " + digits[2].toUpperCase() + " is invalid state");
                        }
                    }
                }
            }
            catch (ArrayIndexOutOfBoundsException e7)
            {
                if(isLogging)
                {
                    logErrorMessages.add("Line " + inputCount + ": WARNING Symbols or states were entered incompletely");
                }
                System.out.println( "WARNING: Symbols or states were entered incompletely");
            }


            if(symbolsList.contains(digits[0].toUpperCase()) && statesList.contains(digits[1].toUpperCase()) && statesList.contains(digits[2].toUpperCase()))
            {
                String uniqueKey = digits[0].toUpperCase() + digits[1].toUpperCase();
                String targetState = digits[2].toUpperCase();

                if(uniqueKeysList.isEmpty() && targetStatesList.isEmpty())
                {
                    uniqueKeysList.add(uniqueKey);
                    targetStatesList.add(targetState);
                }
                else
                {
                    for(String cc : uniqueKeysList)
                    {
                        if(cc.equals(uniqueKey)){
                            String xx = cc.substring(0,1);
                            String kk = cc.substring(1,cc.length());

                            if(!warningsForTransitions.contains("transition already exists for <"+xx.toUpperCase()+","+kk.toUpperCase()+">. "))
                            {
                                warningsForTransitions.add("transition already exists for <"+xx.toUpperCase()+","+kk.toUpperCase()+">. ");
                                if(isLogging)
                                {
                                    logErrorMessages.add("Line " + inputCount + " Transition already exists for <"+xx.toUpperCase()+","+kk.toUpperCase()+">. ");
                                }
                            }
                            int a = uniqueKeysList.indexOf(uniqueKey);
                            targetStatesList.set(a,targetState);
                            isDeclearedBefore=true;
                            break;
                        }
                    }
                    if(!isDeclearedBefore)
                    {
                        uniqueKeysList.add(uniqueKey);
                        targetStatesList.add(targetState);
                    }
                }
            }
            else
            {
                isDeclearedBefore=true;
            }
        }
        if(isDeclearedBefore)
        {
            for(String warning: warningsForTransitions)
            {
                System.out.println("WARNING: " + warning);
            }
        }
        warningsForTransitions.clear();
    }

    private void EXECUTE(String[] inputsArray)
    {
        if(uniqueKeysList.isEmpty())
        {
            System.out.println("Could not find any transition");
            if(isLogging)
            {
                logErrorMessages.add("Line " + inputCount + ": Could not find any transition");
            }
            return;
        }
        if(finalStatesList.isEmpty())
        {
            System.out.println("Could not find any final state");
            if(isLogging)
            {
                logErrorMessages.add("Line " + inputCount + ": Could not find any final state");
            }
            return;
        }

        String joined = String.join("", Arrays.copyOfRange(inputsArray, 1, inputsArray.length));
        String[] currectArray = joined.split("");

        for(String aa : currectArray)
        {
            if(!symbolsList.contains(aa))
            {
                System.out.println(aa + " was not previously declared as a symbol");
                if(isLogging)
                {
                    logErrorMessages.add("Line " + inputCount + ": " + aa + " was not previously declared as a symbol");
                }
                return;
            }
        }
        StringBuilder line = new StringBuilder();
        for (String aa : currectArray)
        {
            if(!aa.equals(" ")) {
                line.append(aa);
            }
        }
        String [] currentArray = line.toString().split("");
        String currentState = initialState;
        System.out.print(currentState+" ");

        for(String aa:currentArray)
        {
            String abc = aa + currentState;

            int k = uniqueKeysList.indexOf(abc);
            if (k == -1)
            {
                break;
            }
            currentState = targetStatesList.get(k);
            System.out.print(currentState+" ");
        }

        for(String aa: finalStatesList)
        {
            if(aa.equals(currentState))
            {
                System.out.println("Yes");
                return;
            }
        }
        System.out.println("No");
    }
    private void CLEAR()
    {
        initialState = "";
        statesList.clear();
        symbolsList.clear();
        finalStatesList.clear();
        uniqueKeysList.clear();
        //targetStatesList.clear();
    }
    private void PRINT()
    {
        StringBuilder output = new StringBuilder();

        if(arrayInputs.length==1)
        {
            output.append("SYMBOLS: {");
            for (int i = 0; i < symbolsList.size(); i++) {
                output.append(symbolsList.get(i));
                if (i < symbolsList.size() - 1) output.append(",");
            }
            output.append("}\n");

            output.append("STATES: {");
            for (int i = 0; i < statesList.size(); i++) {
                output.append(statesList.get(i));
                if (i < statesList.size() - 1) output.append(",");
            }
            output.append("}\n");

            output.append("INITIAL STATE: ").append(initialState).append("\n");

            output.append("FINAL STATES: {");
            for (int i = 0; i < finalStatesList.size(); i++) {
                output.append(finalStatesList.get(i));
                if (i < finalStatesList.size() - 1) output.append(",");
            }
            output.append("}\n");

            output.append("TRANSITIONS: {");
            for (int i = 0; i < uniqueKeysList.size(); i++) {
                String xx = uniqueKeysList.get(i).substring(0, 1);
                String kk = uniqueKeysList.get(i).substring(1);
                output.append(xx).append(" ").append(kk).append(" ").append(targetStatesList.get(i));
                if (i < uniqueKeysList.size() - 1) output.append(", ");

            }
            output.append("}\n");


            String result = output.toString();
            System.out.print(result);
        }

        if (arrayInputs.length == 2)
        {
            if(!arrayInputs[1].endsWith(".txt")) arrayInputs[1] += ".txt";
            String filename = arrayInputs[1];
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename)))
            {
                if(!symbolsList.isEmpty()) {
                    writer.write("SYMBOLS ");
                    for (String symbol : symbolsList) writer.write(symbol + " ");
                    writer.write(";\n");
                }
                if(!statesList.isEmpty()) {
                    writer.write("STATES ");
                    for (String state : statesList) writer.write(state + " ");
                    writer.write(";\n");
                }
                if(!initialState.isEmpty()) {
                    writer.write("INITIAL-STATE " + initialState + ";\n");
                }
                if(!finalStatesList.isEmpty()) {
                    writer.write("FINAL-STATES ");
                    for (String state : finalStatesList) writer.write(state + " ");
                    writer.write(";\n");
                }

                if(!uniqueKeysList.isEmpty()) {
                    writer.write("TRANSITIONS");
                    writer.write(" ");

                    for(int i = 0; i< uniqueKeysList.size(); i++){
                        int k= uniqueKeysList.size()-1;
                        String line="";
                        line+= uniqueKeysList.get(i).substring(0,1);

                        line+=" ";
                        line+= uniqueKeysList.get(i).substring(1);

                        line+=" ";
                        line+= targetStatesList.get(i);
                        if(k!=i){
                            line+=", ";

                        }
                        writer.write(line);
                    }
                    writer.write(";\n");
                }
                System.out.println("FSM data written to file: " + filename);
            }
            catch (IOException e)
            {
                System.out.println("Error writing to file: " + e.getMessage());
                if(isLogging)
                {
                    logErrorMessages.add("Line " + inputCount + ": WARNING " + filename + " Error writing to file: ");
                }
            }
        }
    }
    private void COMPILE(String fileName)
    {
        if(!fileName.endsWith(".ser")) fileName += ".ser";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName)))
        {
            if (!Files.exists(Paths.get(fileName)))
                Files.createFile(Paths.get(fileName));

            out.writeObject(this);
            out.flush();
            System.out.println("Datas are serialized and written to " + fileName);
        }
        catch (InvalidClassException e)
        {
            if(isLogging)
            {
                logErrorMessages.add("Line " + inputCount + ": WARNING " + this + "Invalid Class!");
            }
            System.out.println("Warning: Invalid Class! " + e.getMessage());

        }
        catch (NotSerializableException e)
        {
            if(isLogging)
            {
                logErrorMessages.add("Line " + inputCount + ": WARNING " + this + " Not Serializable!");
            }
            System.out.println("Warning: Not Serializable! " + e.getMessage());
        }
        catch (IOException e)
        {
            if(isLogging)
            {
                logErrorMessages.add("Line " + inputCount + ": WARNING " + fileName + "Something went wrong!");
            }
            System.out.println("Warning: Something went wrong! " + e.getMessage());
        }
    }
    private void LOAD(String fileName)
    {
        if (fileName.endsWith(".txt"))
        {
            processCommandsFromFile(fileName);
        }

        if (!fileName.endsWith(".ser"))
        {
            fileName += ".ser";
        }

        File file = new File(fileName);
        if (!file.exists())
        {
            if(isLogging)
            {
                logErrorMessages.add("Line " + inputCount + ": WARNING " + fileName + "File not found");
            }
            System.out.println("ERROR: File not found: " + fileName);
            return;
        }

        FSM readFSM = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName)))
        {
            readFSM = (FSM) in.readObject();
        }
        catch (FileNotFoundException e)
        {
            if(isLogging)
            {
                logErrorMessages.add("Line " + inputCount + ": WARNING " + fileName + "File not found");
            }
            System.out.println("WARNING: File not found!");
        }
        catch (InvalidClassException e)
        {
            if(isLogging)
            {
                logErrorMessages.add("Line " + inputCount + ": WARNING " + "Version not compatible.");
            }
            System.out.println("WARNING: Version not compatible.");
        }
        catch (StreamCorruptedException e)
        {
            if(isLogging)
            {
                logErrorMessages.add("Line " + inputCount + ": WARNING " + fileName + " File corrupted.");
            }
            System.out.println("WARNING: File corrupted.");
        }
        catch (OptionalDataException e)
        {
            if(isLogging)
            {
                logErrorMessages.add("Line " + inputCount + ": WARNING " + "Unexpected data found.");
            }
            System.out.println("WARNING: Unexpected data found.");
        }
        catch (ClassNotFoundException e)
        {
            if(isLogging)
            {
                logErrorMessages.add("Line " + inputCount + ": WARNING " + this + "Class not found");
            }
            System.out.println("WARNING: Class not found.");
        }
        catch (IOException e)
        {
            if(isLogging)
            {
                logErrorMessages.add("Line " + inputCount + ": WARNING " + fileName + "Something went wrong!");
            }
            System.out.println("WARNING: Something went wrong!");
        }

        if (readFSM != null) {
            this.setSymbolsList(readFSM.getSymbolsList());
            this.setStatesList(readFSM.getStatesList());
            this.setInitialState(readFSM.getInitialState());
            this.setFinalStatesList(readFSM.getFinalStatesList());
            this.setUniqueKeysList(readFSM.getUniqueKeysList());
            this.setLogging(readFSM.getLogging());
            System.out.println("Object loading successful!");
        }
        else
        {
            System.out.println("Error: Failed to load FSM from file: " + fileName);
        }
    }
    private boolean isAlphaNumeric(String input)
    {
        if(input.length() == 1)
        {
            return false;
        }
        if(input.matches("[a-zA-Z]+")){
            return false;
        }
        if(input.matches("[0-9]+")){
            return false;
        }
        return input.matches("[a-zA-Z0-9]+");
    }
    private boolean isAlphaNumber(String input)
    {
        if(input.length()>1){
            return false;
        }

        return input.matches("[a-zA-Z0-9]+");
    }
    public String getInitialState() {
        return initialState;
    }
    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }
    public ArrayList<String> getUniqueKeysList() {
        return uniqueKeysList;
    }
    public void setUniqueKeysList(ArrayList<String> uniqueKeysList) {
        this.uniqueKeysList = uniqueKeysList;
    }
    public ArrayList<String> getFinalStatesList() {
        return finalStatesList;
    }
    public void setFinalStatesList(ArrayList<String> finalStatesList) {
        this.finalStatesList = finalStatesList;
    }
    public ArrayList<String> getSymbolsList() {
        return symbolsList;
    }
    public void setSymbolsList(ArrayList<String> symbolsList) {
        this.symbolsList = symbolsList;
    }
    public ArrayList<String> getStatesList() {
        return statesList;
    }
    public void setStatesList(ArrayList<String> statesList) {
        this.statesList = statesList;
    }
    public boolean getLogging() {
        return isLogging;
    }
    public void setLogging(boolean logging) {
        this.isLogging = logging;
    }
}





class InvalidFileNameException extends RuntimeException
{
    public InvalidFileNameException(String message)
    {
        super(message);
    }
}
class FileCreationException extends RuntimeException
{
    public FileCreationException(String message)
    {
        super(message);
    }
}
class FileAccessException extends Exception
{
    public FileAccessException(String message)
    {
        super(message);
    }
}
class TransitionsWay extends Exception{
    public  TransitionsWay(String message){
        super(message);
    }
}
class InvalidInputException extends RuntimeException {
    public InvalidInputException(String culprit) {
        super("Warning: Invalid Input " + culprit);
    }
}
class NoNeedExtraInputAfterCommand extends RuntimeException
{
    public NoNeedExtraInputAfterCommand(String message)
    {
        super(message);
    }
}
class NeedsFileNameException extends RuntimeException
{
    public NeedsFileNameException(String message)
    {
        super(message);
    }
}
class NeedsInputException extends RuntimeException
{
    public NeedsInputException(String message)
    {
        super(message);
    }
}
class InvalidTransitionTypeLength extends RuntimeException
{
    public InvalidTransitionTypeLength(String message)
    {
        super(message);
    }
}
