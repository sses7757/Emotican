package com.emotican.util;

import android.support.annotation.NonNull;

public class CommonMethods {
    @NonNull
    public static String removeID(@NonNull String s) {
        int index = s.indexOf("_");
        return s.substring(0, index);
    }
}
