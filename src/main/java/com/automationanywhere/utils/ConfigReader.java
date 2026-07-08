package com.automationanywhere.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class ConfigReader {
    private static Dotenv dotenv;

    static {
        dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();
    }

    public static String get(String key) {
        return dotenv.get(key);
    }
}
