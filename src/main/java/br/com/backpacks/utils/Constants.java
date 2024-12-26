package br.com.backpacks.utils;

import java.util.List;

public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean DEBUG_MODE = false;
    public static boolean MONSTER_DROPS_BACKPACK = true;
    public static boolean CATCH_BACKPACK = true;
    public static String VERSION = "";
    public static final List<String> SUPPORTED_VERSIONS = List.of("1.21.3");
}
