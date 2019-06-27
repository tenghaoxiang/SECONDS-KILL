package top.haibaraai.secondsKill.util;

import java.util.UUID;

public class UUIDUtil {

    public static String generate() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

}
