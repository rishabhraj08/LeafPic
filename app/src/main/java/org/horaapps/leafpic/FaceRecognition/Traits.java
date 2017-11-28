package org.horaapps.leafpic.FaceRecognition;

/**
 * Created by Sam on 11/28/2017.
 */

public class Traits {
    String gender, asian, maleConfidence, femaleConfidence, eyeDistance, age, glasses, path;

    public Traits(String gender, String asian, String eyeDistance, String maleConfidence, String femaleConfidence, String age, String glasses, String path) {
        this.gender = gender;
        this.asian = asian;
        this.eyeDistance = eyeDistance;
        this.maleConfidence = maleConfidence;
        this.femaleConfidence = femaleConfidence;
        this.age = age;
        this.glasses = glasses;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getGender() {
        return gender;

    }

    public String getAsian() {
        return asian;
    }

    public String getMaleConfidence() {
        return maleConfidence;
    }

    public String getFemaleConfidence() {
        return femaleConfidence;
    }

    public String getAge() {
        return age;
    }

    public String getGlasses() {
        return glasses;
    }

    public String getEyeDistance() {
        return eyeDistance;

    }
}
