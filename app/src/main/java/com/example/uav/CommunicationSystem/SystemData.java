package com.example.uav.CommunicationSystem;

public class SystemData implements ByteParser {


    private TelemetryModel Data;

    public SystemData() {
    }

    enum DroneData {
        DRONE_LATITUDE(1, 2, 3, 4),
        DRONE_LONGITUDE(5, 6, 7, 8),
        DRONE_MSL(9, 10, 11, 12),
        DRONE_AGL(13, 14, 15, 16),
        HOME_LATITUDE(17, 18, 19, 20),
        HOME_LONGITUDE(21, 22, 23, 24),
        HOME_MSL(25, 26, 27, 28),
        DRONE_HORIZONTAL_VELOCITY(29, 30, 31, 32),
        DRONE_VERTICAL_VELOCITY(33, 34, 35, 36),
        DRONE_HEADING(37, 38, 39, 40),
        DRONE_GPS(41),
        DRONE_MODE(42),
        DRONE_ARM_STATUS(43),
        DRONE_BATTERY_VOLTAGE(44, 45, 46, 47),
        DRONE_BATTERY_PERCENT(48);

        private final int[] indexes;
        private byte[] bytes;

        DroneData(int... indexes) {
            this.indexes = indexes;
        }

        public void setData(byte[] data) {
            bytes = new byte[indexes.length];
            for (int i = 0; i < indexes.length; i++) {
                bytes[i] = data[indexes[i]];
            }
        }

        public byte[] getBytes() {
            return bytes;
        }
    }

    @Override
    public TelemetryModel getData(byte[] data) {
        if (data != null) {
            DroneData.DRONE_LATITUDE.setData(data);
            DroneData.DRONE_LONGITUDE.setData(data);
            DroneData.DRONE_MSL.setData(data);
            DroneData.DRONE_AGL.setData(data);

            DroneData.HOME_LATITUDE.setData(data);
            DroneData.HOME_LONGITUDE.setData(data);
            DroneData.HOME_MSL.setData(data);

            DroneData.DRONE_HORIZONTAL_VELOCITY.setData(data);
            DroneData.DRONE_VERTICAL_VELOCITY.setData(data);

            DroneData.DRONE_HEADING.setData(data);

            DroneData.DRONE_GPS.setData(data);
            DroneData.DRONE_MODE.setData(data);
            DroneData.DRONE_ARM_STATUS.setData(data);

            DroneData.DRONE_BATTERY_VOLTAGE.setData(data);
            DroneData.DRONE_BATTERY_PERCENT.setData(data);

            byte[] droneLatitudeBytes = DroneData.DRONE_LATITUDE.getBytes();
            byte[] droneLongitudeBytes = DroneData.DRONE_LONGITUDE.getBytes();
            byte[] droneMslBytes = DroneData.DRONE_MSL.getBytes();
            byte[] droneAglBytes = DroneData.DRONE_AGL.getBytes();

            byte[] homeLatitudeBytes = DroneData.HOME_LATITUDE.getBytes();
            byte[] homeLongitudeBytes = DroneData.HOME_LONGITUDE.getBytes();
            byte[] homeMslBytes = DroneData.HOME_MSL.getBytes();

            byte[] droneHorizontalVelocityBytes = DroneData.DRONE_HORIZONTAL_VELOCITY.getBytes();
            byte[] droneVerticalVelocityBytes = DroneData.DRONE_VERTICAL_VELOCITY.getBytes();
            byte[] droneHeadingBytes = DroneData.DRONE_HEADING.getBytes();


            byte[] gpsBytes = DroneData.DRONE_GPS.getBytes();
            byte[] modeBytes = DroneData.DRONE_MODE.getBytes();
            byte[] armStatusBytes = DroneData.DRONE_ARM_STATUS.getBytes();
            byte[] droneBatteryVoltageBytes = DroneData.DRONE_BATTERY_VOLTAGE.getBytes();
            byte[] droneBatteryPercentBytes = DroneData.DRONE_BATTERY_PERCENT.getBytes();

            double droneLatitude = ByteParser.byteArrayToDoubleLittleEndian(droneLatitudeBytes);
            double droneLongitude = ByteParser.byteArrayToDoubleLittleEndian(droneLongitudeBytes);
            double droneMSL = ByteParser.byteArrayToDoubleLittleEndian(droneMslBytes);
            double droneAGL = ByteParser.byteArrayToDoubleLittleEndian(droneAglBytes);

            double homeLatitude = ByteParser.byteArrayToDoubleLittleEndian(homeLatitudeBytes);
            double homeLongitude = ByteParser.byteArrayToDoubleLittleEndian(homeLongitudeBytes);
            double homeMSL = ByteParser.byteArrayToDoubleLittleEndian(homeMslBytes);

            double droneHorizontalVelocity = ByteParser.byteArrayToDoubleLittleEndian(droneHorizontalVelocityBytes);
            double droneVerticalVelocity = ByteParser.byteArrayToDoubleLittleEndian(droneVerticalVelocityBytes);
            double droneHeading = ByteParser.byteArrayToDoubleLittleEndian(droneHeadingBytes);

            int gps = ByteParser.byteArrayToInt(gpsBytes);
            int mode = ByteParser.byteArrayToInt(modeBytes);
            int armStatus = ByteParser.byteArrayToInt(armStatusBytes);
            double droneBatteryVoltage = ByteParser.byteArrayToDoubleLittleEndian(droneBatteryVoltageBytes);
            int droneBatteryPercent = ByteParser.byteArrayToInt(droneBatteryPercentBytes);

            this.Data = new TelemetryModel(droneLatitude, droneLongitude, droneMSL, droneAGL,
                    homeLatitude, homeLongitude, homeMSL,
                    droneHorizontalVelocity,
                    droneVerticalVelocity,
                    droneHeading,
                    gps, mode, armStatus, droneBatteryVoltage, droneBatteryPercent);
        }

        return Data;
    }
}
