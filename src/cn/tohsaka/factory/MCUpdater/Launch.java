package cn.tohsaka.factory.MCUpdater;

import cn.tohsaka.factory.MCUpdater.awt.awtWindow;
import cn.tohsaka.factory.MCUpdater.utils.Env;
import cn.tohsaka.factory.MCUpdater.utils.FSKt;
import cn.tohsaka.factory.MCUpdater.utils.MB;
import cn.tohsaka.factory.MCUpdater.utils.Version;
import com.google.gson.Gson;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Launch{
    public static void main(String[] args)
    {
        Env.startpath = System.getProperty("user.dir");

        Version version = new Gson().fromJson(FSKt.readUrl(Env.load().url),Version.class);
        Map<File,String> mods = makerLaunch.scandir(Env.startpath+"/.minecraft/mods",null);
        for(File f:mods.keySet()){
            if(!version.mods.keySet().contains(mods.get(f))){
                f.delete();
            }
        }
        mods = makerLaunch.scandir(Env.startpath+"/.minecraft/mods",null);

        Map<String,String> needDownload=new HashMap<>();
        for(String md5file:version.mods.keySet()){
            if(!mods.values().contains(md5file)){
                needDownload.put(md5file,version.mods.get(md5file));
            }
        }
        needDownload.put("config.zip","\\.minecraft\\tmp\\config.zip");
        needDownload.put("scripts.zip","\\.minecraft\\tmp\\scripts.zip");
        needDownload.put("servers.dat","\\.minecraft\\servers.dat");

        awtWindow awt =new awtWindow();
        awt.startDownload(needDownload,version);
    }
    public static void onFinish(){

    }
}