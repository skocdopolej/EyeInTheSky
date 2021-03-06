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

    public float getFovX(float s) {

        return (chipSizeX * s) / focalLength;
    }

    public float getChipSizeX() {
        return chipSizeX;
    }

    public float getFocalLength() {
        return focalLength;
    }

    public float getFovY(float s) {

        return (chipSizeY * s) / focalLength;
    }

    public int getResolutionX() {
        return resolutionX;
    }

    public int getResolutionY() {
        return resolutionY;
    }

    public String getParams() {
        return (name + ";" + resolutionX + ";" + resolutionY + ";" + chipSizeX + ";" + chipSizeY + ";" + focalLength + ";" + shutterURL + "\n");
    }

    public void shrinkChipSizeX(float shrinkX) {
        chipSizeX = chipSizeX / (chipSizeX + shrinkX);
    }

    public void shrinkChipSizeY(float shrinkY) {
        chipSizeY = chipSizeY / (chipSizeY + shrinkY);
    }

    public void takePhoto() {

        new HttpClient().execute(shutterURL);
    }
}
