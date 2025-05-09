import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


///                                  IF CODING IS A ART, THEN I AM THE MONA LISA



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
    private String commandEntered="";
    private String fileName="";
    private String initialState = "";
    private ArrayList<String> FR4list=new ArrayList<>();
    private ArrayList<String> statesList = new ArrayList<>();
    private ArrayList<String> symbolsList = new ArrayList<>();
    private ArrayList<String> finalStates = new ArrayList<>();
    private ArrayList<String> transitionsList = new ArrayList<>();
    private ArrayList<String> transitionsList1 = new ArrayList<>();
    private String[] commandArray =null;
    private Formatter f_fr4=null;
    private boolean logging=false;
    private String comment;

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
        try
        {
            if (fileName == null || fileName.trim().isEmpty())
            {
                fr4ekleme("File name cannot be null or empty.");
                throw new InvalidFileNameException("File name cannot be null or empty.");
            }
            if (fileName.matches(".*[<>:\"/\\\\|?*].*") || fileName.contains("\0"))
            {
                fr4ekleme("File name contains invalid characters: " + fileName);
                throw new InvalidFileNameException("File name contains invalid characters: " + fileName);
            }

            try (Scanner scanner = new Scanner(new File(fileName)))
            {
                while (scanner.hasNextLine())
                {
                    String line = scanner.nextLine().trim();
                    if (line.isEmpty())
                    {
                        continue;
                    }
                    if (line.contains(";"))
                    {
                        String commandOnly = line.split(";", 2)[0].trim();
                        if (commandOnly.isEmpty()) return;

                        commandEntered = commandOnly;
                        commandArray = commandOnly.split("\\s+");
                        fr4ekleme(commandEntered);
                        processCommand();
                    }
                    else
                    {
                        System.out.println("Invalid command format (missing semicolon): " + line);
                        fr4ekleme("Invalid command format (missing semicolon): " + line);
                    }
                }
                takeInput();
            }
            catch (FileNotFoundException e)
            {
                throw new FileAccessException("Cannot access the file: " + fileName + ". File not found.");
            }
        }
        catch (InvalidFileNameException | FileAccessException e)
        {
            System.out.println(e.getMessage());
            fr4ekleme(e.getMessage());
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
                
                commandEntered = builder.toString();
                commandEntered = commandEntered.replaceAll("\n", " ");
                comment = commandEntered.split(";", 2)[1].trim();
                commandEntered = commandEntered.split(";", 2)[0].trim();
                commandArray = commandEntered.split("\\s+");
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
        System.out.println(Arrays.toString(commandArray));
        if(logging){
            fr4ekleme(commandEntered);
        }

        if(commandArray.length>=2)
        {


            if(commandArray[0].equals("LOG")){
                if(commandArray[1].endsWith(".txt")){
                    fileName=commandArray[1];
                }else{
                    fileName=commandArray[1]+".txt";
                }
                LOG();
            }
            else if (commandArray[0].equals("SYMBOLS")) {
                SYMBOLS(commandArray);
            }
            else if (commandArray[0].equals("STATES")){
                STATES(commandArray);
            }
            else if(commandArray[0].equals("INITIAL-STATE") && commandArray.length==2){
                INITIAL_STATE();
            }
            else if(commandArray[0].equals("FINAL-STATES")){
                FINAL_STATES();
            }
            else if(commandArray[0].equals("TRANSITIONS")){
                TRANSITIONS(commandEntered);
            }
            else if (commandArray[0].equals("PRINT") && commandArray.length==2){
                PRINT();
            }
            else if(commandArray[0].equalsIgnoreCase("COMPILE") && commandArray.length==2)
            {
                try
                {
                    COMPILE(commandArray[1]);
                }
                catch (FileCreationException | InvalidFileNameException e5)
                {
                    System.out.println(e5.getMessage());
                }
            }
            else if(commandArray[0].equals("LOAD")&& commandArray.length==2){
                load(commandArray[1]);
            }
            else if(commandArray[0].equals("EXECUTE")){
                EXECUTE();
            }
            else {
                System.out.println("invalid command");
                fr4ekleme("invalid command");
            }
        }
        else
        {
            if (commandEntered.equals("")) {

            }
            else if (commandEntered.equals("EXIT")) {
                EXIT();
            }
            else if (commandEntered.equals("LOG")) {
                LOG_();
            }
            else if (commandEntered.equals("STATES")) {
                STATES_();
            }
            else if (commandEntered.equalsIgnoreCase("COMPILE"))
            {
                try
                {
                    throw new InvalidFileNameException("Compile method wants a fileName after the command");
                }
                catch(InvalidFileNameException e3)
                {
                    System.out.println(e3.getMessage());
                }
            }
            else if (commandEntered.equals("SYMBOLS")) {
                SYMBOLS_();
            }

            else if (commandEntered.equals("CLEAR")) {
                CLEAR();
            }
            else if (commandEntered.equals("PRINT"))
            {
                PRINT();
            }
            else{
                System.out.println("invalid command");
                fr4ekleme("invalid command");
            }
        }
    }
    private void VERSION_CONTROL()
    {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        fr4ekleme("FSM DESIGNER <Update 06.05 21.10>  "+formattedDateTime);
        System.out.println("FSM DESIGNER <Update 06.05 21.10>  "+formattedDateTime);
    }
    private void EXIT()
    {
        fr4ekleme("TERMINATED BY USER");
        System.out.println("TERMINATED BY USER");

        try {
            f_fr4 = new Formatter(fileName);
            for (String aa:FR4list){
                f_fr4.format("%s \n", aa);
            }

        } catch (Exception e) {
            System.out.println("LOGGING was not enabled");
            System.out.println("file cannot be created, written, etc");
            System.out.println(e.getMessage());
            fr4ekleme("file cannot be created, written, etc");
        }finally {
            if(f_fr4!=null){
                System.out.println("STOPPED LOGGING");
                fr4ekleme("STOPPED LOGGING");
                f_fr4.close();
            }
        }
        System.exit(0);
    }
    private void LOG(){
        logging = true;
        FR4list.clear();
    }
    private void LOG_(){

    }
    private void SYMBOLS(String[] incomingArray){
        for(int i=1;i<incomingArray.length;i++){
            String data=incomingArray[i].toUpperCase();
            if(isalfasayı(data)){
                boolean varmıydı=false;
                for(String aa:symbolsList){
                    if(aa.equals(data)){
                        varmıydı=true;
                        break;
                    }
                }
                if(varmıydı){
                    System.out.println(data+" already exists");
                    fr4ekleme(data+" already exists");
                }else{
                    symbolsList.add(data);
                }
            }else{
                System.out.println(data+" is not alphanumeric");
                fr4ekleme(data +" is not alphanumeric");
            }
        }
    }
    private void SYMBOLS_(){
        System.out.print("SYMBOLS: ");
        for(String aa:symbolsList){
            System.out.print(aa+" ");
        }
        System.out.println();
    }
    private void STATES(String[] incomingArray ){
        for(int i=1;i< incomingArray.length;i++){
            if(isalphanumeric(incomingArray[i].toUpperCase())){
                if(statesList.isEmpty()){
                    statesList.add(incomingArray[i].toUpperCase());
                    initialState=incomingArray[i].toUpperCase();
                    System.out.println(incomingArray[i].toUpperCase()+" set es initial state ");
                    fr4ekleme(incomingArray[i].toUpperCase()+" set es initial state ");
                    continue;
                }
                boolean varmıydı=false;
                for(String aa:statesList){
                    if(aa.equals(incomingArray[i].toUpperCase())){
                        varmıydı=true;
                        break;
                    }
                }
                if(varmıydı){
                    System.out.println(incomingArray[i].toUpperCase()+" already exists");
                    fr4ekleme(incomingArray[i].toUpperCase()+" already exists");
                    continue;
                }
                statesList.add(incomingArray[i].toUpperCase());

            }else{
                System.out.println(incomingArray[i]+" is not alphanumeric");
                fr4ekleme(incomingArray[i]+" is not alphanumeric");
            }
        }
    }
    private void STATES_()
    {
        System.out.println(statesList);
    }
    private void INITIAL_STATE(){
        String state = commandArray[1].toUpperCase();

        if (!state.matches("[a-zA-Z0-9]+")) {
            System.out.println("Warning: state is not alphanumeric");
            fr4ekleme("Warning: state is not alphanumeric");
            return;
        }
        if (!statesList.contains(state)) {
            statesList.add(state);
            System.out.println("Warning: state has not been declared yet, added to list");
            fr4ekleme("Warning: state has not been declared yet, added to list");
        }
        initialState = state;
    }
    private void FINAL_STATES(){
        String states = commandEntered.substring(commandEntered.indexOf(" ") + 1).trim().toUpperCase();
        String[] stateArray = states.split("[,\\s]+"); // hem boşluk hem virgül ile ayırıyor artık

        for (String state : stateArray) {
            state = state.trim();

            if (!state.matches("[a-zA-Z0-9]+")) {
                System.out.println("Warning: invalid final state name: " + state);
                fr4ekleme("Warning: invalid final state name: " + state);
                continue;
            }

            if (!statesList.contains(state)) {

                System.out.println("Warning: final state not declared previously, added to states list: " + state);
                fr4ekleme("Warning: final state not declared previously, added to states list: " + state);
                statesList.add(state);
            }

            if (!finalStates.contains(state)) {
                finalStates.add(state);
            }
        }
    }
    private void TRANSITIONS(String gelenkomut){
        String errorline="";
        boolean varmıydı1=false;
        String fr9line=gelenkomut.substring(11);
        fr9line=fr9line.trim();//String[] fr9linedizisi=fr9line.split(",");//a q1 q2//   d e f   k l m
        String[] fr9linedizisi=fr9line.split(",");//a q1 q2//   d e f   k l m
        for(String aa:fr9linedizisi){
            aa=aa.trim();
            String[]anlıkdizi=aa.split(" ");
            if(!symbolsList.contains(anlıkdizi[0].toUpperCase())){
                errorline+="invalid symbols "+anlıkdizi[0]+". ";
            }
            if(!statesList.contains(anlıkdizi[1].toUpperCase())){
                errorline+="invalid state "+anlıkdizi[1]+". ";
            }
            if(!statesList.contains(anlıkdizi[2].toUpperCase())){
                errorline+="invalid state "+anlıkdizi[2]+". ";
            }
            if(symbolsList.contains(anlıkdizi[0].toUpperCase()) && statesList.contains(anlıkdizi[1].toUpperCase()) && statesList.contains(anlıkdizi[2].toUpperCase())){
                String line1=anlıkdizi[0].toUpperCase()+anlıkdizi[1].toUpperCase();
                String line2=anlıkdizi[2].toUpperCase();
                varmıydı1=false;
                if(transitionsList.isEmpty() && transitionsList1.isEmpty()){
                    transitionsList.add(line1);
                    transitionsList1.add(line2);
                    System.out.println("eklendi");

                }else{
                    for(String cc:transitionsList){
                        if(cc.equals(line1)){
                            String xx=cc.substring(0,1);
                            String kk=cc.substring(1,cc.length());
                            errorline+="transition already exists for <"+xx+","+kk+">. ";
                            int a =transitionsList.indexOf(line1);
                            transitionsList1.set(a,line2);
                            varmıydı1=true;
                            break;
                        }
                    }
                    if(!varmıydı1){
                        transitionsList.add(line1);
                        transitionsList1.add(line2);
                        System.out.println("eklendi");
                    }
                }
            }else {
                varmıydı1=true;
            }
        }
        if(varmıydı1){
            System.out.println("ERROR:  "+errorline);
        }
    }
    private void PRINT()
    {
        StringBuilder output = new StringBuilder();

        if(commandArray.length==1) {
            output.append("SYMBOLS {");
            for (int i = 0; i < symbolsList.size(); i++) {
                output.append(symbolsList.get(i));
                if (i < symbolsList.size() - 1) output.append(",");
            }
            output.append("}\n");

            output.append("STATES {");
            for (int i = 0; i < statesList.size(); i++) {
                output.append(statesList.get(i));
                if (i < statesList.size() - 1) output.append(",");
            }
            output.append("}\n");

            output.append("INITIAL STATE ").append(initialState).append("\n");

            output.append("FINAL STATES {");
            for (int i = 0; i < finalStates.size(); i++) {
                output.append(finalStates.get(i));
                if (i < finalStates.size() - 1) output.append(",");
            }
            output.append("}\n");

            output.append("TRANSITIONS {");
            for (int i = 0; i < transitionsList.size(); i++) {
                String xx = transitionsList.get(i).substring(0, 1);
                String kk = transitionsList.get(i).substring(1, transitionsList.get(i).length());
                output.append(xx + " " + kk + " " + transitionsList1.get(i) + "  ");
                if (i < transitionsList.size() - 1) output.append(",");

            }
            output.append("}\n");


            String result = output.toString();
            System.out.print(result);
            fr4ekleme(result);
        }

        if (commandArray.length == 2) {
            if(!commandArray[1].endsWith(".txt")) commandArray[1] += ".txt";
            String filename = commandArray[1];
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
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

                if(!finalStates.isEmpty()) {
                    writer.write("FINAL-STATES ");
                    for (String state : finalStates) writer.write(state + " ");
                    writer.write(";\n");
                }


                if(!transitionsList.isEmpty()) {
                    writer.write("TRANSITIONS");
                    writer.write(" ");

                    for(int i=0;i<transitionsList.size();i++){
                        int k=transitionsList.size()-1;
                        String line="";
                        line+=transitionsList.get(i).substring(0,1);

                        line+=" ";
                        line+=transitionsList.get(i).substring(1,transitionsList.get(i).length());

                        line+=" ";
                        line+=transitionsList1.get(i);
                        if(k!=i){
                            line+=",";

                        }
                        writer.write(line);


                    }

                    writer.write(";\n");
                }


                System.out.println("FSM data written to file: " + filename);
                fr4ekleme("FSM data written to file: " + filename);
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
                fr4ekleme("Error writing to file: " + e.getMessage());
            }
        }
    }
    private void COMPILE(String fileName)
    {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName)))
        {
            if(!Files.exists(Paths.get(fileName))) Files.createFile(Paths.get(fileName));
            out.writeObject(this);
            out.flush();
            System.out.println("Datas are serialized and to " + fileName + " written.");
        }
        catch (InvalidClassException e)
        {
            System.out.println("Warning: Invalid Class! " + e.getMessage());
        }
        catch (NotSerializableException e)
        {
            System.out.println("Warning: Not Serializable! " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("Warning: Something went wrong! " + e.getMessage());
        }
    }
    private void load(String fileName)
    {
        if (fileName.endsWith(".txt"))
        {
            processCommandsFromFile(fileName);
        }
        else
        {
            FSM readFSM = null;
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
                readFSM = (FSM) in.readObject();
            }
            catch (FileNotFoundException e)
            {
                System.out.println("Warning: File not found!");
            }
            catch (InvalidClassException e)
            {
                System.out.println("Warning: Version not compatible." );
            }
            catch (StreamCorruptedException e)
            {
                System.out.println("Warning: File corrupted.");
            }
            catch (OptionalDataException e)
            {
                System.out.println("Warning: Unexpected data found.");
            }
            catch (ClassNotFoundException e)
            {
                System.out.println("Warning: Class not found.");
            }
            catch (IOException e)
            {
                System.out.println("Warning: Something went wrong!");
            }

            if (readFSM == null) {
                System.out.println("Error: Failed to load FSM from file.");
                return;
            }
            this.setSymbolsList(readFSM.getSymbolsList());
            this.setStatesList(readFSM.getStatesList());
            this.setInitialState(readFSM.getInitialState());
            this.setFinalStates(readFSM.getFinalStates());
            this.setTransitionsList(readFSM.getTransitionsList());
            this.setLogging(readFSM.getLogging());
            System.out.println("Object loading successful!");
        }
    }
    private void CLEAR(){
        initialState = "";
        statesList.clear();
        symbolsList.clear();
        finalStates.clear();
        transitionsList.clear();

        System.out.println("All FSM data cleared.");
        fr4ekleme("All FSM data cleared.");
    }
    private void EXECUTE(){
        if (commandArray.length != 2) {
            System.out.println("EXECUTE requires an input string.");
            return;
        }

        String inputString = commandArray[1].toUpperCase();
        String currentState = initialState;

        System.out.print(currentState + " ");
        fr4ekleme(currentState);

        for (char symbol : inputString.toCharArray()) {
            String sym = String.valueOf(symbol);
            boolean found = false;

            for (String transition : transitionsList) {
                String[] parts = transition.split("\\s+");
                if (parts.length != 3) continue;

                if (parts[0].equals(sym) && parts[1].equals(currentState)) {
                    currentState = parts[2];
                    System.out.print(currentState + " ");
                    fr4ekleme(currentState);
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("NO");
                fr4ekleme("NO");
                return;
            }
        }

        if (finalStates.contains(currentState)) {
            System.out.println("YES");
            fr4ekleme("YES");
        } else {
            System.out.println("NO");
            fr4ekleme("NO");
        }
    }
    private void fr4ekleme(String a)
    {
        if(logging){
            FR4list.add(a);
        }
    }
    public static boolean isalphanumeric(String input) {
        if(input.length()==1){
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
    public static boolean isalfasayı(String input){
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
    public ArrayList<String> getTransitionsList() {
        return transitionsList;
    }
    public void setTransitionsList(ArrayList<String> transitionsList) {
        this.transitionsList = transitionsList;
    }
    public ArrayList<String> getFinalStates() {
        return finalStates;
    }
    public void setFinalStates(ArrayList<String> finalStates) {
        this.finalStates = finalStates;
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
        return logging;
    }
    public void setLogging(boolean logging) {
        this.logging = logging;
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
class InvalidInputException extends RuntimeException {
    public InvalidInputException(String culprit) {
        super("Warning: Invalid Input " + culprit);
    }
}
