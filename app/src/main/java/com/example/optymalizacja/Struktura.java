package com.example.optymalizacja;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Struktura {

    private int ileOsobnikow;
    private int ilePunktow;
    private Random r;
    ArrayList<Punkt> punkty;
    List<List<Integer>> osobniki;
    List<List<Punkt>> trasa;
    double dlugosciTras[];

    public Struktura(int ileOsobikow, int ilePunktow) {

        this.ileOsobnikow = ileOsobikow;
        this.ilePunktow = ilePunktow; // Ilość punktów przez które przechodzi = Ilość genów osobnika
        r = new Random();
        generujPunkty();
        generujOsobniki();
        generujTrasy();
        dlugosciTras();
    }

    // Wygenerowanie wektora punktów po których będzie odwiedział komiwojażer
    private void generujPunkty() {

        punkty = new ArrayList<Punkt>();

        for (int i = 0; i < ilePunktow; i++)
            punkty.add(new Punkt(r.nextInt(21) - 10, r.nextInt(21) - 10));
    }

    //  Wygenerowanie listy osobników
    private void generujOsobniki() {

        osobniki = new ArrayList<>();

        for (int i = 0; i < ileOsobnikow; i++) {
            osobniki.add(new ArrayList<>());
            for (int j = 0; j < ilePunktow; j++) {
                osobniki.get(i).add(r.nextInt(ilePunktow - j));
            }
        }
    }

    ArrayList<Punkt> generujTrase(List<Integer> osobnikpermutacja) {
        ArrayList<Punkt> tempPunkty = (ArrayList<Punkt>) punkty.clone();
        ArrayList<Punkt> wynik = new ArrayList<>();
        int iter;
        for (int i = 0; i < osobnikpermutacja.size(); i++) {
            iter = osobnikpermutacja.get(i);
            wynik.add(tempPunkty.get(iter));
            tempPunkty.remove(iter);
        }

        return wynik;
    }

    void generujTrasy() {

        trasa = new ArrayList<>();
        ArrayList<Punkt> tempPunkty;

        int iter;
        for (int i = 0; i < ileOsobnikow; i++) {
            trasa.add(new ArrayList<>());
            tempPunkty = (ArrayList<Punkt>) punkty.clone();
            for (int j = 0; j < osobniki.get(0).size(); j++) {
                iter = osobniki.get(i).get(j);
                trasa.get(i).add(tempPunkty.get(iter));
                tempPunkty.remove(iter);
            }
        }
    }

    void generujTrasy(int ileOsobnikow) {

        trasa = new ArrayList<>();
        ArrayList<Punkt> tempPunkty;

        int iter;
        for (int i = 0; i < ileOsobnikow; i++) {
            trasa.add(new ArrayList<>());
            tempPunkty = (ArrayList<Punkt>) punkty.clone();
            for (int j = 0; j < osobniki.get(0).size(); j++) {
                iter = osobniki.get(i).get(j);
                trasa.get(i).add(tempPunkty.get(iter));
                tempPunkty.remove(iter);
            }
        }
    }


    void dlugosciTras() {

        dlugosciTras = new double[ileOsobnikow];
        double suma = 0;
        for (int i = 0; i < ileOsobnikow; i++) {
            dlugosciTras[i] = dlugoscTrasy(trasa.get(i));
        }
    }

    void dlugosciTras(int ileOsobnikow) {

        dlugosciTras = new double[ileOsobnikow];
        double suma = 0;
        for (int i = 0; i < ileOsobnikow; i++) {
            dlugosciTras[i] = dlugoscTrasy(trasa.get(i));
        }
    }

    double dlugoscTrasy(List<Punkt> lp) {

        double wynik = 0;
        for (int i = 0; i < ilePunktow - 1; i++) {
            wynik += lp.get(i).obliczOdlegloscDoPunktu(lp.get(i + 1));
        }
        return wynik;
    }

    void wyswietlPunkty() {

        for (Punkt p : punkty) {
            System.out.print(p + " ");
        }
    }

    void wyswietlOsobniki() {

        for (List<Integer> l : osobniki) {
            System.out.println(l);
        }
    }

    void wyswietlTrasy() {

        for (List<Punkt> l : trasa) {
            System.out.println(l);
        }
    }

    void wyswietlDlugosciTras() {

        for (Double d : dlugosciTras) {
            System.out.println(d);
        }
    }
}
