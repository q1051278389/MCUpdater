package cn.tohsaka.factory.MCUpdater.utils;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.digest.*;

public class MD5 {
    private static final String[] strHex= {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
    private static MessageDigest digest = null;

    static{
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public static String fromFile(File file){
        String hash = null;
        try {
            hash = DigestUtils.md5Hex(new FileInputStream(file));
        }catch (Exception e){
            return null;
        }
        return hash;
    }


    public static String getDist(String source){
        try {
            byte[] data = source.getBytes("UTF-8");
            return buffer2Hex(digest.digest(data));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String buffer2Hex(byte[] data){
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<data.length;i++){
            int d = data[i];
            if(d<0){
                d+=256;
            }
            int d1 = d / 16;
            int d2 = d % 16;
            sb.append(strHex[d1]+strHex[d2]);
        }
        return sb.toString();
    }
}
