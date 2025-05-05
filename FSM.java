import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class FSMDatas implements Serializable
{
    String initialState;
    ArrayList<String> statesList;
    ArrayList<String> symbolsList;
    ArrayList<String> finalStates;
    ArrayList<String> transitionsList;

    public FSMDatas(String initialState, ArrayList<String> statesList, ArrayList<String> symbolsList, ArrayList<String> finalStates, ArrayList<String> transitionsList)
    {
        this.initialState = initialState;
        this.statesList = statesList;
        this.symbolsList = symbolsList;
        this.finalStates = finalStates;
        this.transitionsList = transitionsList;
    }
}
public class Main
{
    static String commandEntered="";
    static String[] commandArray =null;

    //fr4 için ek kısımlar
    static Formatter f_fr4=null;
    static boolean kayıt=false;
    static ArrayList<String> FR4list=new ArrayList<>();//response eklendiği yer
    static String dosyaAdı="";

    //fr5 için ek kısımlar
    


    // fsm için gerekli ek değişkenler(fr7 için)
    static String initialState = "";
    static ArrayList<String> statesList = new ArrayList<>();

    //fr10 için ek değişkenler
    static ArrayList<String> symbolsList = new ArrayList<>();
    static ArrayList<String> finalStates = new ArrayList<>();
    static ArrayList<String> transitionsList = new ArrayList<>();


