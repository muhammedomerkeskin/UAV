package com.example.uav.Util;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;
import static java.lang.Math.pow;

public class LocationCalculation {

    private List<Double> targetLocation = new ArrayList<>();
    private double bearing,horizontalDistance,targetAltitude,verticalDistance;
    private static final double radiusOfEarth=6371.137;
    // this method is used to convert degree to radian


    public List<Double> getTargetLocation(double currentLatitude, double currentLongitude,
                                          double currentHeading,
                                          double horizontalDistance2Target){
        targetLocation=TargetLocation(currentLatitude,currentLongitude,currentHeading,horizontalDistance2Target);
        return targetLocation;
    }
    public double getBearing(double currentLatitude, double currentLongitude,double targetLatitude, double targetLongitude){
        bearing=Bearing(currentLatitude,currentLongitude,targetLatitude, targetLongitude);
        return bearing;
    }
    public double getVerticalDistance(double currentMSL,double targetMSL){

        verticalDistance=VerticalDistance(currentMSL,targetMSL);
        return verticalDistance;
    }
    public double getHorizontalDistance(double currentLatitude, double currentLongitude,double targetLatitude, double targetLongitude){
        horizontalDistance=HorizontalDistance(currentLatitude,currentLongitude,targetLatitude, targetLongitude);
        return horizontalDistance;
    }
    public double getTargetAltitude(double currentAltitude,double diagonalDistance,double pitch){
        targetAltitude =TargetAltitude(currentAltitude, diagonalDistance, pitch);
        return targetAltitude;

    }
    private List<Double> TargetLocation(double currentLatitude, double currentLongitude,
                                        double currentHeading,
                                        double horizontalDistance2Target){

        double latitudeDrone =  Math.toRadians(currentLatitude);
        double longitudeDrone = Math.toRadians(currentLongitude);
        double headingDrone =Math.toRadians(currentHeading);
        double distanceDivisionRadius = horizontalDistance2Target / radiusOfEarth;

        double latitudeTarget = Math.asin(
                Math.sin(latitudeDrone) *  Math.cos(distanceDivisionRadius) +
                        Math.cos(latitudeDrone) *  Math.sin(distanceDivisionRadius) *  Math.cos(headingDrone)
        );

        double longitudeTarget = longitudeDrone +  Math.atan2(
                Math.sin(headingDrone) *  Math.sin(distanceDivisionRadius) *  Math.cos(latitudeDrone),
                Math.cos(distanceDivisionRadius) -  Math.sin(latitudeDrone) *  Math.sin(latitudeTarget)
        );

        targetLocation.add(Math.toDegrees(latitudeTarget));
        targetLocation.add(Math.toDegrees(longitudeTarget));
        return targetLocation;
    }
    private double Bearing(double currentLatitude, double currentLongitude,double targetLatitude, double targetLongitude){
        double latitudeDrone =  Math.toRadians(currentLatitude);
        double longitudeDrone = Math.toRadians(currentLongitude);
        double latitudeTarget =  Math.toRadians(targetLatitude);
        double longitudeTarget = Math.toRadians(targetLongitude);
        double differenceLongitude=longitudeTarget-longitudeDrone;
        double x=Math.sin(differenceLongitude)*Math.cos(latitudeTarget);
        double y = Math.cos(latitudeDrone) * Math.sin(latitudeTarget) -
                (Math.sin(latitudeDrone) * Math.cos(latitudeTarget) * Math.cos(differenceLongitude));
        bearing=(Math.toDegrees(Math.atan2(x,y))+ 360) % 360;

        return bearing;
    }

    private double HorizontalDistance(double currentLatitude, double currentLongitude,
                                                  double targetLatitude, double targetLongitude) {
        currentLatitude = Math.toRadians(currentLatitude);
        currentLongitude = Math.toRadians(currentLongitude);
        targetLatitude = Math.toRadians(targetLatitude);
        targetLongitude = Math.toRadians(targetLongitude);

        double differenceOfLongitude = targetLongitude - currentLongitude;
        double differenceOfLatitude = targetLatitude - currentLatitude;
        double formula = Math.pow(Math.sin(differenceOfLatitude / 2), 2) +
                Math.cos(currentLatitude) * Math.cos(targetLatitude) *
                        Math.pow(Math.sin(differenceOfLongitude / 2), 2);
        horizontalDistance = 2000 * Math.asin(Math.sqrt(formula)) * radiusOfEarth;
        return horizontalDistance;

    }
    private double VerticalDistance(double currentMSL,double targetMSL){
        verticalDistance=currentMSL-targetMSL;
        return verticalDistance;
    }
    private double TargetAltitude(double currentAltitude,double diagonalDistance,double pitch){
        targetAltitude=currentAltitude+(diagonalDistance*Math.sin(pitch));
        return targetAltitude;
    }


}
