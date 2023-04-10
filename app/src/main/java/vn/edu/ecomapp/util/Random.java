package vn.edu.ecomapp.util;

import android.annotation.SuppressLint;

import java.security.SecureRandom;

public class Random {
    @SuppressLint("DefaultLocale")
    public static String randomId(String prefix) {
        if(prefix == null) prefix = "";
        int upperbound = 1000000;
        SecureRandom rand = new SecureRandom();
        int numberRan = rand.nextInt(upperbound);
        return  prefix + String.format("%d", numberRan);
    }
}
