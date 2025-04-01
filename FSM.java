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
    static List<String> mainStates = new ArrayList<>();
    static Map<Map<String, String>, String> mainTransitions = new HashMap<>();



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
            insaat.append(oAnkiLine);
            if(oAnkiLine.contains(";"))
            {
                System.out.println("; Tespit edildi");
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
        if(insaEdilmisString.contains("STATES") && !insaEdilmisString.contains("FINAL-STATES"))
        {
            STATES(insaEdilmisString);
        }
        if(insaEdilmisString.contains("TRANSITIONS"))
        {
            TRANSITIONS(insaEdilmisString);
        }
        if(insaEdilmisString.contains("CLEAR"))
        {
            CLEAR();
        }
        if(insaEdilmisString.contains("EXECUTE"))
        {
            EXECUTE(insaEdilmisString);
        }

    }

    public static void LOAD(){} // ilk once asadaki
    public static void COMPILE(){} //bu napiyor mk
    public static void LOG(){}

    public static void EXECUTE(String takeSymbolsFromHere)
    {
        System.out.println("Executing...");
        String[] parts = takeSymbolsFromHere.split("EXECUTE");
        String executableSymbols = parts[1].replaceAll("\\s+", "").replace(";","");
        String[] handleOnebyOne = executableSymbols.split("");

        String curretState = mainInitialState;
        Map<String,String> dumassTransition = new HashMap<>();


        for (int i = 0; i < handleOnebyOne.length; i++)
        {
            dumassTransition.put(handleOnebyOne[i], curretState);
            if(mainTransitions.get(dumassTransition) != null)
            {
                curretState = mainTransitions.get(dumassTransition);
                dumassTransition.clear();
                System.out.print(curretState+ " ");
            }
        }
        if(mainFinalStates.contains(curretState))
        {
            System.out.print("YES");
            System.out.println();
        }
        else
        {
            System.out.print("NO");
            System.out.println();
        }
    }

    public static void TRANSITIONS(String takeTransitionsFromHere)
    {
        String[] fakeArray1 = takeTransitionsFromHere.split("TRANSITIONS",2);
        String fakeString1 = fakeArray1[1].trim().replace(";","");
        String[] fakeArray2 = fakeString1.split(", ");

        String bir;
        String iki;
        String uc;

        for(String s : fakeArray2)
        {
            String[] dumassArray = s.split(" ");
            bir = dumassArray[0];
            iki = dumassArray[1];
            uc = dumassArray[2];
            Map<String,String> onlyTransition = new HashMap<>();
            onlyTransition.put(bir,iki);
            mainTransitions.put(onlyTransition,uc);
        }
        System.out.println("Transitions eklendi");
        System.out.println(mainTransitions);
    }
    public static void CLEAR()
    {
        mainStates.clear();
        mainFinalStates.clear();
        mainSymbols.clear();
        mainInitialState = null;
        mainTransitions.clear();
        System.out.println("Everything is swapped");
    }
    public static void PRINT()
    {
        System.out.println("SYMBOLS: " + mainSymbols);
        System.out.println("STATES: " + mainStates);
        System.out.println("INITIAL STATE: " + mainInitialState);
        System.out.println("FINAL STATES: " + mainFinalStates);
        System.out.println("TRANSITIONS: " + mainTransitions);
    }

    public static void FINAL_STATES(String takeFStateFromHere)
    {
        String[] parts = takeFStateFromHere.split("FINAL-STATES");
        String sagTaraf = parts[1].trim().replace(";","");
        Set<String> fakeFStates = new HashSet<>(Arrays.asList(sagTaraf.split("\\s+")));
        mainFinalStates.addAll(fakeFStates);
        mainStates.addAll(fakeFStates);
        System.out.println("Final Stateler Eklendi");
    }
    public static void INITIAL_STATE(String takeIStateFromHere)
    {
        String[] parts = takeIStateFromHere.split("INITIAL-STATE");
        mainInitialState = parts[1].replaceAll("\\s+", "").replace(";","");
        mainStates.add(mainInitialState);
        System.out.println("Initial State Belirlendi");
    }
    public static void EXIT()
    {
        System.out.println("TERMINATED BY USER");
        System.exit(0);
    }

    public static void SYMBOLS(String takeSymbolsFromHere)
    {
        String[] parts = takeSymbolsFromHere.split("SYMBOLS",2);
        String sagTaraf = parts[1].trim().replace(";","");
        List<String> fakeSymbols =  Arrays.asList(sagTaraf.split(" "));
        mainSymbols.addAll(fakeSymbols);
        System.out.println("Semboller eklendi");
    }

    public static void STATES(String takeStatesFromHere)
    {
        if (takeStatesFromHere.contains(" "))
        {
            String[] parts = takeStatesFromHere.split("STATES",2);
            String sagTaraf = parts[1].trim().replace(";","");
            List<String> fakeStates =  Arrays.asList(sagTaraf.split(" "));
            mainStates.addAll(fakeStates);
            System.out.println("Stateler eklendi");
        }
        else
        {
            System.out.println(mainStates);
        }

    }

    public static void satirYaz(String yazilacakSey)
    {
        try (FileWriter writer = new FileWriter("everything.txt", true))
        {
            writer.write(yazilacakSey + "\n");
            //System.out.println("Dosyaya başarıyla yazıldı!");
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
