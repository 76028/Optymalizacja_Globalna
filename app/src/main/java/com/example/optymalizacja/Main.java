package com.example.optymalizacja;

import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.*;

public class Main {

    static int ileOsobnikow = 10;
    static int ilePunktow = 10;
    static Random r = new Random();

    public static void main(String[] args) {

        Struktura s = new Struktura(ileOsobnikow, ilePunktow);
        sukcesjaTrywialna(s);
    }

    static void sukcesjaElitarna(Struktura s) {
        int iloscEpok = 10;
        List<List<Integer>> osobniki = new ArrayList<>();
        kopiuj_liste(s.osobniki, osobniki);
        List<List<Integer>> poselkcji;
        List<List<Integer>> mutacja;
        List<List<Integer>> inwersja;
        List<List<Integer>> transpozycja;
        List<List<Integer>> krzyzowanie;
        // Wyświetlenie współrzędnych punktów/miast po których będzie poruszał się komiwojażer
        System.out.println("\n----Punkty (Miasta)----");
        s.wyswietlPunkty();
        // Wyświetlenie populacji osobników wraz z ich trasami i długościami tras
        Map<Double, List<Integer>> mapa = new LinkedHashMap<>();
        osobniki_dlugosci(mapa, s.osobniki, s.dlugosciTras);
        System.out.println("\n----Osobnik + długość trasy----");
        mapa.forEach((key, value) -> System.out.println(value + " " + key));
        // Iteracja po epokach
        for (int i = 0; i < iloscEpok; i++) {
            System.out.println("\n-----EPOKA " + (i + 1) + "-----");
            poselkcji = selekcja(mapa, s.dlugosciTras);
            mapa.clear();
            System.out.println("----PO SELEKCJI----");
            poselkcji.forEach(System.out::println);
            // Zastosowanie operatorów genetycznych na populacji po selekcji i wyświetlenie tych danych
            mutacja = mutacja(poselkcji);
            inwersja = inwersja(poselkcji);
            transpozycja = transpozycja(poselkcji);
            krzyzowanie = krzyzowanieRownomierne(poselkcji);
            System.out.println("----Z MUTACJI----");
            mutacja.forEach(System.out::println);
            System.out.println("----Z INWERSJI----");
            inwersja.forEach(System.out::println);
            System.out.println("----Z TRANSPOZYCJI----");
            transpozycja.forEach(System.out::println);
            System.out.println("----Z KRZYŻOWANIA----");
            krzyzowanie.forEach(System.out::println);
            // Dodanie do populacji do eliminacji tylko zmienionych w wyniku działania operatorów genetycznych osobników
            for (int j = 0; j < ileOsobnikow; j++) {
                if (!mutacja.get(j).equals(poselkcji.get(j))) osobniki.add(mutacja.get(j));
                if (!inwersja.get(j).equals(poselkcji.get(j))) osobniki.add(inwersja.get(j));
                if (!transpozycja.get(j).equals(poselkcji.get(j))) osobniki.add(transpozycja.get(j));
                if (!krzyzowanie.get(j).equals(poselkcji.get(j))) osobniki.add(krzyzowanie.get(j));
            }
            // Wyświetlenie wszystkich osobników (populacja przed selekcją oraz osobniki zmienione w wyniku działania operatorów genetycznych) oraz ilość tych osobników
            System.out.println("----WSZYSTKIE OSOBNIKI (" + osobniki.size() + ")----");
            s.osobniki = osobniki;
            s.generujTrasy(osobniki.size());
            s.dlugosciTras(osobniki.size());
            osobniki_dlugosci(mapa, s.osobniki, s.dlugosciTras);
            // 3 METODY USUWANIA OSOBNIKÓW

            // ELIMINACJA
//            // Wybranie początkowej liczby osobników z populacji do eliminacji
//            // posortowanie mapy
//            TreeMap<Double, List<Integer>> mapa1 = new TreeMap<>(mapa);
//            mapa1.forEach((key, value) -> System.out.println(value + " " + key));
//            // wyodrębnienie odpowiedniej liczby osobników poprzez usunięcie niepotrzebnych
//            while(mapa1.size()!=ileOsobnikow) mapa1.pollLastEntry();
//            System.out.println("----OSOBNIKI PO USUNIĘCIU----");
//            mapa1.forEach((key, value) -> System.out.println(value + " " + key));
//            mapa.clear();
//            mapa = new LinkedHashMap(mapa1);
//            mapa1.clear();
//            osobniki.clear();
//            mapa.forEach((key, value) -> osobniki.add(value));
//            s.osobniki = osobniki;
//            s.generujTrasy();
//            s.dlugosciTras();

              // LOSOWE
//            // Wybranie początkowej liczby osobników z populacji do usnięcia losowaego
//            mapa.forEach((key, value) -> System.out.println(value + " " + key));
//            ArrayList<Double> dlugosciTras = new ArrayList<Double>();
//            for(double d : s.dlugosciTras) dlugosciTras.add(d);
//            while(mapa.size()!=ileOsobnikow) {
//                mapa.remove(dlugosciTras.remove(r.nextInt(mapa.size())));
//            }
//            System.out.println("----OSOBNIKI PO UNUNIĘCIU----");
//            mapa.forEach((key, value) -> System.out.println(value + " " + key));
//            osobniki.clear();
//            mapa.forEach((key, value) -> osobniki.add(value));
//            s.osobniki = osobniki;
//            s.generujTrasy();
//            s.dlugosciTras();

            // ZE ŚCISKIEM
            // Wybranie początkowej liczby osobników do usnięcia ze ściskiem
            TreeMap<Double, List<Integer>> mapa1 = new TreeMap<>(mapa);
            mapa1.forEach((key, value) -> System.out.println(value + " " + key));
            // usunięcie podobnych osobników (o podobnej długości trasy)
            int ile_podobnych;
            double roznica = 0.001;
            ArrayList<Double> dlugosciTras = new ArrayList<Double>();
            for (double d : s.dlugosciTras) dlugosciTras.add(d);
            while (mapa1.size() != ileOsobnikow) {
                for (int x = 0; x < mapa1.size() - 1; x++) {
                    ile_podobnych = 0;
                    for (int y = x + 1; y < mapa1.size(); y++) {
                        if (Math.abs(dlugosciTras.get(x) - dlugosciTras.get(y)) < roznica) ile_podobnych += 1;
                    }
                    if (ile_podobnych == 1) {
                        mapa1.remove(dlugosciTras.remove(x+1));
                    }
                }
                roznica += 0.001;
            }
            System.out.println("----OSOBNIKI PO USUNIĘCIU----");
            mapa1.forEach((key, value) -> System.out.println(value + " " + key));
            mapa.clear();
            mapa = new LinkedHashMap(mapa1);
            mapa1.clear();
            osobniki.clear();
            mapa.forEach((key, value) -> osobniki.add(value));
            s.osobniki = osobniki;
            s.generujTrasy();
            s.dlugosciTras();
            // Czyszczenie zmiennych
            poselkcji.clear();
            mutacja.clear();
            inwersja.clear();
            transpozycja.clear();
            krzyzowanie.clear();
        }
    }

