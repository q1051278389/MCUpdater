package cn.tohsaka.factory.MCUpdater.utils

import java.io.File

fun readFile(file: String):String{
    return File(file).readText(Charsets.UTF_8);
}