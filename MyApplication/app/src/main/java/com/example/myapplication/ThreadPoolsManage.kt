package com.example.myapplication

import android.os.SystemClock
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

class ThreadPoolsManage{
var command:Runnable = object :Runnable {
    override fun run() {
        SystemClock.sleep(2000)
       System.out.println("")
    }
}
   fun test1(){
       var fixdThreadPool:ExecutorService = Executors.newFixedThreadPool(4)
       fixdThreadPool?.execute(command)
   }



}