    // Sukcjesja trywialna
    static void sukcesjaTrywialna(Struktura s) {
        int iloscEpok = 10;
        // Wyświetlenie współrzędnych punktów/miast po których będzie poruszał się komiwojażer
        System.out.println("\n----Punkty (Miasta)----");
        s.wyswietlPunkty();
        // Wyświetlenie populacji osobników wraz z ich trasami i długościami tras
        Map<Double, List<Integer>> mapa = new LinkedHashMap<>();
        osobniki_dlugosci(mapa, s.osobniki, s.dlugosciTras);
        System.out.println("\n----Osobnik + długość trasy----");
        mapa.forEach((key, value) -> System.out.println(value + " " + key));
        // Iteracja po epokach
        for (int i = 0; i < iloscEpok; i++) {
            // Zastosowanie selecji i kolejnych operatorów genetycznych na populacji
            System.out.println("\n\n-----EPOKA " + (i + 1) + "-----");
            System.out.println("----SELEKCJA----");
            s.osobniki = selekcja(mapa, s.dlugosciTras);
            s.wyswietlOsobniki();
            System.out.println("\n----MUTACJA----");
            s.osobniki = mutacja(s.osobniki);
            s.wyswietlOsobniki();
            System.out.println("\n----INWERSJA----");
            s.osobniki = inwersja(s.osobniki);
            s.wyswietlOsobniki();
            System.out.println("\n----TRANSPOZYCJA----");
            s.osobniki = transpozycja(s.osobniki);
            s.wyswietlOsobniki();
            System.out.println("\n----KRZYŻOWANIE----");
            s.osobniki = krzyzowanieRownomierne(s.osobniki);
            s.wyswietlOsobniki();
            // ocena
            s.generujTrasy();
            s.dlugosciTras();
            mapa.clear();
            osobniki_dlugosci(mapa, s.osobniki, s.dlugosciTras);
        }
        System.out.println("\n----PO SUKCESJI----");
        mapa.forEach((key, value) -> System.out.println(value + " " + key));
    }

