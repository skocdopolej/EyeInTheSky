package cz.sk_net.eyeinthesky;

import java.io.Serializable;

public class Area implements Serializable {

    private double xa;
    private double xb;
    private double xc;
    private double xaa;
    private double xbb;
    private double ya;
    private double yb;
    private double yc;
    private double yaa;
    private double ybb;

    public Area(double xa, double xb, double xc, double ya, double yb, double yc) {
        this.xa = xa;
        this.xb = xb;
        this.xc = xc;
        this.ya = ya;
        this.yb = yb;
        this.yc = yc;
    }

    public double getXa() {
        return xa;
    }

    public double getXb() {
        return xb;
    }

    public double getXc() {
        return xc;
    }

    public double getXaa() {
        return xaa;
    }

    public double getXbb() {
        return xbb;
    }

    public double getYa() {
        return ya;
    }

    public double getYb() {
        return yb;
    }

    public double getYc() {
        return yc;
    }

    public double getYaa() {
        return yaa;
    }

    public double getYbb() {
        return ybb;
    }

    public void setXa(double xa) {
        this.xa = xa;
    }

    public void setXb(double xb) {
        this.xb = xb;
    }

    public void setXc(double xc) {
        this.xc = xc;
    }

    public void setYa(double ya) {
        this.ya = ya;
    }

    public void setYb(double yb) {
        this.yb = yb;
    }

    public void setYc(double yc) {
        this.yc = yc;
    }

    public void computeArea() {

        double u1 = xb - xa;
        double u2 = yb - ya;
        double v1 = u2;
        double v2 = - u1;

        yaa = (xc - xa - (u1 / u2) * yc + (v1 / v2) * ya) / ((v1 / v2) - (u1 / u2));
        ybb = (xc - xb - (u1 / u2) * yc + (v1 / v2) * yb) / ((v1 / v2) - (u1 / u2));

        xaa = (v1 / v2) * yaa + xc - (v1 / v2) * yc;
        xbb = (v1 / v2) * ybb + xc - (v1 / v2) * yc;
    }
}
