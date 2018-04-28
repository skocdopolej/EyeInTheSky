package cz.sk_net.eyeinthesky;

import java.io.Serializable;

public class Area implements Serializable {

    private static final double eartRad = 6378137.0;

    private double latA;
    private double latB;
    private double latC;
    private double latAA;
    private double latBB;
    private double lngA;
    private double lngB;
    private double lngC;
    private double lngAA;
    private double lngBB;

    Area(double latA, double latB, double latC, double lngA, double lngB, double lngC) {
        this.latA = latA;
        this.latB = latB;
        this.latC = latC;
        this.lngA = lngA;
        this.lngB = lngB;
        this.lngC = lngC;
    }

    public double getLatA() {
        return latA;
    }

    public double getLatB() {
        return latB;
    }

    public double getLatC() {
        return latC;
    }

    public double getLatAA() {
        return latAA;
    }

    public double getLatBB() {
        return latBB;
    }

    public double getLngA() {
        return lngA;
    }

    public double getLngB() {
        return lngB;
    }

    public double getLngC() {
        return lngC;
    }

    public double getLngAA() {
        return lngAA;
    }

    public double getLngBB() {
        return lngBB;
    }

    //https://wiki.openstreetmap.org/wiki/Mercator#Java
    private static double y2lat(double aY) {
        return Math.toDegrees(Math.atan(Math.exp(aY / eartRad)) * 2 - Math.PI / 2);
    }

    //https://wiki.openstreetmap.org/wiki/Mercator#Java
    private static double x2lon(double aX) {
        return Math.toDegrees(aX / eartRad);
    }

    //https://wiki.openstreetmap.org/wiki/Mercator#Java
    private static double lat2y(double aLat) {
        return Math.log(Math.tan(Math.PI / 4 + Math.toRadians(aLat) / 2)) * eartRad;
    }

    //https://wiki.openstreetmap.org/wiki/Mercator#Java
    private static double lon2x(double aLong) {
        return Math.toRadians(aLong) * eartRad;
    }

    public void computeArea() {

        //enMercate -> (lat, lng) to (y, x)
        double xa = lon2x(lngA);
        double xb = lon2x(lngB);
        double xc = lon2x(lngC);
        double ya = lat2y(latA);
        double yb = lat2y(latB);
        double yc = lat2y(latC);

        double u = (xb - xa) / (yb - ya);
        double v = (yb - ya) / (xa - xb);

        double yaa = (xc - xa - u * yc + v * ya) / (v - u);
        double ybb = (xc - xb - u * yc + v * yb) / (v - u);

        double xaa = xa + v * yaa - v * ya;
        double xbb = xb + v * ybb - v * yb;

        //deMercate -> (y, x) to (lat, lng)
        lngAA = x2lon(xaa);
        lngBB = x2lon(xbb);
        latAA = y2lat(yaa);
        latBB = y2lat(ybb);
    }
}