    // Transpozyjc genów osobnika na podstawie prawdopodobieństwa
    static List<List<Integer>> transpozycja(List<List<Integer>> osobniki) {
        List<List<Integer>> ztranspozycjonowane = new ArrayList<>();
        kopiuj_liste(osobniki, ztranspozycjonowane);
        double PT = 0.2;
        int a, b, x1, x2;
        // Iteracja po osobnikach
        for (int i = 0; i < ileOsobnikow; i++) {
            // Działanie na osobniku zależnie od ustalonego prawdopodbieństwa transpozycji
            if (r.nextInt() < PT) {
                // wylosowanie dwóch indeksów do podmiany
                a = r.nextInt(ileOsobnikow);
                b = r.nextInt(ileOsobnikow);
                while (a == b) b = r.nextInt(ileOsobnikow);
                // pobranie z osobnika wartości genu na wylosowanym indeksie
                x1 = osobniki.get(i).get(a);
                x2 = osobniki.get(i).get(b);
                // jeśli jest możliwość przeprowadzania transpozycji tzn. na danym indeksie może zostać podmieniona wartość zgodnie z założeniami reprezetnacji porządkowej zostaje dokonana transpozycja
                if (x1 <= ilePunktow - 1 - b && x2 <= ilePunktow - 1 - a) {
                    //System.out.println("Transpozycja wiersz(" + i + "): x1(" + a + ")=" + x1 + " x2(" + b + ")=" + x2);
                    ztranspozycjonowane.get(i).set(a, x2);
                    ztranspozycjonowane.get(i).set(b, x1);
                }
            }
        }
        return ztranspozycjonowane;
    }

    // Inwersja genów w osobniku na podstawie prawdopodobieństwa
    static List<List<Integer>> inwersja(List<List<Integer>> osobniki) {
        List<List<Integer>> zinwersowane = new ArrayList<>();
        kopiuj_liste(osobniki, zinwersowane);
        List<Integer> temp;
        List<Integer> temp2;

        double PI = 0.2;
        int a, b;
        boolean flag = true;
        for (int i = 0; i < ileOsobnikow; i++) {
            flag = true;
            if (r.nextDouble() < PI) {
                a = r.nextInt(ileOsobnikow - 1);
                b = r.nextInt(ileOsobnikow - 1);
                while (a >= b) {
                    a = r.nextInt(ileOsobnikow - 1);
                    b = r.nextInt(ileOsobnikow - 1);
                }
                // a=3 b=7
                temp = new ArrayList<Integer>(osobniki.get(i).subList(a, b));
                Collections.reverse(temp);
                temp2 = new ArrayList<Integer>(temp);
                // sprawdzenie czy inwersja jest możliwa
                for (int j = a; j < b; j++) {
                    if (temp2.remove(0) > (ilePunktow - 1 - j)) {
                        flag = false;
                    }
                }
                // jeśli możliwa inwersja
                if (flag) {
                    // System.out.println("osobnik="+i+" a="+a+" b="+b);
                    for (int j = a; j < b; j++) {
                        zinwersowane.get(i).set(j, temp.remove(0));
                    }
                }
            }
        }
        return zinwersowane;
    }

