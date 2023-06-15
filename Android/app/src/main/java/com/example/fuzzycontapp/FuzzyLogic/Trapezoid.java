package com.example.fuzzycontapp.FuzzyLogic;

public class Trapezoid {
    double n, a, b, c, d, h;

    public Trapezoid(double[] params) {
        if (params.length == 5) {
            this.a = params[0];
            this.b = params[1];
            this.c = params[2];
            this.d = params[3];
            this.h = params[4];
        }
        if (params.length == 6) {
            this.a = params[0] - params[1];
            this.b = params[0] - params[2];
            this.c = params[0] + params[3];
            this.d = params[0] + params[4];
            this.h = 1;
        }

    }

    private double mu(double x) {
        double y = 0;
        if ((x >= this.a) && (x <= this.b)) {
            y = 1 - ((this.b - x) / (this.b - this.a));
        } else if ((x >= this.b) && (x <= this.c)) {
            y = 1;
        } else if ((x >= this.c) && (x <= this.d)) {
            y = 1 - ((x - this.c) / (this.d - this.c));
        }
        return y;
    }

    public double muh(double x) {
        return Math.min(this.h, this.mu(x));
    }
}

