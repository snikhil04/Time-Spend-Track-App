package com.tracker.entities;

import java.util.UUID;

public class IdGenerator {

    public static String Id(){
        String ts = String.valueOf(System.currentTimeMillis());
        String uuid = (ts + UUID.randomUUID());
        return uuid;
    }

}
