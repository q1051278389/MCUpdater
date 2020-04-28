package cn.tohsaka.factory.MCUpdater;

import cn.tohsaka.factory.MCUpdater.awt.awtWindow;
import cn.tohsaka.factory.MCUpdater.utils.*;
import com.google.gson.Gson;
import org.jackhuang.hmcl.Launcher;
import org.jackhuang.hmcl.Main;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Launch{
    public static String[] args;
    public static void main(String[] a)
    {
        args=a;
        Env.startpath = System.getProperty("user.dir");

        new File(Env.startpath+"\\.minecraft\\sidemod").mkdirs();
        new File(Env.startpath+"\\.minecraft\\mods").mkdirs();
        if(!new File(Env.startpath+"\\.minecraft\\updater.json").exists() || a[0]=="hmcldirect"){
            Launcher.main(a);
            return;
        }
        Version version = new Gson().fromJson(FSKt.readUrl(Env.load().url),Version.class);
        Map<File,String> mods = makerLaunch.scandir(Env.startpath+"/.minecraft/mods",null);
        for(File f:mods.keySet()){
            if(!version.mods.keySet().contains(mods.get(f))){
                System.out.println(f.getAbsolutePath());
                deleteAllFilesOfDir(f);
            }
        }
        mods = makerLaunch.scandir(Env.startpath+"/.minecraft/mods",null);

        Map<String,String> needDownload=new HashMap<>();
        for(String md5file:version.mods.keySet()){
            if(!mods.values().contains(md5file)){
                needDownload.put(md5file,version.mods.get(md5file));
            }
        }
        needDownload.put("config","\\.minecraft\\tmp\\config.zip");
        needDownload.put("scripts","\\.minecraft\\tmp\\scripts.zip");
        needDownload.put("servers.dat","\\.minecraft\\servers.dat");

        awtWindow awt =new awtWindow();
        awt.startDownload(needDownload,version);
    }
    public static void onFinish(){
        try{
            new File(Env.startpath+"\\.minecraft\\scripts").mkdirs();
            new File(Env.startpath+"\\.minecraft\\config").mkdirs();
            new ZipUtils().unZip(Env.startpath+"\\.minecraft\\tmp\\scripts.zip",Env.startpath+"\\.minecraft\\scripts",true);
            new ZipUtils().unZip(Env.startpath+"\\.minecraft\\tmp\\config.zip",Env.startpath+"\\.minecraft\\config",true);

        }catch (Exception e){
            MB.error("解压配置文件错误,请重新打开启动器\n"+e.getLocalizedMessage());
            System.exit(-1);
        }
        for(File f :new File(Env.startpath+"\\.minecraft\\sidemod").listFiles()){
            FSKt.copyFile(f, new File(Env.startpath + "\\.minecraft\\mods\\" + f.getName()));
        }
        Launcher.main(args);
    }
    public static void deleteAllFilesOfDir(File path) {
        if (null != path) {
            if (!path.exists())
                return;
            if (path.isFile()) {
                boolean result = path.delete();
                int tryCount = 0;
                while (!result && tryCount++ < 10) {
                    System.gc(); // 回收资源
                    result = path.delete();
                }
            }
            File[] files = path.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    deleteAllFilesOfDir(files[i]);
                }
            }
            path.delete();
        }
    }
}