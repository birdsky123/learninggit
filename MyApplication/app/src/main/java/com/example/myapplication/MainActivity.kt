package com.example.myapplication
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import dalvik.system.BaseDexClassLoader
import dalvik.system.DexClassLoader

class MainActivity : AppCompatActivity() {
    var context: Context? = null
    var textView:MyTextView  ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.myTextView)
        context = this
      //  Toast.makeText(context,"CESHI122",Toast.LENGTH_LONG).show()
        initTest()
        onCLick()
    }

    public fun initTest(){
        Log.d("TAG","The height is-->"+  textView?.measuredHeight)
        Log.d("TAG","The width is-->"+   textView?.measuredWidth)
    }


   public fun onCLick(){
       textView?.setOnClickListener(object:View.OnClickListener{
           override fun onClick(v: View?) {
               Toast.makeText(context,"CESHI122",Toast.LENGTH_LONG).show()
           }
       })
       textView?.setOnClickListener {

       }


   }
}
