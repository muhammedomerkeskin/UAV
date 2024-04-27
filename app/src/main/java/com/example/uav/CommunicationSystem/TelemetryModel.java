package com.example.uav.CommunicationSystem;

public class TelemetryModel {
    private double droneLatitude, droneLongitude, droneMSL, droneAGL,
            homeLatitude, homeLongitude, homeMSL,
            droneHorizontalVelocity, droneVerticalVelocity,
            droneHeading,droneBatteryVoltage;
    private int droneGPS, droneMode, droneArmStatus,droneBatteryPercent;
    public double getDroneLatitude() {
        return droneLatitude;
    }

    public void setDroneLatitude(double droneLatitude) {
        this.droneLatitude = droneLatitude;
    }

    public double getDroneLongitude() {
        return droneLongitude;
    }

    public void setDroneLongitude(double droneLongitude) {
        this.droneLongitude = droneLongitude;
    }

    public double getDroneMSL() {
        return droneMSL;
    }

    public void setDroneMSL(double droneMSL) {
        this.droneMSL = droneMSL;
    }

    public double getDroneAGL() {
        return droneAGL;
    }

    public void setDroneAGL(double droneAGL) {
        this.droneAGL = droneAGL;
    }

    public double getHomeLatitude() {
        return homeLatitude;
    }

    public void setHomeLatitude(double homeLatitude) {
        this.homeLatitude = homeLatitude;
    }

    public double getHomeLongitude() {
        return homeLongitude;
    }

    public void setHomeLongitude(double homeLongitude) {
        this.homeLongitude = homeLongitude;
    }

    public double getHomeMSL() {
        return homeMSL;
    }

    public void setHomeMSL(double homeMSL) {
        this.homeMSL = homeMSL;
    }

    public double getDroneHorizontalVelocity() {
        return droneHorizontalVelocity;
    }

    public void setDroneHorizontalVelocity(double droneHorizontalVelocity) {
        this.droneHorizontalVelocity = droneHorizontalVelocity;
    }

    public double getDroneVerticalVelocity() {
        return droneVerticalVelocity;
    }

    public double getDroneBatteryVoltage() {
        return droneBatteryVoltage;
    }

    public void setDroneBatteryVoltage(double droneBatteryVoltage) {
        this.droneBatteryVoltage = droneBatteryVoltage;
    }

    public int getDroneBatteryPercent() {
        return droneBatteryPercent;
    }

    public void setDroneBatteryPercent(int droneBatteryPercent) {
        this.droneBatteryPercent = droneBatteryPercent;
    }

    public void setDroneVerticalVelocity(double droneVerticalVelocity) {
        this.droneVerticalVelocity = droneVerticalVelocity;
    }

    public double getDroneHeading() {
        return droneHeading;
    }

    public void setDroneHeading(double droneHeading) {
        this.droneHeading = droneHeading;
    }

    public int getDroneGPS() {
        return droneGPS;
    }

    public void setDroneGPS(int droneGPS) {
        this.droneGPS = droneGPS;
    }

    public int getDroneMode() {
        return droneMode;
    }

    public void setDroneMode(int droneMode) {
        this.droneMode = droneMode;
    }

    public int getDroneArmStatus() {
        return droneArmStatus;
    }

    public void setDroneArmStatus(int droneArmStatus) {
        this.droneArmStatus = droneArmStatus;
    }

    public TelemetryModel(double droneLatitude, double droneLongitude, double droneMSL, double droneAGL,
                          double homeLatitude, double homeLongitude, double homeMSL,
                          double droneHorizontalVelocity, double droneVerticalVelocity,
                          double droneHeading, int droneGPS, int droneMode, int droneArmStatus,
                          double droneBatteryVoltage,int droneBatteryPercent) {
        this.droneLatitude = droneLatitude;
        this.droneLongitude = droneLongitude;
        this.droneMSL = droneMSL;
        this.droneAGL = droneAGL;
        this.homeLatitude = homeLatitude;
        this.homeLongitude = homeLongitude;
        this.homeMSL = homeMSL;
        this.droneHorizontalVelocity = droneHorizontalVelocity;
        this.droneVerticalVelocity = droneVerticalVelocity;
        this.droneHeading = droneHeading;
        this.droneGPS = droneGPS;
        this.droneMode = droneMode;
        this.droneArmStatus = droneArmStatus;
        this.droneBatteryVoltage=droneBatteryVoltage;
        this.droneBatteryPercent=droneBatteryPercent;
    }




}
