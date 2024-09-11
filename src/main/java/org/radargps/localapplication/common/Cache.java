package org.radargps.localapplication.common;

import org.radargps.localapplication.data_receiver.domain.Data;
import org.radargps.localapplication.data_receiver.domain.DataCaptureDevice;

import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    //
    private static final ConcurrentHashMap<UUID, Data> DEVICE_ID_TO_LAST_DATA = new ConcurrentHashMap<>();
    public static void putDeviceIdToLastData(UUID deviceId, Data data) {
        DEVICE_ID_TO_LAST_DATA.put(deviceId, data);
    }
    public static Data getDeviceIdToLastData(UUID deviceId) {
        return DEVICE_ID_TO_LAST_DATA.get(deviceId);
    }
    public static boolean containsKeyDeviceIdToLastData(UUID deviceId) {
        return DEVICE_ID_TO_LAST_DATA.containsKey(deviceId);
    }

    //
    private static final ConcurrentHashMap<UUID, DataCaptureDevice> DEVICE_ID_TO_DATA_CAPTURE_DEVICE = new ConcurrentHashMap<>();
    public static void putDeviceIdToDataCaptureDevice(UUID deviceId, DataCaptureDevice device) {
        DEVICE_ID_TO_DATA_CAPTURE_DEVICE.put(deviceId, device);
    }
    public static DataCaptureDevice getDeviceIdToDataCaptureDevice(UUID deviceId) {
        return DEVICE_ID_TO_DATA_CAPTURE_DEVICE.get(deviceId);
    }
    public static boolean containsKeyDeviceIdToDataCaptureDevice(UUID deviceId) {
        return DEVICE_ID_TO_DATA_CAPTURE_DEVICE.containsKey(deviceId);
    }

    //
    private static final ConcurrentHashMap<String, DataCaptureDevice> UNIQUE_ID_TO_DATA_CAPTURE_DEVICE = new ConcurrentHashMap<>();
    public static void putUniqueIdToDataCaptureDevice(String uniqueId, DataCaptureDevice device) {
        UNIQUE_ID_TO_DATA_CAPTURE_DEVICE.put(uniqueId, device);
    }
    public static DataCaptureDevice getUniqueIdToDataCaptureDevice(String uniqueId) {
        return UNIQUE_ID_TO_DATA_CAPTURE_DEVICE.get(uniqueId);
    }
    public static boolean containsKeyUniqueIdToDataCaptureDevice(String uniqueId) {
        return UNIQUE_ID_TO_DATA_CAPTURE_DEVICE.containsKey(uniqueId);
    }

}
