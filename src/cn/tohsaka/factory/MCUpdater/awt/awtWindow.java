package cn.tohsaka.factory.MCUpdater.awt;

import cn.tohsaka.factory.MCUpdater.utils.Env;
import cn.tohsaka.factory.MCUpdater.utils.MB;
import cn.tohsaka.factory.MCUpdater.utils.OkHttpDownloader;
import cn.tohsaka.factory.MCUpdater.utils.Version;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Vector;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;

public class awtWindow extends JFrame{
    Vector< Vector<String> > vd = new Vector<Vector<String>>();
    JTable table = null;
    public awtWindow(){
        Vector<String> vh = new Vector<String>();
        vh.add("文件名");
        vh.add("大小");
        vh.add("进度");
        DefaultTableModel dtm = new DefaultTableModel(vd,vh);
        table = new JTable(dtm);
        table.getColumnModel().getColumn(0).setPreferredWidth(300);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        this.add(new JScrollPane(table));
        this.setTitle("游戏文件更新");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600,450);
        this.setVisible(true);
    }
    public void startDownload(Map<String,String> needDownload, Version v){

        for(String md5:needDownload.keySet()){
            Vector<String> row = new Vector<String>();
            String fn = needDownload.get(md5);
            row.add(fn.substring(fn.lastIndexOf("\\")+1));
            if(v.size.containsKey(md5)){
                row.add(getDataSize(v.size.get(md5)));
            }else{
                row.add("0B");
            }
            row.add("0%");
            vd.add(row);
            File f = new File(Env.startpath+needDownload.get(md5));
            f.mkdirs();
            f.delete();
            new OkHttpDownloader(v.url+md5, Env.startpath+needDownload.get(md5)).download(new OkHttpDownloader.Callback() {
                @Override
                public void onProgress(long progress) {
                    row.set(2,String.valueOf(progress)+"%");
                    table.updateUI();
                }

                @Override
                public void onFinish() {
                    row.set(2,"完成");
                    table.updateUI();
                }

                @Override
                public void onError(IOException ex) {
                    MB.error("更新错误,请重新打开启动器\n"+ex.getLocalizedMessage());
                }
            });

            table.updateUI();
        }
    }
    public static String getDataSize(long size) {
        DecimalFormat formater = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "bytes";
        } else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            return formater.format(kbsize) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            return formater.format(mbsize) + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            float gbsize = size / 1024f / 1024f / 1024f;
            return formater.format(gbsize) + "GB";
        } else {
            return "size: error";
        }
    }
}


