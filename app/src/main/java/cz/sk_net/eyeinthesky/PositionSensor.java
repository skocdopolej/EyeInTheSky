package cz.sk_net.eyeinthesky;

import java.io.Serializable;

public class PositionSensor implements Serializable{

    private float yawCorrection;
    private float rollCorrection;
    private float pitchCorrection;

    public PositionSensor(float yawCorrection, float rollCorrection, float pitchCorrection) {
        this.yawCorrection = yawCorrection;
        this.rollCorrection = rollCorrection;
        this.pitchCorrection = pitchCorrection;
    }

    public float getYawCorrection() {
        return yawCorrection;
    }

    public void setYawCorrection(float yawCorrection) {
        this.yawCorrection = yawCorrection;
    }

    public float getRollCorrection() {
        return rollCorrection;
    }

    public void setRollCorrection(float rollCorrection) {
        this.rollCorrection = rollCorrection;
    }

    public float getPitchCorrection() {
        return pitchCorrection;
    }

    public void setPitchCorrection(float pitchCorrection) {
        this.pitchCorrection = pitchCorrection;
    }

    public String print() {
        return (yawCorrection + ";" + rollCorrection + ";" + pitchCorrection + "\n");
    }
}
