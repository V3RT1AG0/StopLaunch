package com.gamerequirements.SaveCofig

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.JsonArrayRequest
import com.gamerequirements.JSONCustom.CustomVolleyRequest
import com.gamerequirements.R
import org.json.JSONArray
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner
import android.view.MotionEvent
import android.view.View
import gr.escsoft.michaelprimez.searchablespinner.interfaces.IStatusListener
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener
import android.R.id.edit
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SelectConfig : AppCompatActivity() {


    var CPUlist = ArrayList<Information>()
    var GPUlist = ArrayList<Information>()
    var RAMlist = ArrayList<Information>()
    var CPUspinner: SearchableSpinner? = null
    var GPUspinner: SearchableSpinner? = null
    var RAMspinner: SearchableSpinner? = null
   // var toggle: Switch? = null
    var sharedPrefrence: SharedPreferences?= null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_config)
        CPUspinner = findViewById(R.id.myCpu) as SearchableSpinner
        GPUspinner = findViewById(R.id.myGpu) as SearchableSpinner
        RAMspinner = findViewById(R.id.myRam) as SearchableSpinner
        val SaveButton: Button = findViewById(R.id.SaveConfig) as Button
        var CPUname:String?=null
        var GPUname:String?=null
        var RAMname:String?=null
        var CPUid:Int?=null
        var GPUid:Int?=null
        var RAMid:Int?=null


       // toggle = findViewById(R.id.toggle) as Switch
        loadAllList()
        sharedPrefrence= this.getSharedPreferences("MyPref",Context.MODE_PRIVATE)
        var editor = sharedPrefrence!!.edit()
        var checked: Boolean = sharedPrefrence!!.getBoolean("toggle", false)
        Log.d("Checked1", checked.toString())
       // toggle!!.setChecked(checked)


        SaveButton.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                val gson = Gson()
                val config: String ?= sharedPrefrence!!.getString("Config", "")
                val type = object : TypeToken<ArrayList<ConfigInfo>>() {

                }.getType()
               // configList = gson.fromJson(config, type)
                var ConfigList = ArrayList<ConfigInfo>()
                ConfigList = gson.fromJson(config, type)
                if(CPUname!=null&&GPUname!=null&&RAMname!=null) {
                    ConfigList.add(ConfigInfo(CPUname!!, CPUid!!, GPUname!!, GPUid!!, RAMname!!, RAMid!!,false))
                }

                val json = gson.toJson(ConfigList)
                Log.d("OKButton Pressed",json.toString())
                editor.putString("Config", json)
                editor.commit()

            }

        })


     /*   toggle!!.setOnCheckedChangeListener { buttonView, isChecked ->
            editor.putBoolean("toggle", isChecked)
            editor.commit()
            Log.d("Checked2", sharedPrefrence!!.getBoolean("toggle", false).toString());
        }*/


        CPUspinner!!.setStatusListener(object : IStatusListener {
            override fun spinnerIsOpening() {
                GPUspinner!!.hideEdit()
                RAMspinner!!.hideEdit()
            }

            override fun spinnerIsClosing() {

            }
        })

        GPUspinner!!.setStatusListener(object : IStatusListener {
            override fun spinnerIsOpening() {
                CPUspinner!!.hideEdit()
                RAMspinner!!.hideEdit()
            }

            override fun spinnerIsClosing() {

            }
        })

        RAMspinner!!.setStatusListener(object : IStatusListener {
            override fun spinnerIsOpening() {
                CPUspinner!!.hideEdit()
                GPUspinner!!.hideEdit()
            }

            override fun spinnerIsClosing() {

            }
        })


      CPUspinner!!.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onNothingSelected() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(view: View?, position: Int, id: Long) {
                Log.d("pos",position.toString())
                CPUname = CPUlist.get(position).title
                CPUid = CPUlist.get(position).id

               /* editor.putInt("CPU", CPUlist.get(position).id)
                editor.putInt("CPUindex", position)
                editor.commit();*/

            }

        })

        GPUspinner!!.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onNothingSelected() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(view: View?, position: Int, id: Long) {

                GPUname = GPUlist.get(position).title
                GPUid = GPUlist.get(position).id
                /*editor.putInt("GPU", GPUlist.get(position).id)
                editor.putInt("GPUindex", position)
                editor.commit();*/

            }

        })

        RAMspinner!!.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onNothingSelected() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(view: View?, position: Int, id: Long) {
                RAMname = RAMlist.get(position).title
                RAMid = RAMlist.get(position).id
               /* editor.putInt("RAM", RAMlist.get(position).id)
                editor.putInt("RAMindex", position)
                editor.commit();*/

            }

        })

    }


    fun loadAllList(): Unit {
        getData("cpu")
        getData("gpu")
        getRAM()
    }

    fun getRAM() {
        RAMlist.add(Information(1, "1GB"))
        RAMlist.add(Information(2, "2GB"))
        RAMlist.add(Information(3, "3GB"))
        RAMlist.add(Information(4, "4GB"))
        RAMlist.add(Information(5, "5GB"))
        RAMlist.add(Information(6, "6GB"))
        RAMlist.add(Information(8, "8GB"))
        RAMlist.add(Information(10, "10GB"))
        RAMlist.add(Information(12, "12GB"))
        RAMlist.add(Information(14, "14GB"))
        RAMlist.add(Information(16, "16GB"))
        RAMlist.add(Information(32, "32GB"))
        RAMlist.add(Information(64, "64GB"))
        RAMlist.add(Information(128, "128GB"))
        val adapter = ArrayAdapter<Information>(applicationContext, android.R.layout.simple_spinner_dropdown_item, RAMlist)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        RAMspinner!!.setAdapter(adapter)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!CPUspinner!!.isInsideSearchEditText(event)) {
            CPUspinner!!.hideEdit()
        }
        if (!GPUspinner!!.isInsideSearchEditText(event)) {
            GPUspinner!!.hideEdit()
        }
        if (!RAMspinner!!.isInsideSearchEditText(event)) {
            RAMspinner!!.hideEdit()
        }
        return super.onTouchEvent(event)
    }


    fun getData(type: String): Unit {

        val url = "https://www.game-debate.com/$type/api/list"
        val jsonarrayrequest = JsonArrayRequest(Request.Method.GET, url, null, Response.Listener { response -> handleresponse(response, type) }, Response.ErrorListener { error -> Log.e("Error", error.toString()) })
        val requestqueue = CustomVolleyRequest.getInstance(this.applicationContext).requestQueue
        requestqueue.add(jsonarrayrequest)
    }

    fun handleresponse(response: JSONArray, type: String): Unit {
        if (type.equals("cpu")) {
            for (i in 0 until response.length()) {
                val jsonObject = response.getJSONObject(i)
                val pid = jsonObject.getInt("p_id")
                val name = jsonObject.getString("p_deriv")
                CPUlist.add(Information(pid, name))
            }
            Log.e("Kotlin", CPUlist.get(0).title)
            val adapter = ArrayAdapter<Information>(applicationContext, android.R.layout.simple_spinner_dropdown_item, CPUlist)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            CPUspinner!!.setAdapter(adapter)
            Log.e("pos2",sharedPrefrence!!.getInt("CPUindex", 1).toString())
        }

        else

        {
            for (i in 0 until response.length()) {
                val jsonObject = response.getJSONObject(i)
                val pid = jsonObject.getInt("gc_id")
                val name = jsonObject.getString("gc_deriv")
                GPUlist.add(Information(pid, name))
            }
            Log.e("Kotlin", GPUlist.get(0).title)
            val adapter = ArrayAdapter<Information>(applicationContext, android.R.layout.simple_spinner_dropdown_item, GPUlist)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            GPUspinner!!.setAdapter(adapter)
        }

        /*GPUspinner!!.setSelectedItem(sharedPrefrence!!.getInt("GPUindex", 1))
        RAMspinner!!.setSelectedItem(sharedPrefrence!!.getInt("RAMindex", 1))*/
    }

}



