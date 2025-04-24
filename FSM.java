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
    static boolean kayıt=false;
    static ArrayList<String> FR4list=new ArrayList<>();//response eklendiği yer

    // fsm için gerekli ek değişkenler(fr7 için)
    static String initialState = "";
    static ArrayList<String> statesList = new ArrayList<>();

    //fr10 için ek değişkenler
    static ArrayList<String> symbolsList = new ArrayList<>();
    static ArrayList<String> finalStates = new ArrayList<>();
    static ArrayList<String> transitionsList = new ArrayList<>();


    public static void main(String[] args)
    {
        codeExoskeleton();
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
                LOG(fr4kelime2dizisi);
            }
            else if (commandArray[0].equals("SYMBOLS")) {
                SYMBOLS();
            }
            else if (commandArray[0].equals("STATES")){
                STATES();
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
            else if(commandArray[0].equals("COMPILE")&& commandArray.length==2)
            {
                COMPILE(commandArray[1]);
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
        }else{
            if (commandEntered.equals("")) {
                VERSION_CONTROL();
            }
            else if (commandEntered.equals("EXIT")) {
                EXIT();
            }
            else if (commandEntered.equals("LOG")) {
                LOG_();
            }
            else if (commandEntered.equals("SYMBOLS")) {
                SYMBOLS_();
            }
            else if (commandEntered.equals("STATES")) {
                STATES_();
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
        System.out.println("FSM DESIGNER <3.0beta A>  "+formattedDateTime);
    }
    public static void EXIT() //EXIT A     //fr3
    {
        System.out.println("TERMINATED BY USER");
        System.exit(0);
    }
    public static void LOG(String[] a){}//fr4
    public static void LOG_(){}//fr4
    public static void SYMBOLS(){}//fr5
    public static void SYMBOLS_(){}//fr
    public static void STATES(){}//fr6
    public static void STATES_(){}//fr6
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
    }
    public static void FINAL_STATES(){}//fr8
    public static void TRANSITIONS(){}//fr9
    public static void PRINT()//fr10
    {
        System.out.println("SYMBOLS: " + symbolsList);
        System.out.println("STATES: " + statesList);
        System.out.println("INITIAL STATE: " + initialState);
        System.out.println("FINAL STATES: " + finalStates);
        System.out.println("TRANSITIONS: " + transitionsList);
    }
    public static void COMPILE(String fileName)//fr11
    {
        FSMDatas data = new FSMDatas(initialState,statesList,symbolsList,finalStates,transitionsList);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName)))
        {
            out.writeObject(data);
            System.out.println("Datas are serialized and to " + fileName + " written.");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void CLEAR(){}//fr12
    public static void LOAD(String fileName)//fr13
    {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName)))
        {
            FSMDatas data = (FSMDatas) in.readObject();
            initialState = data.initialState;
            statesList = data.statesList;
            System.out.println("Datas are to " + fileName + " written");
        }
        catch (ClassNotFoundException | IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void EXECUTE(){}//fr14
    //FR 15

    public static void fr4ekleme(String a){
        if(kayıt){
            FR4list.add(a);
        }
    }
}
