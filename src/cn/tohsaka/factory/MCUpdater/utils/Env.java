package cn.tohsaka.factory.MCUpdater.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Env {
    public static String startpath;
    public static Setting load(){
        Setting st = new Gson().fromJson(FSKt.readFile(startpath+"/.minecraft/updater.json"),Setting.class);
        return st;
    }
}
