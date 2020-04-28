package cn.tohsaka.factory.MCUpdater.utils

import java.io.File
import java.net.HttpURLConnection
import java.net.URL

fun readFile(file: String):String{
    return File(file).readText(Charsets.UTF_8);
}
fun writeFile(file: String,text:String){
    File(file).writeText(text,Charsets.UTF_8);
}
fun copyFile(input:File,target:File){
    input.copyTo(target,true);
}
fun readUrl(url:String):String{
    var c = URL(url).openConnection() as HttpURLConnection;
    c.setRequestMethod("GET");
    c.setRequestProperty("User-agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
    return c.inputStream.bufferedReader(Charsets.UTF_8).readText();
}