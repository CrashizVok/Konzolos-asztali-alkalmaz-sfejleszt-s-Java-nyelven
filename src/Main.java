import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static ArrayList<Bookings> bookings = new ArrayList<>();
    static Map<Integer, List<Bookings>> foglalasok = new TreeMap<>(Collections.reverseOrder());
    static Set<String> emailok = new HashSet<>();

    public static void main(String[] args) throws IOException {
        // READ #START
        System.out.println("Foglalasok feldolgozása...");
        System.out.println("=========================");
        read();
        leghoszabbFoglals();
        System.out.println(" ");
        leghoszabbFoglalasAdatai();
        System.out.println(" ");
        leghoszabbFoglalsrendezes();
        System.out.println(" ");
        veletlenvendegfogadas();
        System.out.println(" ");
        legtobbszo();
        System.out.println(" ");
        evesbontas();
        System.out.println(" ");
        System.out.println(" ");
        mentes();
        System.out.println(" ");
        System.out.println("Program vége.");

    }

    public static void leghoszabbFoglals(){
        int index = 0;
        for (Bookings i: bookings) {
            index += 1;
        }

        System.out.println("1. Beolvasott foglalások szama: " + index);
    }

    public static void leghoszabbFoglalasAdatai() {
        int biggestDataSize = 0;
        for (Bookings i: bookings) {
            LocalDate start = LocalDate.parse(i.getStart_date());
            LocalDate end = LocalDate.parse(i.getEnd_date());

            int DateSize = start.datesUntil(end).toArray().length;
            if (biggestDataSize < DateSize) { biggestDataSize = DateSize; }


            //System.out.println(Arrays.toString(start.datesUntil(end).toArray()));
        }
        System.out.println("2. Leghosszabb foglalás adatai: ");
        for (Bookings i: bookings) {
            if (LocalDate.parse(i.getStart_date()).datesUntil(LocalDate.parse(i.getEnd_date())).toArray().length == biggestDataSize){
                System.out.println("Vendég: " + i.getLast_name() + " " + i.getFirst_name());
                System.out.println("Szálloda: " + i.getHotel_name() + "(" + i.getHotel_city() + ")");
                System.out.println("Időszak: " + i.getStart_date() + " - " + i.getEnd_date() + " (" + biggestDataSize + "nap" + ")");
                //System.out.println(" ");
                break;
            }
        }
    }

    public static void leghoszabbFoglalsrendezes(){
        for (Bookings i: bookings) {
            LocalDate start = LocalDate.parse(i.getStart_date());
            LocalDate end = LocalDate.parse(i.getEnd_date());

            if (start.getYear() == 2024){
                int DateSize = start.datesUntil(end).toArray().length;
                foglalasok.computeIfAbsent(DateSize, k -> new ArrayList<>()).add(i);
            }

        }
        //System.out.println(foglalasok);
        System.out.println("3. 2024-es foglalasok hossz szerint csökkenő sorrendben: ");
        for (Integer i : foglalasok.keySet()) {
            for (Bookings x: foglalasok.get(i)) {
                System.out.println(x.getFirst_name() + " " + x.getLast_name() + " - " + x.getHotel_name() + " (" + i + "nap)");
            }
        }

    }

    public static void veletlenvendegfogadas(){
        for (Bookings i: bookings) {
            emailok.add(i.getCustomer_email());
        }
        System.out.println("Összes egyedi vendég száma: " + emailok.size());

        List<String> list = new ArrayList<String>(emailok);
        int randomIndex = (int) (Math.random() * emailok.size());
        //System.out.println(list.get(randomIndex));
        String email = list.get(randomIndex);

        System.out.println("4. Véletlenszerű vendég: " + email);
        for (Bookings i: bookings) {
            if (Objects.equals(i.getCustomer_email(), email)) System.out.println(i.getHotel_name() + "(" + i.getStart_date() + ")");
        }
    }

    public static void legtobbszo(){
        String hotelName = "";
        String hotelDesc = "";

        System.out.println("5. Legtöbb szóból álló hotel leírás: ");
        int biggestNum = 0;
        for (Bookings i: bookings){
            if (legtobbszoHeler(i.getHotel_description()) > biggestNum) {
                biggestNum = legtobbszoHeler(i.getHotel_description());
                hotelName = i.getHotel_name();
                hotelDesc = i.getHotel_description();
            }
        }
        System.out.println("Szálloda: " + hotelName);
        System.out.println("Leírás: " + hotelDesc);
        System.out.println("Szavak száma: " + biggestNum);
    }

    public static void evesbontas(){
        TreeSet<Integer> years = new TreeSet<>();
        ArrayList<Integer> yearNum = new ArrayList<>();
        ArrayList<String> templateYear = new ArrayList<>();

        for (Bookings i: bookings){
            LocalDate year = LocalDate.parse(i.getStart_date());
            years.add(year.getYear());
        }

        for (int yeeet : years){
            int number = 0;
            for (Bookings i: bookings){
                LocalDate year = LocalDate.parse(i.getStart_date());
                if (year.getYear() == yeeet){
                    number += 1;
                }
            }
            yearNum.add(number);
        }
        List<Integer> asd = new ArrayList<>(years);
        for (int i = 0; i < years.size(); i++){
            templateYear.add(String.valueOf(asd.get(i)));
            templateYear.add(": ");
            templateYear.add(String.valueOf(yearNum.get(i)));
            templateYear.add(", ");
            //System.out.println(asd.get(i) + ": " + yearNum.get(i));
        }

        if (!templateYear.isEmpty()) {
            templateYear.removeLast();
        }
        System.out.println("6. Éves bontásban a foglalasok száma: ");
        for (String i : templateYear){
            System.out.print(i);
        }
    }

    public static int legtobbszoHeler(String leiras){
        String[] splittedLeiras = leiras.split(" ");
        return splittedLeiras.length;
    }


    public static void mentes() throws IOException {
        ArrayList<Bookings> keszek = new ArrayList<>();
        System.out.println("7. Rövid foglalasok mentése rovidek.txt fájlba:");

        for (Bookings i: bookings){
            LocalDate start = LocalDate.parse(i.getStart_date());
            LocalDate end = LocalDate.parse(i.getEnd_date());

            int DateSize = start.datesUntil(end).toArray().length;
            if (DateSize <= 1){
                keszek.add(i);
            }

        }

        List<String> output = (List<String>) keszek.stream()
                .map(b -> b.getCustomer_email()  + "," + b.getHotel_name() + "," + b.getStart_date() + "," + b.getEnd_date())
                .toList();

        Files.write(Path.of("rovidek.txt"), output);
        System.out.println("Mentve " + output.size() + " rövid foglalás.");

    }

    public static void read() throws FileNotFoundException {
        File file = new File("bookings.csv");
        Scanner scanner = new Scanner(file);
        scanner.nextLine();

        while (scanner.hasNextLine()){
            Bookings obj = new Bookings();
            String data = scanner.nextLine();
            String[] fineData = data.split(",");

            obj.setCustomer_email(fineData[0]);
            obj.setFirst_name(fineData[1]);
            obj.setLast_name(fineData[2]);
            obj.setPhone(fineData[3]);
            obj.setBirthday(fineData[4]);
            obj.setCustomer_image(fineData[5]);
            obj.setHotel_id(Integer.parseInt(fineData[6]));
            obj.setHotel_city(fineData[7]);
            obj.setHotel_name(fineData[8]);
            obj.setHotel_description(fineData[9]);
            obj.setHotel_stars(Integer.parseInt(fineData[10]));
            obj.setHotel_owner_email(fineData[11]);
            obj.setHotel_image(fineData[12]);
            obj.setStart_date(fineData[13]);
            obj.setEnd_date(fineData[14]);
            obj.setAdults(Integer.parseInt(fineData[15]));
            obj.setChildren(Integer.parseInt(fineData[16]));
            obj.setBoylovers(Integer.parseInt(fineData[17]));

            //System.out.println(fineData[0]);

            bookings.add(obj);

        }
    }
}