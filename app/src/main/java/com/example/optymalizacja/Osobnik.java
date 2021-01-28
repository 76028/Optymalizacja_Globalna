package com.example.optymalizacja;

import java.util.List;

public class Osobnik implements Comparable<Osobnik> {
    double dlugosc;
    List<Integer> geny;

    Osobnik(double dlugosc, List<Integer> geny){
        this.dlugosc=dlugosc;
        this.geny=geny;
    }

    public String toString(){
        return geny+" "+dlugosc;
    }

    @Override
    public int compareTo(Osobnik o) {
        if(dlugosc > o.dlugosc) return 1;
        if(dlugosc < o.dlugosc) return -1;
        return 0;
    }
}

