package cn.tohsaka.factory.MCUpdater;

import cn.tohsaka.factory.MCUpdater.utils.*;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class makerLaunch {
    public static void main(String[] args) throws IOException {
        Env.startpath = System.getProperty("user.dir");

        System.out.println(Env.load().url);
        System.out.println(System.getProperty("user.dir"));

        //start scan
        Map<File,String> sidemod = scandir(Env.startpath+"/.minecraft/sidemod",null);
        Map<File,String> mods = scandir(Env.startpath+"/.minecraft/mods",sidemod);
        Map<File,String> config = scandir(Env.startpath+"/.minecraft/config",null);
        Map<File,String> scripts = scandir(Env.startpath+"/.minecraft/scripts",null);

        //create digist
        File packs = new File(System.getProperty("user.dir")+"/HMCLA/packs/");
        packs.mkdirs();
        for(File f:mods.keySet()){
            FSKt.copyFile(f,new File(packs.getAbsolutePath()+"/"+mods.get(f)));
        }
        //compress config
        ZipUtils zip = new ZipUtils();
        try {
            zip.zip(System.getProperty("user.dir")+"/HMCLA/packs/config.zip",Env.startpath+"/.minecraft/config",false);
        }catch (Exception e){
            MB.error(e.getMessage());
        }
        try {
            zip.zip(System.getProperty("user.dir")+"/HMCLA/packs/scripts.zip",Env.startpath+"/.minecraft/scripts",false);
        }catch (Exception e){
            MB.error(e.getMessage());
        }
        //copy server.dat
        FSKt.copyFile(new File(Env.startpath+"/.minecraft/servers.dat"),new File(packs.getAbsolutePath()+"/servers.dat"));


        //generate version json

        Version v = new Version();
        for(File f:mods.keySet()){
            v.mods.put(mods.get(f),f.getCanonicalPath().replace(System.getProperty("user.dir"),""));
            v.size.put(mods.get(f),f.length());
        }
        v.url=Env.load().downloadUrl;
        FSKt.writeFile(System.getProperty("user.dir")+"/HMCLA/version.json",new Gson().toJson(v));
    }
    public static Map<File,String> scandir(String path,Map<File,String> ignore){
        HashMap<File,String> hashlist = new HashMap<>();
        if(!new File(path).exists()){
            return  hashlist;
        }
        File[] files = new File(path).listFiles();
        for (File f:files) {
            if(f.isDirectory()){
                hashlist.putAll(scandir(f.getAbsolutePath(),ignore));
            }else{
                String md5 = MD5.fromFile(f);
                if(ignore!=null && ignore.values().contains(md5)) {
                    continue;
                }
                hashlist.put(f,md5);
            }
        }
        return hashlist;
    }
}