    // Zamiana wartości dla mutacji
    static int zamien(int wartosc, int zakres) {
        int zwroc = r.nextInt(zakres);
        while (zwroc == wartosc) {
            zwroc = r.nextInt(zakres);
        }
        return zwroc;
    }

    // Skopiowanie zawartości jednej listy do drugiej
    public static void kopiuj_liste(List<List<Integer>> a, List<List<Integer>> b) {
        for (List<Integer> podlisty : a) {
            List<Integer> temp = new ArrayList<>();
            for (Integer elementy : podlisty) {
                temp.add(elementy);
            }
            b.add(temp);
        }
    }

    // Mutacja
    static List<List<Integer>> mutacja(List<List<Integer>> osobniki) {
        List<List<Integer>> zmutowane = new ArrayList<>();
        kopiuj_liste(osobniki, zmutowane);
        double PM = 0.2;
        for (int i = 0; i < ileOsobnikow; i++) {
            for (int j = 0; j < ilePunktow - 1; j++) {
                if (r.nextDouble() < PM) {
                    zmutowane.get(i).set(j, zamien(osobniki.get(i).get(j), ilePunktow - j));
                }
            }
        }
        return zmutowane;
    }

    // Reprezentacja osobnika w postaci [[geny osobnika] dlugosc trasy]
    static void osobniki_dlugosci(Map<Double, List<Integer>> mapa, List<List<Integer>> osobniki, double[] dlugosci_tras) {
        for (int i = 0; i < osobniki.size(); i++) {
            for (int j = 0; j < ilePunktow; j++) {
                mapa.put(dlugosci_tras[i], osobniki.get(i));
            }
        }
    }

    // Selekcja zwraca osobniki po przejściu wybranej metody selekcji
    static List<List<Integer>> selekcja(Map<Double, List<Integer>> mapa, double[] dlugosciTras) {
//        double []posortowane = rankingowa(dlugosciTras);
//        double[] posortowane = turniejowa(dlugosciTras);
        double[] posortowane = ruletki(dlugosciTras);
        List<List<Integer>> po_selekcji = new ArrayList<>();
        for (int i = 0; i < ileOsobnikow; i++) {
            po_selekcji.add(mapa.get(posortowane[i]));
        }
        return po_selekcji;
    }

    // Metoda selekcji rankingowa
    static double[] rankingowa(double[] dlugosciTras) {
        double temp;
        double[] tab_rankingowa = new double[ileOsobnikow];
        for (int i = 0; i < ileOsobnikow - 1; i++) {
            for (int j = 0; j < ileOsobnikow - i - 1; j++) {
                if (dlugosciTras[j] > dlugosciTras[j + 1]) {
                    temp = dlugosciTras[j];
                    dlugosciTras[j] = dlugosciTras[j + 1];
                    dlugosciTras[j + 1] = temp;
                }
            }
        }
        for (int i = 0; i < ileOsobnikow; i++) {
            tab_rankingowa[i] = dlugosciTras[r.nextInt((r.nextInt(ileOsobnikow)) + 1)];
        }
        return tab_rankingowa;
    }

