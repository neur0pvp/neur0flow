package dev.neur0pvp.neur0flow.util;

import org.jetbrains.annotations.Nullable;

public class NumberConversions {
    public static long toLong(@Nullable Object object, long defaultValue) {
        if (object instanceof Number) {
            return ((Number) object).longValue();
        } else {
            try {
                if (object != null) {
                    return Long.parseLong(object.toString());
                }
            } catch (NumberFormatException | NullPointerException ignored) {
            }

            return defaultValue;
        }
    }

    public static int toInt(@Nullable Object object, int def) {
        if (object instanceof Number) {
            return ((Number) object).intValue();
        } else {
            try {
                if (object != null) {
                    return Integer.parseInt(object.toString());
                }
            } catch (NumberFormatException | NullPointerException ignored) {
            }

            return def;
        }
    }
}
