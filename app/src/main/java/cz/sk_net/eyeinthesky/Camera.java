package cz.sk_net.eyeinthesky;

import java.io.Serializable;

public class Camera implements Serializable{

    private String name;
    private int resolutionX;
    private int resolutionY;
    private float chipSizeX;
    private float chipSizeY;
    private float focalLength;
    private String shutter;

    public Camera(String name, int resolutionX, int resolutionY, float chipSizeX, float chipSizeY, float focalLength, String shutter) {
        this.name = name;
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
        this.chipSizeX = chipSizeX;
        this.chipSizeY = chipSizeY;
        this.focalLength = focalLength;
        this.shutter = shutter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResolutionX() {
        return resolutionX;
    }

    public void setResolutionX(int resolutionX) {
        this.resolutionX = resolutionX;
    }

    public int getResolutionY() {
        return resolutionY;
    }

    public void setResolutionY(int resolutionY) {
        this.resolutionY = resolutionY;
    }

    public float getChipSizeX() {
        return chipSizeX;
    }

    public void setChipSizeX(float chipSizeX) {
        this.chipSizeX = chipSizeX;
    }

    public float getChipSizeY() {
        return chipSizeY;
    }

    public void setChipSizeY(float chipSizeY) {
        this.chipSizeY = chipSizeY;
    }

    public float getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(float focalLength) {
        this.focalLength = focalLength;
    }

    public String getShutter() {
        return shutter;
    }

    public void setShutter(String shutter) {
        this.shutter = shutter;
    }

    public String print() {
        return ("Camera name: " + name + "\nResolution: " + resolutionX + "×" + resolutionY + "\nChip size: " + chipSizeX + "×" + chipSizeY + "\nLens" + focalLength + " mm\nShutter URL: " + shutter);
    }
}
