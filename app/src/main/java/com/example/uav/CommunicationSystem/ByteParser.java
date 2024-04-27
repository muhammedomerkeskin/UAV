package com.example.uav.CommunicationSystem;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

interface ByteParser {
    static double byteArrayToDoubleLittleEndian(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getFloat();
    }
    public static int byteArrayToInt(byte[] byteArray) {
        int size=4;
        byte[] filledArray = new byte[size];
        Arrays.fill(filledArray, (byte) 0x00);
        System.arraycopy(byteArray, 0, filledArray, size - byteArray.length, byteArray.length);
        ByteBuffer buffer = ByteBuffer.wrap(filledArray);
        return buffer.getInt();
    }
    TelemetryModel getData(byte[] data);

}


