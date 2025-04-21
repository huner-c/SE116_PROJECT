import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    static String girilenkomut="";
    static String[]akmoutdizisi=null;
    static boolean kayıt=false;
    static ArrayList<String> FR4list=new ArrayList<>();//response eklendiği yer


    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        while (true){
            System.out.print("? ");
            String komut=sc.nextLine();
            //FR2 START
            girilenkomut="";
            String[]fr2dizisi=komut.split("");
            boolean noktalıvirgülvarmı=false;
            for(String aa:fr2dizisi){
                if(aa.equals(";")){
                    noktalıvirgülvarmı=true;
                    break;
                }
                girilenkomut+=aa;
            }
            if(!noktalıvirgülvarmı){
                continue;
            }
            girilenkomut=girilenkomut.trim();
            akmoutdizisi=girilenkomut.split(" ");
            yönlendirme();
        }
    }
    public static void yönlendirme(){
        if(akmoutdizisi.length>=2) {
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
            else if(akmoutdizisi[0].equals("COMPILE")&& akmoutdizisi.length==2){
                FR11();
            }
            else if(akmoutdizisi[0].equals("LOAD")&& akmoutdizisi.length==2){
                FR13();
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
    public static void FR7(){}
    public static void FR8(){}
    public static void FR9(){}
    public static void FR10(){}
    public static void FR11(){}
    public static void FR12(){}
    public static void FR13(){}
    public static void FR14(){}








    public static void fr4ekleme(String a){
        if(kayıt){
            FR4list.add(a);
        }

    }



}
