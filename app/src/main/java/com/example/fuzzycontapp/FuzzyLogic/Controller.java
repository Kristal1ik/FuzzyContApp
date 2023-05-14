package com.example.fuzzycontapp.FuzzyLogic;

import java.util.Arrays;
import java.util.Collections;

public class Controller {
    Trapezoid x, v;
    double y1, y2, y3, y4, y5;

    public Controller(double x, double v) {
        this.x = new Trapezoid(new double[]{x, 0.05, 0.008, 0.008, 0.019, 0.0});
        this.v = new Trapezoid(new double[]{v, 0.071, 0.0575, 0.0575, 0.15, 0.0});

        this.y1 = Collections.min(Arrays.asList(this.intersection(this.x.a, this.x.b, this.x.c, this.x.d,
                        Rules.x1.a, Rules.x1.b, Rules.x1.c, Rules.x1.d),
                this.intersection(this.v.a, this.v.b, this.v.c, this.v.d,
                        Rules.v1.a, Rules.v1.b, Rules.v1.c, Rules.v1.d)));
        this.y2 = Collections.min(Arrays.asList(this.intersection(this.x.a, this.x.b, this.x.c, this.x.d,
                        Rules.x2.a, Rules.x2.b, Rules.x2.c, Rules.x2.d),
                this.intersection(this.v.a, this.v.b, this.v.c, this.v.d,
                        Rules.v2.a, Rules.v2.b, Rules.v2.c, Rules.v2.d)));
        this.y3 = Collections.min(Arrays.asList(this.intersection(this.x.a, this.x.b, this.x.c, this.x.d,
                        Rules.x3.a, Rules.x3.b, Rules.x3.c, Rules.x3.d),
                this.intersection(this.v.a, this.v.b, this.v.c, this.v.d,
                        Rules.v3.a, Rules.v3.b, Rules.v3.c, Rules.v3.d)));
        this.y4 = Collections.min(Arrays.asList(this.intersection(this.x.a, this.x.b, this.x.c, this.x.d,
                        Rules.x4.a, Rules.x4.b, Rules.x4.c, Rules.x4.d),
                this.intersection(this.v.a, this.v.b, this.v.c, this.v.d,
                        Rules.v4.a, Rules.v4.b, Rules.v4.c, Rules.v4.d)));
        this.y5 = Collections.min(Arrays.asList(this.intersection(this.x.a, this.x.b, this.x.c, this.x.d,
                        Rules.x5.a, Rules.x5.b, Rules.x5.c, Rules.x5.d),
                this.intersection(this.v.a, this.v.b, this.v.c, this.v.d,
                        Rules.v5.a, Rules.v5.b, Rules.v5.c, Rules.v5.d)));
    }

    private boolean cross_bool(double b1, double c1, double b2, double c2) {
        if (b1 > c1) {
            b1 = b1 + c1;
            c1 = b1 - c1;
            b1 = b1 - c1;
        }
        if (b2 > c2) {
            b2 = b2 + c2;
            c2 = b2 - c2;
            b2 = b2 - c2;
        }
        if (b1 > c2) {
            b1 = b1 + b2;
            b2 = b1 - b2;
            b1 = b1 - b2;

            c1 = c1 + c2;
            c2 = c1 - c2;
            c1 = c1 - c2;
        }
        if (c1 < b2) return false;
        return true;
    }

    private double intersection(double a1, double b1, double c1, double d1, double a2, double b2, double c2, double d2) {
        boolean f = this.cross_bool(b1, c1, b2, c2);
        if (f) {
            return 1;
        }
        if (b1 > c2) {
            a1 = a1 + a2;
            a2 = a1 - a2;
            a1 = a1 - a2;

            b1 = b1 + b2;
            b2 = b1 - b2;
            b1 = b1 - b2;

            c1 = c1 + c2;
            c2 = c1 - c2;
            c1 = c1 - c2;

            d1 = d1 + d2;
            d2 = d1 - d2;
            d1 = d1 - d2;
        }

        f = this.cross_bool(c1, d1, a2, b2);
        if (!f) {
            return 0;
        }
        double x = (a2 * c1 - d1 * b2) / (a2 - b2 + c1 - d1);
        return (d1 - x) / (d1 - c1);
    }

    private double find_min_point(double[] p_xv) {
        if (p_xv[0] > p_xv[1]) return p_xv[1];
        return p_xv[0];
    }

    public double[] return_() {
        return new double[] {this.y1, this.y2, this.y3, this.y4, this.y5};
    }
}