    // Metoda selekcji turniejowa
    static double[] turniejowa(double[] dlugosciTras) {
        ArrayList<Integer> wylosowane; // lista wylosowanych indeksów w danej grupie
        double[] grupa_turniejowa = new double[2]; // 2-ososobowe grupy osobników
        double[] tab_turniejowa = new double[ileOsobnikow]; //końcowe osobniki wylosowane w turnieju

        for (int i = 0; i < ileOsobnikow; i++) {
            wylosowane = losuj(); // losowanie indeksów 2 osobników
            for (int j = 0; j < 2; j++) {
                grupa_turniejowa[j] = dlugosciTras[wylosowane.get(j)];
            }
            if (grupa_turniejowa[0] < grupa_turniejowa[1]) tab_turniejowa[i] = grupa_turniejowa[0];
            else tab_turniejowa[i] = grupa_turniejowa[1];
        }
        return tab_turniejowa;
    }

    // Metoda selekcji ruletki
    static double[] ruletki(double[] dlugosciTras) {
        double[] tab_rankingowa = new double[ileOsobnikow];
        double[] qi = new double[ileOsobnikow]; // dystrybuanta i-tego osobnika
        double[] pi = new double[ileOsobnikow]; // prawdobodobieństwo i-tego osobnika
        double F = 0; // wartość dopasowania całkowitego
        double q = 0; // pomocnicza zmienna do summowania kolejnych dystrybuant
        for (Double d : dlugosciTras) F += d;
        for (int i = 0; i < ileOsobnikow; i++) {
            pi[i] = (1.0 - (dlugosciTras[i] / F)) / (ileOsobnikow - 1);
            q += pi[i];
            qi[i] = q;
        }
        for (int i = 0; i < ileOsobnikow; i++) {
            double los = r.nextDouble();
            int j = 0;
            while (j < ileOsobnikow) {
                if (los <= qi[j]) {
                    tab_rankingowa[i] = dlugosciTras[j];
                    break;
                }
                j++;
            }
        }
        return tab_rankingowa;
    }

    // Losowanie 2 indeksów osobników bez zwracania
    static ArrayList<Integer> losuj() {
        ArrayList<Integer> wylosowane = new ArrayList<Integer>();
        int liczba;

        wylosowane.add(r.nextInt(ileOsobnikow));
        while (wylosowane.size() != 2) {
            liczba = r.nextInt(ileOsobnikow);
            if (liczba != wylosowane.get(0)) wylosowane.add(liczba);
        }
        return wylosowane;
    }

    // Zwrócenie par osobników do krzyżowania przy założonym prawdopodobieństwie
    static List<List<Integer>> wybierzPary(List<List<Integer>> osobniki) {
        // wybranie osobników na podstawie prawdopodobieństwa krzyżowania
        List<List<Integer>> wylosowane = new ArrayList<>();
        double PK = 0.1;
        for (int i = 0; i < ileOsobnikow; i++) {
            if (r.nextDouble() < PK) {
                wylosowane.add(osobniki.get(i));
            }
        }
        if (wylosowane.size() % 2 == 1) {
            wylosowane.remove(r.nextInt(wylosowane.size()));
        }
        int ile = wylosowane.size();
        // losowanie pary z wybranych osobników
        List<List<Integer>> pary = new ArrayList<>();
        for (int i = 0; i < ile; i++) {
            pary.add(wylosowane.remove(r.nextInt(wylosowane.size())));
        }
        System.out.println(pary);
        return pary;
    }

    static List<Integer> wybierzPary2() {
        List<Integer> wylosowane = new ArrayList<>();
        double PK = 0.5;
        // Wylosowanie indeksów na podstawie prawdopodobieństwa krzyżowania
        for (int i = 0; i < ileOsobnikow; i++) {
            if (r.nextDouble() < PK) {
                wylosowane.add(i);
            }
        }
        // Usunięcie losowego indeksu w przypadku nieparzystej liczby
        if (wylosowane.size() % 2 == 1) wylosowane.remove(r.nextInt(wylosowane.size()));
        // Zwrócenie losowo ułożonych wybranych indexów reprezentujących kolejne pary
        List<Integer> pary = new ArrayList<>();
        int ile = wylosowane.size();
        for (int i = 0; i < ile; i++) {
            pary.add(wylosowane.remove(r.nextInt(wylosowane.size())));
        }
        //System.out.println(pary);
        return pary;
    }

