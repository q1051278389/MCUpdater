package cn.tohsaka.factory.MCUpdater;

import cn.tohsaka.factory.MCUpdater.utils.Env;
import cn.tohsaka.factory.MCUpdater.utils.Setting;
import cn.tohsaka.factory.MCUpdater.utils.ZipUtils;

import java.util.List;

public class makerLaunch {
    public static void main(String[] args) {
        Env.startpath = System.getProperty("user.dir");

        System.out.println(Env.load().url);
    }
    public static List<String> scandir(String path){

    }
}
