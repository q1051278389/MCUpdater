package cn.tohsaka.factory.MCUpdater.utils

import java.io.File
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
    return URL(url).readText(Charsets.UTF_8);
}