package com.emotican.API;

import android.graphics.Bitmap;

import java.util.Map;

public class LocalAPI {
    /**
     * Called the very first time to start a new thread to acquire data from server
     */
    public static void startQueryData() {

    }

    /**
     * String is in format of "Name_ID"
     * @return  All the maps are ArrayMap
     */
    public static Map<String, Bitmap> getTemples() {

        return null;
    }

    public static Map<String, Bitmap> getNetworkRes() {

        return null;
    }

    public static Map<String, Bitmap> getLocalExist() {

        return null;
    }

    /**
     * 获取所有模板、元素
     * @return  Object[4]{String[] main_label, String[] nonmain_labels, boolean[] isHot, Bitmap[] images}
     */
    public static Object[] getAllMoulds() {

        return null;
    }
}
