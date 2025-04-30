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
    static String buyukharfler="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static String rakamlar="0123456789";
    static String[] harflerdızı=buyukharfler.split("");
    static String[]rakamlardızı=rakamlar.split("");
    static String alfanumaerik=buyukharfler+rakamlar;
    static String[] alfanumerikdizi=alfanumaerik.split("");

    
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
                dosyaAdı=commandArray[1].substring(1,commandArray[1].length()-1);
                LOG();
            }
            else if (commandArray[0].equals("SYMBOLS")) {
                SYMBOLS();
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
            System.out.println("yazıldı");
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
    public static void SYMBOLS(){
        for (int i = 1; i < commandArray.length; i++) {
            boolean alfanumerikmi = false;
            boolean zatenvarmı = false;
            System.out.println("araştırılan : " + commandArray[i].toUpperCase());
            for (String aa : alfanumerikdizi) {//alfanmerik mi diye baktık
                if (aa.equals(commandArray[i].toUpperCase())) {
                    alfanumerikmi = true;
                    break;
                }
            }
            if (alfanumerikmi) {
                if (symbolsList.isEmpty()) {
                    symbolsList.add(commandArray[i].toUpperCase());
                    System.out.println("eklendi: " + commandArray[i].toUpperCase());
                } else {
                    for (String aa : symbolsList) {
                        if (aa.equals(commandArray[i].toUpperCase())) {
                            System.out.println("daha önceden eklenmiş : " + commandArray[i].toUpperCase());
                            fr4ekleme("daha önceden eklenmiş : " + commandArray[i].toUpperCase());
                            zatenvarmı = true;
                            break;
                        }
                    }
                    if (!zatenvarmı) {
                        symbolsList.add(commandArray[i].toUpperCase());
                        System.out.println("eklendi : " + commandArray[i].toUpperCase());
                    }
                }
            } else {
                System.out.println("alfanumerik değil : " + commandArray[i].toUpperCase());
                fr4ekleme("alfanumerik değil : " + commandArray[i].toUpperCase());
            }
        }
    }//fr5
    public static void SYMBOLS_(){
        System.out.println("******* SYMBOLS LİST *******");
        for(String aa:symbolsList){
            System.out.println(aa);
        }
        System.out.println("******* SYMBOLS LİST *******");
    }//fr
    public static void STATES(String[] gelendizi ){
        for (int i = 1; i < gelendizi.length; i++) {
            boolean stateilkdogrumu=false;
            boolean stateikidogrumu=false;
            boolean zatenvar=false;
            String[]fr6kelimeler=gelendizi[i].split("");
            if(fr6kelimeler.length!=2){
                System.out.println(gelendizi[i]+"  uzunluk hatası");
                continue;
            }
            for(String aa:harflerdızı){
                if(aa.equals(fr6kelimeler[0].toUpperCase())){
                    stateilkdogrumu=true;
                    break;
                }
            }
            if(!stateilkdogrumu){
                System.out.println("state ilk harf degıl");
                continue;
            }
            for(String aa:rakamlardızı){
                if(aa.equals(fr6kelimeler[1])){
                    stateikidogrumu=true;
                    break;
                }
            }
            if(!stateikidogrumu){
                System.out.println("state 2 rakam değil");
                continue;
            }
            if(statesList.isEmpty()){
                statesList.add(gelendizi[i].toUpperCase());
                System.out.println("eklendi "+gelendizi[i].toUpperCase());
            }else{
                for(String aa:statesList){
                    if(gelendizi[i].toUpperCase().equals(aa)){
                        zatenvar=true;
                        break;
                    }
                }
                if(zatenvar){
                    System.out.println("zaten var "+gelendizi[i].toUpperCase());
                }else{
                    statesList.add(gelendizi[i].toUpperCase());
                    System.out.println("eklendi "+gelendizi[i].toUpperCase());
                }
            }


        }
    }//fr6
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

    //fr8
    public static void FINAL_STATES(){
        // Girilen final stateleri commandArray[1]'den alıyor
        String states = commandArray[1];

        // Virgülle ayır (örnek: Q1,Q2,Q3 gibi girilecek)
        String[] stateArray = states.split(",");

        for (String state : stateArray) {
            state = state.trim();

            if (!state.matches("[a-zA-Z0-9]+")) { // Alfanümerik kontrol
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
            if (fileName == null || fileName.trim().isEmpty()) {
                throw new InvalidFileNameException("File name cannot be null or empty.");
            }
            out.writeObject(data);
            System.out.println("Datas are serialized and to " + fileName + " written.");
        }
        catch (FileNotFoundException e1)
        {
            System.out.println("OS cannot work on this type of fileName");
        }

        catch (InvalidFileNameException e2)
        {
            System.out.println(e2.getMessage());
        }
        catch (IOException e3)
        {
            e3.printStackTrace();
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
            symbolsList = data.symbolsList;
            finalStates = data.finalStates;
            transitionsList = data.transitionsList;
            System.out.println("Datas from " + fileName + "  are read");
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
class InvalidFileNameException extends Exception
{
    public InvalidFileNameException(String message)
    {
        super(message);
    }
}

class FileCreationException extends Exception
{
    public FileCreationException(String message)
    {
        super(message);
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
        return input.matches("[a-zA-Z0-9]+");
    }
}
