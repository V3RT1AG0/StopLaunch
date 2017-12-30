package com.gamerequirements.SaveCofig

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.gamerequirements.R
import com.gamerequirements.Singelton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivityConfig : AppCompatActivity()
{
    var adapt: ConfigAdapter? = null
    var configList: List<ConfigInfo> = ArrayList()
    var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_config)
        findViewById(R.id.AddConf).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, SelectConfig::class.java)) //TODO why not overide OnClick method
        })
        recyclerView = findViewById(R.id.configRecyclerView) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        val pref: SharedPreferences = this.getSharedPreferences(Singelton.getSharedPrefrenceKey(), Context.MODE_PRIVATE)

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
    }

    override fun onResume()
    {
        super.onResume()
        Log.d("OnResume", "Executed")
        val pref: SharedPreferences = this.getSharedPreferences(Singelton.getSharedPrefrenceKey(), Context.MODE_PRIVATE)
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
        val gson = Gson()
        val json = gson.toJson(configList)
        val pref: SharedPreferences = this.getSharedPreferences(Singelton.getSharedPrefrenceKey(), Context.MODE_PRIVATE)
        val editor=pref.edit()
        editor.putString("Config", json)
        editor.commit()
        Log.d("Hello", json)
    }

}




