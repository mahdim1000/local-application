//package org.radargps.localapplication.common;
//
//import org.radargps.localapplication.captured.data.domain.Data;
//
//import java.util.Scanner;
//import java.util.UUID;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class Cache {
//    // -----------------------
//    private static final ConcurrentHashMap<UUID, Data> DEVICE_ID_TO_LAST_DATA = new ConcurrentHashMap<>();
//    public static void deviceIdToLastDataPut(UUID deviceId, Data data) {
//        DEVICE_ID_TO_LAST_DATA.put(deviceId, data);
//    }
//    public static Data deviceIdToLastDataGet(UUID deviceId) {
//        return DEVICE_ID_TO_LAST_DATA.get(deviceId);
//    }
//    public static boolean deviceIdToLastDataContainsKey(UUID deviceId) {
//        return DEVICE_ID_TO_LAST_DATA.containsKey(deviceId);
//    }
//    public static void deviceIdToLastDataEvict(UUID deviceId) {
//        DEVICE_ID_TO_LAST_DATA.remove(deviceId);
//    }
//
//
//
//    // -----------------------
//    private static final ConcurrentHashMap<UUID, Scanner> DEVICE_ID_TO_DATA_CAPTURE_DEVICE = new ConcurrentHashMap<>();
//    public static void deviceIdToDataCaptureDevicePut(UUID deviceId, Scanner device) {
//        DEVICE_ID_TO_DATA_CAPTURE_DEVICE.put(deviceId, device);
//    }
//    public static Scanner deviceIdToDataCaptureDeviceGet(UUID deviceId) {
//        return DEVICE_ID_TO_DATA_CAPTURE_DEVICE.get(deviceId);
//    }
//    public static boolean deviceIdToDataCaptureDeviceContainsKey(UUID deviceId) {
//        return DEVICE_ID_TO_DATA_CAPTURE_DEVICE.containsKey(deviceId);
//    }
//    public static void deviceIdToDataCaptureDeviceEvict(UUID deviceId) {
//        DEVICE_ID_TO_DATA_CAPTURE_DEVICE.remove(deviceId);
//    }
//
//
//
//    // -----------------------
//    private static final ConcurrentHashMap<String, Scanner> UNIQUE_ID_TO_DATA_CAPTURE_DEVICE = new ConcurrentHashMap<>();
//    public static void uniqueIdToDataCaptureDevicePut(String uniqueId, Scanner device) {
//        UNIQUE_ID_TO_DATA_CAPTURE_DEVICE.put(uniqueId, device);
//    }
//    public static Scanner uniqueIdToDataCaptureDeviceGet(String uniqueId) {
//        return UNIQUE_ID_TO_DATA_CAPTURE_DEVICE.get(uniqueId);
//    }
//    public static boolean uniqueIdToDataCaptureDeviceContainsKey(String uniqueId) {
//        return UNIQUE_ID_TO_DATA_CAPTURE_DEVICE.containsKey(uniqueId);
//    }
//    public static void uniqueIdToDataCaptureDeviceEvict(String uniqueId) {
//        UNIQUE_ID_TO_DATA_CAPTURE_DEVICE.remove(uniqueId);
//    }
//
//
//    // -----------------------
//    public static void evictDevice(UUID deviceId) {
//        var device = DEVICE_ID_TO_DATA_CAPTURE_DEVICE.remove(deviceId);
//        if (device != null && device.getUniqueId() != null) {
//            UNIQUE_ID_TO_DATA_CAPTURE_DEVICE.remove(device.getUniqueId());
//        }
//    }
//
//}
