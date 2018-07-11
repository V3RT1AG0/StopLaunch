package com.gamerequirements.SaveCofig

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.gamerequirements.MyApplication
import com.gamerequirements.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainActivityConfig : AppCompatActivity()
{
    var adapt: ConfigAdapter? = null
    var configList: MutableList<ConfigInfo> = ArrayList()
    var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_config)
        val addConf:ImageView = findViewById<ImageView>(R.id.AddConf)
        addConf.setOnClickListener(View.OnClickListener {
            saveChangeMadeInConfig()
            startActivity(Intent(this, SelectConfig::class.java)) //TODO why not overide OnClick method
        })

        recyclerView = findViewById<RecyclerView>(R.id.configRecyclerView)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        val pref: SharedPreferences = this.getSharedPreferences(MyApplication.getSharedPrefrenceKey(), Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        var config: String? = pref.getString("Config", null)
        val gson = Gson()
        if (config != null)
        {
            val type = object : TypeToken<ArrayList<ConfigInfo>>()
            {

            }.type
            configList = gson.fromJson(config, type)
            adapt = ConfigAdapter(configList)
            recyclerView!!.adapter = adapt  //TODO How this works
        } else
        {

        }

        /*val myswitch = findViewById<Switch>(R.id.notificationSwitch)
        myswitch.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked)
            {
                FirebaseMessaging.getInstance().subscribeToTopic("notifications")
                editor.putBoolean("notification_toggle", true)
                editor.commit()
            } else
            {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("notifications")
                editor.putBoolean("notification_toggle", false)
                editor.commit()
            }
        }

        myswitch.isChecked = pref.getBoolean("notification_toggle", true)*/
    }

    override fun onResume()
    {
        super.onResume()
        Log.d("OnResume", "Executed")
        val pref: SharedPreferences = this.getSharedPreferences(MyApplication.getSharedPrefrenceKey(), Context.MODE_PRIVATE)
        var config: String? = pref.getString("Config", null)
        val gson = Gson()
        if (config != null)
        {
            val type = object : TypeToken<ArrayList<ConfigInfo>>()
            {
            }.type
            configList = gson.fromJson(config, type)
            adapt = ConfigAdapter(configList)
            recyclerView!!.adapter = adapt
        }
        adapt?.notifyDataSetChanged()
    }

    override fun onBackPressed()
    {
        super.onBackPressed()
        saveChangeMadeInConfig()
    }



    fun back(view: View)
    {
        saveChangeMadeInConfig()
        finish()
    }

   fun saveChangeMadeInConfig(){
       val gson = Gson()
       val json = gson.toJson(configList)
       val pref: SharedPreferences = this.getSharedPreferences(MyApplication.getSharedPrefrenceKey(), Context.MODE_PRIVATE)
       val editor=pref.edit()
       editor.putString("Config", json)
       editor.commit()
       Log.d("Hello", json)
   }

}




