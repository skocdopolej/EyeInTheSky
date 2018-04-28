package cz.sk_net.eyeinthesky;

import java.io.Serializable;

public class Camera implements Serializable {

    private String name;
    private int resolutionX;
    private int resolutionY;
    private float chipSizeX;
    private float chipSizeY;
    private float focalLength;
    private String shutterURL;

    Camera(String name, int resolutionX, int resolutionY, float chipSizeX, float chipSizeY, float focalLength, String shutterURL) {
        this.name = name;
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
        this.chipSizeX = chipSizeX;
        this.chipSizeY = chipSizeY;
        this.focalLength = focalLength;
        this.shutterURL = shutterURL;
    }

    public float getFov(float a) {

        return (float) Math.toDegrees((2 * Math.atan(a / 2 * focalLength)));
    }

    public String print() {
        return (name + ";" + resolutionX + ";" + resolutionY + ";" + chipSizeX + ";" + chipSizeY + ";" + focalLength + ";" + shutterURL + "\n");
    }

    public void takePhoto() {

        new HttpClient().execute(shutterURL);
    }
}
