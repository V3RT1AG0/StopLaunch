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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivityConfig : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_config)
        findViewById(R.id.AddConf).setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, SelectConfig::class.java)) //TODO why not overide OnClick method
        })
        val recyclerView: RecyclerView = findViewById(R.id.configRecyclerView) as RecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val pref: SharedPreferences = this.getSharedPreferences("MyPref",Context.MODE_PRIVATE)
        var configList:List<ConfigInfo>
        val config: String ?= pref.getString("Config", null)
        Log.d("ConfigStroredInPref",config)
        val gson = Gson()
        if (config != null) {
            val type = object : TypeToken<ArrayList<ConfigInfo>>() {

            }.getType()
            configList = gson.fromJson(config, type)
            val adapt: ConfigAdapter = ConfigAdapter(configList)
            recyclerView.adapter = adapt  //TODO How this works

        }
    }

}




