import java.io.*;
import java.util.*;

class FSMDatas implements Serializable
{
    String initialState;
    ArrayList<String> statesList;

    public FSMDatas(String initialState, ArrayList<String> statesList )
    {
        this.initialState = initialState;
        this.statesList = statesList;
    }
}


public class Main
{
    static String girilenkomut="";
    static String[]akmoutdizisi=null;
    static boolean kayıt=false;
    static ArrayList<String> FR4list=new ArrayList<>();//response eklendiği yer

    // fsm için gerekli ek değişkenler(fr7 için)
    static String initialState = "";
    static ArrayList<String> statesList = new ArrayList<>();


    public static void main(String[] args)
    {
        codeExoskeleton();
    }
    public static void codeExoskeleton()
    {
        Scanner sc = new Scanner(System.in);
        StringBuilder insaat = new StringBuilder(); // Çok satırlı komutları birleştirmek için
        while (true)
        {
            System.out.print("? ");
            String oAnkiLine = sc.nextLine();
            insaat.append(oAnkiLine);

            if (oAnkiLine.contains(";"))
            {
                girilenkomut = insaat.toString();
                girilenkomut = girilenkomut.replaceAll("\n", " ");
                girilenkomut = girilenkomut.split(";", 2)[0].trim();
                akmoutdizisi = girilenkomut.split(" ");
                yönlendirme();
                insaat.setLength(0);
            }
        }

    }

    public static void yönlendirme()
    {
        System.out.println(Arrays.toString(akmoutdizisi));

        if(akmoutdizisi.length>=2)
        {
            String[] fr4kelime2dizisi = akmoutdizisi[1].split("");

            if(akmoutdizisi[0].equals("LOG") && akmoutdizisi[1].startsWith("<") && akmoutdizisi[1].endsWith(">")){
                FR4(fr4kelime2dizisi);
            }
            else if (akmoutdizisi[0].equals("SYMBOLS")) {
                FR5();
            }
            else if (akmoutdizisi[0].equals("STATES")){
                FR6();
            }
            else if(akmoutdizisi[0].equals("INITIAL-STATE") && akmoutdizisi.length==2){
                FR7();
            }
            else if(akmoutdizisi[0].equals("FINAL-STATES")){
                FR8();
            }
            else if(akmoutdizisi[0].equals("TRANSITIONS")){
                FR9();
            }
            else if (akmoutdizisi[0].equals("PRINT") && akmoutdizisi.length==2){
                FR10();
            }
            else if(akmoutdizisi[0].equals("COMPILE")&& akmoutdizisi.length==2)
            {
                FR11(akmoutdizisi[1]);
            }
            else if(akmoutdizisi[0].equals("LOAD")&& akmoutdizisi.length==2){
                FR13(akmoutdizisi[1]);
            }
            else if(akmoutdizisi[0].equals("EXECUTE")){
                FR14();
            }
            else {
                System.out.println("invalid comment");
                fr4ekleme("invalid comment");
            }
        }else{
            if (girilenkomut.equals("")) {
                FR1();
            }
            else if (girilenkomut.equals("EXIT")) {
                FR3();
            }
            else if (girilenkomut.equals("LOG")) {
                FR4_2();
            }
            else if (girilenkomut.equals("SYMBOLS")) {
                FR5_2();
            }
            else if (girilenkomut.equals("STATES")) {
                FR6_2();
            }
            else if (girilenkomut.equals("CLEAR")) {
                FR12();
            }
            else if (girilenkomut.equals("PRINT"))
            {
                FR10();
            }
            else{
                System.out.println("invalid comment");
                fr4ekleme("invalid comment");
            }
        }
    }

    //KODUNUZU AŞŞAĞIDAKİ İLGİLİ FR KISMINA  YAZABİLİRSİNİZ
    //KULLANICAIDAN ALDIGIMIZ GİRDİ girilenkomut olarak alınıyor
    //LİST,MAP,SET VS HERHANGİ BİR ŞEY EKLEMEK İSTEDİĞİNİZDE Public Class Main in HEMEN AŞŞAĞISINA STATİC OLARAK EKLEYEBİLİRSİNİZ
    //LAZIM OLDUGUNDA EKSTRA METHODDA EKLEYEBİLİRSİNİZ AŞŞAĞI KISMA

    public static void FR1(){}
    public static void FR3(){}
    public static void FR4(String[] a){}
    public static void FR4_2(){}
    public static void FR5(){}
    public static void FR5_2(){}
    public static void FR6(){}
    public static void FR6_2(){}

    public static void FR7(){
        String state = akmoutdizisi[1];

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
    public static void FR8(){}
    public static void FR9(){}
    public static void FR10()
    {
        System.out.println(initialState);
        System.out.println(statesList);
    }
    public static void FR11(String fileName) // COMPILE
    {
        System.out.println("compile metot calisiyor");

        FSMDatas data = new FSMDatas(initialState,statesList);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName)))
        {
            out.writeObject(data);
            System.out.println("Veriler serileştirildi ve " + fileName + " dosyasına yazıldı.");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void FR12(){}
    public static void FR13(String fileName) //LOAD
    {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            FSMDatas data = (FSMDatas) in.readObject();
            initialState = data.initialState;
            statesList = data.statesList;
            System.out.println("Veriler " + fileName + " dosyasından yüklendi.");
        }
        catch (ClassNotFoundException | IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void FR14(){}

    public static void fr4ekleme(String a){
        if(kayıt){
            FR4list.add(a);
        }
    }
}
