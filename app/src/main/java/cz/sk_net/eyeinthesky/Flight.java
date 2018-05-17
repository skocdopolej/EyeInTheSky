package cz.sk_net.eyeinthesky;

import java.io.Serializable;

class Flight implements Serializable {

    private float angle;
    private float distance;
    private int vector;

    Flight(float angle, float distance, int vector) {
        this.angle = angle;
        this.distance = distance;
        this.vector = vector;
    }

    public float getAngle() {
        return angle;
    }

    public float getDistance() {
        return distance;
    }

    public int getVector() {
        return vector;
    }
}
