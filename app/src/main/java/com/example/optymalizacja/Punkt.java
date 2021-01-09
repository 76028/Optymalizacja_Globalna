package com.example.optymalizacja;

public class Punkt {

    int x, y;

    Punkt(int x, int y) {

        this.x = x;
        this.y = y;
    }

    double obliczOdlegloscDoPunktu(Punkt b) {
        return Math.sqrt(Math.pow(this.x - b.x, 2) + Math.pow(this.y - b.y, 2));
    }

    public String sprawdzOdleglosc(Punkt b) {
        return "(" + x + "," + y + ")->" + "(" + b.x + "," + b.y + ")" + " Odleglosc=" + obliczOdlegloscDoPunktu(b);
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
