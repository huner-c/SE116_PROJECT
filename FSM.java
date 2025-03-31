import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FSM
{
    static List<String> mainSymbols = new ArrayList<>();
    static String mainInitialState;
    static Set<String> mainFinalStates = new HashSet<>();

    public static void main(String[] args)
    {
        String logFileName = "everything.txt";
        dosyaOlustur(logFileName);



        StringBuilder insaat =  new StringBuilder();
        Scanner info = new Scanner(System.in);
        System.out.println("String insa et");
        while(true)
        {
            System.out.print("? ");
            String oAnkiLine = info.nextLine();
            //satirYaz(oAnkiLine.replaceAll("\\s", ""));//s tum bosluk karakterlerini ifade eder
            insaat.append(oAnkiLine);
            if(oAnkiLine.contains(";"))
            {
                System.out.println("; Tespit edildi");
                //insaat = new StringBuilder(insaat.toString().replace(";", ""));
                satirYaz(insaat.toString());
                hub(insaat.toString());
                insaat.setLength(0);

            }
        }
    }
    public static void  hub(String insaEdilmisString)
    {
        if(insaEdilmisString.contains("EXIT"))
        {
            EXIT();
        }
        if(insaEdilmisString.contains("SYMBOLS"))
        {
            SYMBOLS(insaEdilmisString);
        }
        if(insaEdilmisString.contains("PRINT"))
        {
            PRINT();
        }
        if(insaEdilmisString.contains("INITIAL-STATE"))
        {
            INITIAL_STATE(insaEdilmisString);
        }
        if(insaEdilmisString.contains("FINAL-STATES"))
        {
            FINAL_STATES(insaEdilmisString);
        }
    }

    public static void EXECUTE(){}
    public static void LOAD(){}
    public static void CLEAR(){}
    public static void COMPILE(){}
    public static void PRINT()
    {
        System.out.println(mainSymbols);
        System.out.println(mainInitialState);
        System.out.println(mainFinalStates);
    }
    public static void TRANSITIONS(){}
    public static void FINAL_STATES(String takeFStateFromHere)
    {
        String[] parts = takeFStateFromHere.split("FINAL-STATES");
        String sagTaraf = parts[1].trim().replace(";","");
        Set<String> fakeFStates = new HashSet<>(Arrays.asList(sagTaraf.split("\\s+")));
        mainFinalStates.addAll(fakeFStates);
        System.out.println("Final Stateler Eklendi");
    }
    public static void INITIAL_STATE(String takeIStateFromHere)
    {
        String[] parts = takeIStateFromHere.split("INITIAL-STATE");
        mainInitialState = parts[1].replaceAll("\\s+", "").replace(";","");
        System.out.println("Initial State Belirlendi");
    }
    public static void EXIT()
    {
        System.out.println("TERMINATED BY USER");
        System.exit(0);
    }
    public static void LOG(){}
    public static void SYMBOLS(String takeSymbolsFromHere)
    {
        String[] parts = takeSymbolsFromHere.split("SYMBOLS",2);
        String sagTaraf = parts[1].trim().replace(";","");
        List<String> fakeSymbols =  Arrays.asList(sagTaraf.split(" "));
        mainSymbols.addAll(fakeSymbols);
        System.out.println("Semboller eklendi");

    }

    public static void STATES(){}

    public static void satirYaz(String yazilacakSey)
    {
        try (FileWriter writer = new FileWriter("everything.txt", true))
        {
            writer.write(yazilacakSey + "\n");
            System.out.println("Dosyaya başarıyla yazıldı!");
        } catch (IOException ex) {
            System.err.println("Dosyaya yazarken hata oluştu: ");
            ex.printStackTrace();
        }
    }
    public static void dosyaOlustur(String logFileName)
    {
        try
        {
            Files.createFile(Path.of(logFileName));
            System.out.println("Dosya sorunsuz olusturuldu Dosyan hazir");

        }
        catch (FileAlreadyExistsException e) //eger bu hatayi alirsan git tekrardan ayni dosyayi ac dedim
        {
            System.out.println("Dosyan hazir");
        }
        catch (IOException e) {

            System.out.println("Dosya olusutururken hata alindi");
            e.printStackTrace();
        }
    }


}
