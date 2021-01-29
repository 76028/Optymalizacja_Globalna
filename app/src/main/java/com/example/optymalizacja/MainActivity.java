package com.example.optymalizacja;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btn_start, btn_wynik;
    EditText et_osobniki, et_punkty, et_ilosc_epok;
    TextView tv_wynik;
    static RadioGroup rg1, rg2, rg3;
    static int ileOsobnikow, ilePunktow, iloscEpok;
    static Random r = new Random();
    double min;
    Struktura s;
    static Switch sw1, sw2, sw3;
    static ArrayList<Osobnik> osobniki;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_osobniki = findViewById(R.id.et_osobniki);
        et_punkty = findViewById(R.id.et_punkty);
        et_ilosc_epok = findViewById(R.id.editTextNumber);
        btn_start = findViewById(R.id.btn_start);
        btn_wynik = findViewById(R.id.btn_wynik);
        tv_wynik = findViewById(R.id.tv_wynik);
        rg1 = findViewById(R.id.rg1);
        rg2 = findViewById(R.id.rg2);
        rg3 = findViewById(R.id.rg3);
        sw1 = findViewById(R.id.switch1);
        sw2 = findViewById(R.id.switch2);
        sw3 = findViewById(R.id.switch3);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClick(View v) {
        ileOsobnikow = Integer.parseInt(et_osobniki.getText().toString());
        ilePunktow = Integer.parseInt(et_punkty.getText().toString());
        s = new Struktura(ileOsobnikow, ilePunktow);
        osobniki = new ArrayList<Osobnik>();
        for (int i = 0; i < ileOsobnikow; i++) {
            osobniki.add(new Osobnik(s.dlugosciTras[i], s.osobniki.get(i)));
        }
        String wynik = "";
        int i = 0;
        for (Osobnik d : osobniki) {
            System.out.println(d);
            wynik += d.toString() + "\n";
        }
        min = 2;
        wynik = "Minimum: " + min + "\n\n" + wynik;
        tv_wynik.setText(wynik);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClick2(View v) {
        iloscEpok = Integer.parseInt(et_ilosc_epok.getText().toString());
        switch (rg3.getCheckedRadioButtonId()) {
            case R.id.radioButton10:
                sukcesjaTrywialna(s);
                break;
            case R.id.radioButton11:
                sukcesjaElitarna(s);
                break;
            case R.id.radioButton12:
                sukcesjaElitarna(s);
                break;
            case R.id.radioButton13:
                sukcesjaElitarna(s);
                break;
            default:
                sukcesjaElitarna(s);
        }

        String wynik = "";
        for (Osobnik o : osobniki) {
            wynik += o.toString() + "\n";
        }
        wynik = "Minimum: " + min + "\n\n" + wynik;
        tv_wynik.setText(wynik);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    static void sukcesjaElitarna(Struktura s) {

        List<List<Integer>> wynikowa = new ArrayList<>();
        kopiuj_liste(s.osobniki, wynikowa);
        List<List<Integer>> poselkcji;
        List<List<Integer>> mutacja;
        List<List<Integer>> inwersja;
        List<List<Integer>> transpozycja;
        List<List<Integer>> krzyzowanie;
        for (int i = 0; i < iloscEpok; i++) {
            System.out.println("\n-----EPOKA " + (i + 1) + "-----");
            poselkcji = selekcja(s.dlugosciTras);
            osobniki.clear();
            poselkcji.forEach(System.out::println);
            // Zastosowanie operatorów genetycznych na populacji po selekcji i wyświetlenie tych danych
            if (sw1.isChecked()) {
                mutacja = mutacja(poselkcji);
            } else {
                mutacja = null;
            }
            if (sw2.isChecked()) {
                inwersja = inwersja(poselkcji);
            } else {
                inwersja = null;
            }
            if (sw3.isChecked()) {
                transpozycja = transpozycja(poselkcji);
            } else {
                transpozycja = null;
            }

            switch (rg2.getCheckedRadioButtonId()) {
                case R.id.radioButton9:
                    krzyzowanie = krzyzowanieRownomierne(poselkcji);
                    break;
                case R.id.radioButton8:
                    krzyzowanie = krzyzowanieWielopunktowe(poselkcji);
                    break;
                default:
                    krzyzowanie = krzyzowanieRownomierne(poselkcji);
            }
            // Dodanie do populacji do eliminacji tylko zmienionych w wyniku działania operatorów genetycznych osobników
            for (int j = 0; j < ileOsobnikow; j++) {
                if (mutacja != null)
                    if (!mutacja.get(j).equals(poselkcji.get(j))) {
                        wynikowa.add(mutacja.get(j));
                    }
                if (inwersja != null)
                    if (!inwersja.get(j).equals(poselkcji.get(j))) {
                        wynikowa.add(inwersja.get(j));
                    }
                if (transpozycja != null)
                    if (!transpozycja.get(j).equals(poselkcji.get(j))) {
                        wynikowa.add(transpozycja.get(j));
                    }
                if (!krzyzowanie.get(j).equals(poselkcji.get(j))) {
                    wynikowa.add(krzyzowanie.get(j));
                }
            }
            //  wszystkie osobniki (populacja przed selekcją oraz osobniki zmienione w wyniku działania operatorów genetycznych)
            s.osobniki = wynikowa;
            s.generujTrasy(wynikowa.size());
            s.dlugosciTras(wynikowa.size());
            for (int j = 0; j < s.osobniki.size(); j++) {
                osobniki.add(new Osobnik(s.dlugosciTras[j], s.osobniki.get(j)));
            }
            // 3 METODY USUWANIA OSOBNIKÓW
            ArrayList<Double> dlugosciTras;
            switch (rg3.getCheckedRadioButtonId()) {
                case R.id.radioButton11:
                    // ELITARNA
                    Collections.sort(osobniki);
                    while(osobniki.size()!=ileOsobnikow) {
                        osobniki.remove(osobniki.size()-1);
                    }
                    wynikowa.clear();
                    for (int x = 0; x < ileOsobnikow; x++) {
                        wynikowa.add((List<Integer>) osobniki.get(x).geny);
                    }
                    s.osobniki = wynikowa;
                    s.generujTrasy();
                    s.dlugosciTras();
                    osobniki.clear();
                    for (int j = 0; j < s.osobniki.size(); j++) {
                        osobniki.add(new Osobnik(s.dlugosciTras[j], s.osobniki.get(j)));
                    }
                    break;
                case R.id.radioButton12:
                    // ZE SCISKIEM
                    int ile_podobnych;
                    double roznica = 0.001;
                    dlugosciTras = new ArrayList<Double>();
                    for (double d : s.dlugosciTras) {
                        dlugosciTras.add(d);
                    }
                    while (osobniki.size() != ileOsobnikow) {
                        for (int x = 0; x < osobniki.size() - 1; x++) {
                            ile_podobnych = 0;
                            for (int y = x + 1; y < osobniki.size(); y++) {
                                if (Math.abs(dlugosciTras.get(x) - dlugosciTras.get(y)) < roznica) {
                                    ile_podobnych += 1;
                                }
                            }
                            if (ile_podobnych == 1) {
                                osobniki.remove(x + 1);
                            }
                        }
                        roznica += 0.001;
                    }
                    wynikowa.clear();
                    for (int x = 0; x < ileOsobnikow; x++) {
                        wynikowa.add((List<Integer>) osobniki.get(x).geny);
                    }
                    s.osobniki = wynikowa;
                    s.generujTrasy();
                    s.dlugosciTras();
                    osobniki.clear();
                    for (int j = 0; j < s.osobniki.size(); j++) {
                        osobniki.add(new Osobnik(s.dlugosciTras[j], s.osobniki.get(j)));
                    }
                    break;
                case R.id.radioButton13:
                    // LOSOWA
                    while (osobniki.size() != ileOsobnikow) {
                        osobniki.remove(r.nextInt(osobniki.size()));
                    }
                    wynikowa.clear();
                    for (int x = 0; x < ileOsobnikow; x++) {
                        wynikowa.add((List<Integer>) osobniki.get(x).geny);
                    }
                    s.osobniki = wynikowa;
                    s.generujTrasy();
                    s.dlugosciTras();
                    osobniki.clear();
                    for (int j = 0; j < s.osobniki.size(); j++) {
                        osobniki.add(new Osobnik(s.dlugosciTras[j], s.osobniki.get(j)));
                    }
                    break;
            }
            // Czyszczenie zmiennych
            poselkcji.clear();
            if (mutacja != null)
                mutacja.clear();
            if (inwersja != null)
                inwersja.clear();
            if (transpozycja != null)
                transpozycja.clear();
            krzyzowanie.clear();
        }
    }

    // Sukcjesja trywialna
    @RequiresApi(api = Build.VERSION_CODES.N)
    static void sukcesjaTrywialna(Struktura s) {
        // Wyświetlenie współrzędnych punktów/miast po których będzie poruszał się komiwojażer
        System.out.println("\n----Punkty (Miasta)----");
        s.wyswietlPunkty();
        for (int i = 0; i < iloscEpok; i++) {
            // Zastosowanie selecji i kolejnych operatorów genetycznych na populacji
            System.out.println("\n\n-----EPOKA " + (i + 1) + "-----");
            s.osobniki = selekcja(s.dlugosciTras);
            // Zastosowanie odpowiednich operatorów genetycznych
            if (sw1.isChecked()) {
                s.osobniki = mutacja(s.osobniki);
            }
            if (sw2.isChecked()) {
                s.osobniki = inwersja(s.osobniki);
            }
            if (sw3.isChecked()) {
                s.osobniki = transpozycja(s.osobniki);
            }
            switch (rg2.getCheckedRadioButtonId()) {
                case R.id.radioButton9:
                    s.osobniki = krzyzowanieRownomierne(s.osobniki);
                    break;
                case R.id.radioButton8:
                    s.osobniki = krzyzowanieWielopunktowe(s.osobniki);
                    break;
                default:
                    s.osobniki = krzyzowanieWielopunktowe(s.osobniki);
            }
            // ocena
            s.generujTrasy();
            s.dlugosciTras();
            osobniki.clear();
            for (int j = 0; j < ileOsobnikow; j++) {
                osobniki.add(new Osobnik(s.dlugosciTras[j], s.osobniki.get(j)));
            }
        }
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
                while (a == b) {
                    b = r.nextInt(ileOsobnikow);
                }
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

    // Funckja wyliczająca najmniejszą odległość ze wszystkich możliwych permutacji tras
    static double permutacja(Struktura s) {
        List<Integer> osobnikpermutacja = new ArrayList<>();
        int[] licznik = new int[ilePunktow];
        int[] wartosc = new int[ilePunktow];
        double MIN = Integer.MAX_VALUE;
        double temp;
        Arrays.fill(licznik, 0);
        Arrays.fill(wartosc, 0);
        int silnia = silnia(ilePunktow);
        for (int i = 0; i < silnia; i++) {
            for (int j = 0; j < ilePunktow; j++) {
                osobnikpermutacja.add(wartosc[j]);
                licznik[j]++;
                if (licznik[j] >= j) {
                    licznik[j] = 0;
                    wartosc[j]++;
                    if (wartosc[j] > j) wartosc[j] = 0;
                }
            }
            Collections.reverse(osobnikpermutacja);
            System.out.println(osobnikpermutacja);
            temp = s.dlugoscTrasy(s.generujTrase(osobnikpermutacja));
            if (MIN > temp) MIN = temp;
            osobnikpermutacja.clear();
        }
        return MIN;
    }

    static int silnia(int i) {
        if (i == 0)
            return 1;
        else
            return i * silnia(i - 1);
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
    static List<List<Integer>> mutacja(List<List<Integer>> v_osobniki) {
        List<List<Integer>> zmutowane = new ArrayList<>();
        kopiuj_liste(v_osobniki, zmutowane);
        double PM = 0.2;
        for (int i = 0; i < ileOsobnikow; i++) {
            for (int j = 0; j < ilePunktow - 1; j++) {
                if (r.nextDouble() < PM) {
                    zmutowane.get(i).set(j, zamien(v_osobniki.get(i).get(j), ilePunktow - j));
                }
            }
        }
        return zmutowane;
    }

    // Selekcja zwraca osobniki po przejściu wybranej metody selekcji
    static List<List<Integer>> selekcja(double[] dlugosciTras) {
        double[] posortowane;

        switch (rg1.getCheckedRadioButtonId()) {
            case R.id.radioButton3:
                posortowane = turniejowa(dlugosciTras);
                break;
            case R.id.radioButton2:
                posortowane = rankingowa(dlugosciTras);
                break;
            case R.id.radioButton:
                posortowane = ruletki(dlugosciTras);
                break;
            default:
                posortowane = turniejowa(dlugosciTras);
        }

        List<List<Integer>> po_selekcji = new ArrayList<>();
        for (int i = 0; i < ileOsobnikow; i++) {
            po_selekcji.add(znajdzOsobnika(posortowane[i]));
        }
        return po_selekcji;
    }
    // funkcja zwraca osobnika na podstawie odległości
    static List<Integer> znajdzOsobnika(double d) {
        ArrayList<Integer> temp = null;
        for (Osobnik o : osobniki) {
            if (d == o.dlugosc) temp = (ArrayList<Integer>) o.geny;
        }
        return temp;
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
            if (grupa_turniejowa[0] < grupa_turniejowa[1]) {
                tab_turniejowa[i] = grupa_turniejowa[0];
            } else {
                tab_turniejowa[i] = grupa_turniejowa[1];
            }
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
        for (Double d : dlugosciTras) {
            F += d;
        }
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
            if (liczba != wylosowane.get(0)) {
                wylosowane.add(liczba);
            }
        }
        return wylosowane;
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
        if (wylosowane.size() % 2 == 1) {
            wylosowane.remove(r.nextInt(wylosowane.size()));
        }
        // Zwrócenie losowo ułożonych wybranych indexów reprezentujących kolejne pary
        List<Integer> pary = new ArrayList<>();
        int ile = wylosowane.size();
        for (int i = 0; i < ile; i++) {
            pary.add(wylosowane.remove(r.nextInt(wylosowane.size())));
        }
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

    // Krzyżowanie wielopunktowe
    static List<List<Integer>> krzyzowanieWielopunktowe(List<List<Integer>> osobniki) {
        int ilosc;
        int temp_punkt;
        ArrayList<Integer> punkty = new ArrayList<>();
        List<List<Integer>> pokrzyzowane = new ArrayList<>();
        kopiuj_liste(osobniki, pokrzyzowane);
        // Lista indexy przechowuje indeksy wylosowanych par (1,3,4,5) gdzie 1,3 to 1 para a 4,5 kolejna itd.
        List<Integer> indexy = wybierzPary2();
        for (int i = 0; i < indexy.size(); i += 2) {
            // Losowanie punktow dla każdej pary o wylosowanych indexach
            ilosc = r.nextInt(ilePunktow - 1) + 1; //zakres [1, ilePunktow - 1]
            for (int j = 0; j < ilosc; j++) {
                temp_punkt = r.nextInt(ileOsobnikow);
                while (punkty.contains(temp_punkt)) {
                    temp_punkt = r.nextInt(ileOsobnikow);
                }
                punkty.add(temp_punkt);
            }
            Collections.sort(punkty); // posortowane rosnaco

            for (int j = 0; j < punkty.size(); j += 2) {
                if (punkty.size() % 2 == 1) { // jesli liczba punktow jest nieparzysta ostatni punkt leci do konca
                    if (j == punkty.size() - 1) {
                        for (int k = punkty.get(j); k < ilePunktow; k++) {
                            pokrzyzowane.get(indexy.get(i)).set(k, osobniki.get(indexy.get(i + 1)).get(k));
                            pokrzyzowane.get(indexy.get(i + 1)).set(k, osobniki.get(indexy.get(i)).get(k));
                        }
                        break;
                    }
                }
                for (int k = punkty.get(j); k < punkty.get(j + 1); k++) {

                    pokrzyzowane.get(indexy.get(i)).set(k, osobniki.get(indexy.get(i + 1)).get(k));
                    pokrzyzowane.get(indexy.get(i + 1)).set(k, osobniki.get(indexy.get(i)).get(k));
                }
            }
            punkty.clear();
        }
        return pokrzyzowane;
    }
}