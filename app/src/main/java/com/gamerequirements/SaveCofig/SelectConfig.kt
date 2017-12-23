package com.gamerequirements.SaveCofig

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.gamerequirements.JSONCustom.CustomVolleyRequest
import com.gamerequirements.R
import org.json.JSONArray
import org.json.JSONObject
import android.R.layout.simple_spinner_dropdown_item
import android.widget.ArrayAdapter



class SelectConfig : AppCompatActivity() {


    var datalist = ArrayList<Information>()
    var CPUspinner:Spinner?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_config)
        CPUspinner=findViewById(R.id.myCpu) as Spinner
        loadAllList()



    }

    fun loadAllList(): Unit {
        getCpu()
       // getGpu()
       // getRam()
    }


    fun getCpu(): Unit{

        val url = "https://www.game-debate.com/cpu/api/list"
        val jsonarrayrequest = JsonArrayRequest(Request.Method.GET, url, null, Response.Listener { response -> handleresponse(response) }, Response.ErrorListener { error -> Log.e("Error", error.toString()) })
        val requestqueue = CustomVolleyRequest.getInstance(this.applicationContext).requestQueue
        requestqueue.add(jsonarrayrequest)
    }

    fun handleresponse(response:JSONArray):Unit {
        for (i in 0 until response.length()) {
            val jsonObject = response.getJSONObject(i)
            val gid = jsonObject.getInt("p_id")
            val name = jsonObject.getString("p_deriv")
                datalist.add(Information(gid, name))
        }
        Log.e("Kotlin",datalist.get(0).title)

        val adapter = ArrayAdapter<Information>(applicationContext, android.R.layout.simple_spinner_dropdown_item, datalist)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        CPUspinner!!.setAdapter(adapter)
        //fillSpinner(datalist)
    }

    fun fillSpinner(datalist:ArrayList<Information>):Unit {
        CPUspinner!!.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{



            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }
}
