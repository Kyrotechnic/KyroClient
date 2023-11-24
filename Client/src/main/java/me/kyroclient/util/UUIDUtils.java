package me.kyroclient.util;

import java.util.UUID;

public class UUIDUtils {
    public static String getFixedUuid(String uuid)
    {
        return java.util.UUID.fromString(
                uuid
                        .replaceFirst(
                                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                        )
        ).toString();
    }

    public static UUID getUuidWithDashes(String uuid)
    {
        return java.util.UUID.fromString(
                uuid
                        .replaceFirst(
                                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                        )
        );
    }
}
