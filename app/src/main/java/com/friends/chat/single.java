package com.friends.chat;

public class single {
    private static final single ourInstance = new single();

    public static single getInstance() {
        return ourInstance;
    }

    private single() {
    }
}
