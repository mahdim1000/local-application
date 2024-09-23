package org.radargps.localapplication.common.util;

public class TimeUtil {
    public static boolean isTimestampDifferenceLessThan(Long timestamp1, Long timestamp2, Integer thresholdInSeconds) {
        if (timestamp1 == null || timestamp2 == null || thresholdInSeconds == null) {
            throw new IllegalArgumentException("Timestamps and threshold must not be null");
        }

        // Calculate the absolute difference between the two timestamps (in milliseconds)
        long differenceInSeconds = Math.abs(timestamp1 - timestamp2);

        // Check if the difference is less than the threshold in milliseconds
        return differenceInSeconds < thresholdInSeconds;
    }


}
