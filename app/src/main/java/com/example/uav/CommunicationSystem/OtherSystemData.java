package com.example.uav.CommunicationSystem;

public class OtherSystemData implements ByteParser {

    private TelemetryModel Data;

    public OtherSystemData() {
    }

    @Override
    public TelemetryModel getData(byte[] data) {
        if (data != null) {

            double droneLatitude = data[1];
            double droneLongitude = data[2];
            double droneAltitude = data[3];
            double homeLatitude = data[1];
            double homeLongitude = data[2];
            double homeAltitude = data[2];
            double droneVelocity=data[1];
            int gps = data[4];
            int mode = data[5];
            //this.Data = new TelemetryModel(droneLatitude, droneLongitude, droneAltitude, homeLatitude, homeLongitude,
            //        homeAltitude,droneVelocity, gps, mode);
        }

        return Data;
    }
}