    // Krzyżowanie równomierne
    static List<List<Integer>> krzyzowanieRownomierne(List<List<Integer>> osobniki) {
        List<List<Integer>> pokrzyzowane = new ArrayList<>();
        kopiuj_liste(osobniki, pokrzyzowane);
        // Lista indexy przechowuje indeksy wylosowanych par (1,3,4,5) gdzie 1,3 to 1 para a 4,5 kolejna itd.
        List<Integer> indexy = wybierzPary2();
        int wzorzec;
        for (int i = 0; i < indexy.size(); i += 2) {
            for (int j = 0; j < ilePunktow; j++) {
                // Wzorzec jest losowany dla każdego osobnika a zmienna wzorzec przechowuje 0/1 dla kolejnego punktu w osobniku
                wzorzec = r.nextInt(2);
                // Geny, a nie całe osobniki są podmieniane w populacji wynikowej
                // zgodnie z założeniami krzyżowania równomiernego, jeśli 0 to potemek 1 dostaje gen z rodzica 1 a potomek 2 dostaje gen z rodzica 2
                if (wzorzec == 0) {
                    pokrzyzowane.get(indexy.get(i)).set(j, osobniki.get(indexy.get(i)).get(j));
                    pokrzyzowane.get(indexy.get(i + 1)).set(j, osobniki.get(indexy.get(i + 1)).get(j));
                // jeśli 1 to potomek 1 dostaje gen z rodzica 2 a potemek 2 dostaje gen z rodzica 2
                } else {
                    pokrzyzowane.get(indexy.get(i)).set(j, osobniki.get(indexy.get(i + 1)).get(j));
                    pokrzyzowane.get(indexy.get(i + 1)).set(j, osobniki.get(indexy.get(i)).get(j));
                }
            }
        }
        return pokrzyzowane;
    }

    // Krzyżowanie wielopunktowe (zróbta coś z tym)
    static void krzyzowanieWielopunktowe(List<List<Integer>> osobniki) {
        int temp;
        int ilosc = 1;
        int temp_punkt;
        ArrayList<Integer> punkty = new ArrayList<Integer>();
        for (int i = 0; i < osobniki.size(); i += 2) {
            // Losowanie punktów i (onych) ilości dla każdej pary
            ilosc = r.nextInt(ilePunktow - 1) + 1;
            for (int j = 0; j < ilosc; j++) {
                temp_punkt = r.nextInt(ileOsobnikow);
                while (punkty.contains(temp_punkt)) {
                    temp_punkt = r.nextInt(ileOsobnikow);
                }
                punkty.add(temp_punkt);
            }
            Collections.sort(punkty);
            System.out.println("osobnik" + osobniki.get(i));
            System.out.println("osobnik" + osobniki.get(i + 1));
            System.out.println(punkty);
            //////////////////////////////////////////////////////////////////////

            List<Integer> osobnik = osobniki.get(i);
            List<Integer> osobnik2 = osobniki.get(i + 1);
            List<Integer> wers1;
            List<Integer> wers2;
            int x1, x2;
            // if(punkty.size()%2==0){
            for (int k = 0; k < punkty.size(); k++) {
                if (k == punkty.size() - 1) {
                    x1 = punkty.get(k);
                    x2 = ilePunktow;
                } else {
                    x1 = punkty.get(k);
                    x2 = punkty.get(k + 1);
                }
                wers1 = osobnik.subList(x1, x2);
                wers2 = osobnik2.subList(x1, x2);
                System.out.println("wersy " + wers1.toString());
                System.out.println("wersy " + wers2.toString());

                temp = 0;
                for (int j = x1; j < x2; j++) {
                    osobnik.set(j, wers2.get(temp));
                    osobnik2.set(j, wers1.get(temp));
                    temp++;
                }
            }
            // }
            punkty.clear();
        }
        //punkty.clear();
        //return null;
    }
}