    public static void main(String[] args)
    {
        VERSION_CONTROL();
        if (args.length > 0)
        {
            processCommandsFromFile(args[0]);
        }
        else
        {
            codeExoskeleton();
        }

    }
    public static void processCommandsFromFile(String fileName)
    {
        try {
            if (fileName == null || fileName.trim().isEmpty())
            {
                fr4ekleme("File name cannot be null or empty.");
                throw new InvalidFileNameException("File name cannot be null or empty.");
            }
            if (fileName.matches(".*[<>:\"/\\|?*].*") || fileName.contains("\0"))
            {
                fr4ekleme("File name contains invalid characters: " + fileName);
                throw new InvalidFileNameException("File name contains invalid characters: " + fileName);
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName)))
            {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    if (line.trim().isEmpty())
                    {
                        continue;
                    }
                    if (line.contains(";"))
                    {
                        commandEntered = line.replaceAll("\n", " ").split(";", 2)[0].trim();
                        commandArray = commandEntered.split(" ");
                        hub();
                    }
                    else
                    {
                        System.out.println("Invalid command format (missing semicolon): " + line);
                        fr4ekleme("Invalid command format (missing semicolon): " + line);
                    }
                }
            }
            catch (FileNotFoundException e)
            {
                throw new FileAccessException("Cannot access the file: " + fileName + ". File not found.");
            }
            catch (IOException e)
            {
                throw new FileAccessException("Error while reading the file: " + fileName + ". Reason: " + e.getMessage());
            }
        }
        catch (InvalidFileNameException | FileAccessException e)
        {
            System.out.println(e.getMessage());
            fr4ekleme(e.getMessage());
        }
    }


    public static void codeExoskeleton()
    {
        Scanner sc = new Scanner(System.in);
        StringBuilder builder = new StringBuilder(); // Çok satırlı komutları birleştirmek için
        while (true)
        {
            System.out.print("? ");
            String currentLine = sc.nextLine();
            builder.append(currentLine);

            if (currentLine.contains(";"))
            {
                commandEntered = builder.toString();
                commandEntered = commandEntered.replaceAll("\n", " ");
                commandEntered = commandEntered.split(";", 2)[0].trim();
                commandArray = commandEntered.split(" ");
                hub();
                builder.setLength(0);
            }
        }
    }
    public static void hub()
    {
        System.out.println(Arrays.toString(commandArray));

        if(commandArray.length>=2)
        {
            String[] fr4kelime2dizisi = commandArray[1].split("");

            if(commandArray[0].equals("LOG") && commandArray[1].startsWith("<") && commandArray[1].endsWith(">")){
                dosyaAdı=commandArray[1].substring(1,commandArray[1].length()-1);
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
                TRANSITIONS();
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
                LOAD(commandArray[1]);
            }
            else if(commandArray[0].equals("EXECUTE")){
                EXECUTE();
            }
            else {
                System.out.println("invalid comment");
                fr4ekleme("invalid comment");
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
                System.out.println("invalid comment");
                fr4ekleme("invalid comment");
            }
        }
    }
    public static void VERSION_CONTROL()//fr1
    {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        fr4ekleme("FSM DESIGNER <Update 03.05 10.52>  "+formattedDateTime);
        System.out.println("FSM DESIGNER <Update 03.05 10.52>  "+formattedDateTime);
    }
    public static void EXIT() //EXIT A     //fr3
    {
        fr4ekleme("TERMINATED BY USER");
        System.out.println("TERMINATED BY USER");
        System.exit(0);
    }
    public static void LOG(){
        kayıt = true;
        FR4list.clear();
    }//fr4
    public static void LOG_(){
        try {
            f_fr4 = new Formatter(dosyaAdı+".txt");
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
    }//fr4
    public static void SYMBOLS(String[] incomingArray){
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
    }//fr
    public static void SYMBOLS_(){
        System.out.print("SYMBOLS: ");
        for(String aa:symbolsList){
            System.out.print(aa+" ");
        }
        System.out.println();

    }
    public static void STATES(String[] incomingArray ){
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
    }//fr6

    public static void INITIAL_STATE(){
        String state = commandArray[1];

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
    }//fr8
    public static void FINAL_STATES(){
        String states = commandEntered.substring(commandEntered.indexOf(" ") + 1).trim(); 
    String[] stateArray = states.split("[,\\s]+"); // hem boşluk hem virgül ile ayırıyor artık

    for (String state : stateArray) {
        state = state.trim();

        if (!state.matches("[a-zA-Z0-9]+")) {
            System.out.println("Warning: invalid final state name: " + state);
            fr4ekleme("Warning: invalid final state name: " + state);
            continue;
        }

        if (!statesList.contains(state)) {
            statesList.add(state);
            System.out.println("Warning: final state not declared previously, added to states list: " + state);
            fr4ekleme("Warning: final state not declared previously, added to states list: " + state);
        }

        if (!finalStates.contains(state)) {
            finalStates.add(state);
        }
    }
    }
    public static void TRANSITIONS(){
        String input = commandArray[1];

        if (!input.contains("-") || !input.contains(">")) {
            System.out.println("Invalid transition format. Use: FROM-SYMBOL>TO");
            fr4ekleme("Invalid transition format. Use: FROM-SYMBOL>TO");
            return;
        }

        String[] parts = input.split("[-|>]");
        if (parts.length != 3) {
            System.out.println("Transition must be in the format FROM-SYMBOL>TO");
            fr4ekleme("Transition must be in the format FROM-SYMBOL>TO");
            return;
        }

        String fromState = parts[0].toUpperCase();
        String symbol = parts[1].toUpperCase();
        String toState = parts[2].toUpperCase();

        if (!statesList.contains(fromState)) {
            System.out.println("FROM state not defined: " + fromState);
            fr4ekleme("FROM state not defined: " + fromState);
            return;
        }

        if (!statesList.contains(toState)) {
            System.out.println("TO state not defined: " + toState);
            fr4ekleme("TO state not defined: " + toState);
            return;
        }

        if (!symbolsList.contains(symbol)) {
            System.out.println("SYMBOL not defined: " + symbol);
            fr4ekleme("SYMBOL not defined: " + symbol);
            return;
        }

        String transition = fromState + "-" + symbol + ">" + toState;

        if (transitionsList.contains(transition)) {
            System.out.println("Transition already exists: " + transition);
            fr4ekleme("Transition already exists: " + transition);
            return;
        }

        transitionsList.add(transition);
        System.out.println("Transition added: " + transition);
        fr4ekleme("Transition added: " + transition);
    }//fr9
    public static void PRINT()//fr10
    {
        StringBuilder output = new StringBuilder();
        output.append("SYMBOLS: ").append(symbolsList).append("\n");
        output.append("STATES: ").append(statesList).append("\n");
        output.append("INITIAL STATE: ").append(initialState).append("\n");
        output.append("FINAL STATES: ").append(finalStates).append("\n");
        output.append("TRANSITIONS: ").append(transitionsList).append("\n");

        String result = output.toString();

        System.out.print(result);
        fr4ekleme(result);

        //dosya ismi verilmisse yazdirir
        if (commandArray.length == 2) {
            String filename = commandArray[1];
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                writer.write(result);
                System.out.println("FSM data written to file: " + filename);
                fr4ekleme("FSM data written to file: " + filename);
            } catch (IOException e) {
                System.out.println("Error writing to file: " + e.getMessage());
                fr4ekleme("Error writing to file: " + e.getMessage());
            }
        }
    }



    public static void COMPILE(String fileName)//fr11
    {
        if (fileName == null || fileName.trim().isEmpty())
        {
            fr4ekleme("File name cannot be null or empty.");
            throw new InvalidFileNameException("File name cannot be null or empty.");
        }
        if (fileName.matches(".*[<>:\"/\\|?*].*") || fileName.contains("\0"))
        {
            fr4ekleme("File name contains invalid characters: ");
            throw new InvalidFileNameException("File name contains invalid characters: " + fileName);
        }

        FSMDatas data = new FSMDatas(initialState,statesList,symbolsList,finalStates,transitionsList);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName)))
        {
            out.writeObject(data);
            fr4ekleme("Datas are serialized and to " + fileName + " written.");
            System.out.println("Datas are serialized and to " + fileName + " written.");
        }
        catch (FileNotFoundException e1)
        {
            fr4ekleme("Cannot create or access the file: " + fileName + ". Reason: " + e1.getMessage());
            throw new FileCreationException("Cannot create or access the file: " + fileName + ". Reason: " + e1.getMessage());
        }
        catch (IOException e)
        {
            fr4ekleme("Error while writing to the file: " + fileName + ". Reason: " + e.getMessage());
            throw new FileCreationException("Error while writing to the file: " + fileName + ". Reason: " + e.getMessage());
        }
    }

    public static void CLEAR(){
        initialState = "";
        statesList.clear();
        symbolsList.clear();
        finalStates.clear();
        transitionsList.clear();

        System.out.println("All FSM data cleared.");
        fr4ekleme("All FSM data cleared.");
    }//fr12
    public static void LOAD(String fileName)//fr13
    {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName)))
        {
            FSMDatas data = (FSMDatas) in.readObject();
            initialState = data.initialState;
            statesList = data.statesList;
            symbolsList = data.symbolsList;
            finalStates = data.finalStates;
            transitionsList = data.transitionsList;
            System.out.println("Datas from " + fileName + "  are read");
            fr4ekleme("Datas from " + fileName + " are read");
        }
        catch (ClassNotFoundException | IOException e)
        {
            System.out.println("Error while loading data: " + e.getMessage());
            fr4ekleme("Error while loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void EXECUTE(){
         Scanner sc = new Scanner(System.in);
        System.out.print("Enter the input string to execute: ");
        String inputString = sc.nextLine().trim().toUpperCase();

        String currentState = initialState;
        System.out.println("Starting execution from: " + currentState);
        fr4ekleme("Starting execution from: " + currentState);

        for (char symbol : inputString.toCharArray()) {
            String sym = String.valueOf(symbol);
            boolean transitionFound = false;

            for (String transition : transitionsList) {
                String[] parts = transition.split("[-|>]");
                String fromState = parts[0];
                String transSymbol = parts[1];
                String toState = parts[2];

                if (fromState.equals(currentState) && transSymbol.equals(sym)) {
                    System.out.println("Transition: " + currentState + " -" + sym + "-> " + toState);
                    fr4ekleme("Transition: " + currentState + " -" + sym + "-> " + toState);
                    currentState = toState;
                    transitionFound = true;
                    break;
                }
            }

            if (!transitionFound) {
                System.out.println("Rejected: No transition found for symbol '" + sym + "' from state " + currentState);
                fr4ekleme("Rejected: No transition found for symbol '" + sym + "' from state " + currentState);
                return;
            }
        }

        if (finalStates.contains(currentState)) {
            System.out.println("Accepted! Final state reached: " + currentState);
            fr4ekleme("Accepted! Final state reached: " + currentState);
        } else {
            System.out.println("Rejected: Final state not reached. Stopped at " + currentState);
            fr4ekleme("Rejected: Final state not reached. Stopped at " + currentState);
        }
    }//fr14
    //FR 15

    public static void fr4ekleme(String a){
        if(kayıt){
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
