package com.gamerequirements.SaveCofig

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.gamerequirements.JSONCustom.CustomVolleyRequest
import com.gamerequirements.R
import com.gamerequirements.Singelton
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner
import gr.escsoft.michaelprimez.searchablespinner.interfaces.IStatusListener
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener
import org.json.JSONArray


class SelectConfig : AppCompatActivity()
{
    var CPUlist = ArrayList<Information>()
    var GPUlist = ArrayList<Information>()
    var RAMlist = ArrayList<Information>()
    var CPUspinner: SearchableSpinner? = null
    var GPUspinner: SearchableSpinner? = null
    var RAMspinner: SearchableSpinner? = null
    var progressView: CircularProgressView? = null
    // var toggle: Switch? = null
    var sharedPrefrence: SharedPreferences? = null
    var ContentLL: LinearLayout ?=null
    var count = 0


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_config)
        CPUspinner = findViewById(R.id.myCpu) as SearchableSpinner
        GPUspinner = findViewById(R.id.myGpu) as SearchableSpinner
        RAMspinner = findViewById(R.id.myRam) as SearchableSpinner
        val SaveButton: Button = findViewById(R.id.SaveConfig) as Button
        progressView = findViewById(R.id.progress_view) as CircularProgressView
        progressView?.startAnimation()
        ContentLL = findViewById(R.id.ContentLL) as LinearLayout
        var CPUname: String? = null
        var GPUname: String? = null
        var RAMname: String? = null
        var CPUid: Int? = null
        var GPUid: Int? = null
        var RAMid: Int? = null
        // toggle = findViewById(R.id.toggle) as Switch
        loadAllList()
        sharedPrefrence = this.getSharedPreferences(Singelton.getSharedPrefrenceKey(), Context.MODE_PRIVATE)
        val editor = sharedPrefrence!!.edit()


        SaveButton.setOnClickListener(object : View.OnClickListener
        {
            override fun onClick(v: View?)
            {
                if (CPUid == null || GPUid == null || RAMid == null)
                {
                    Toast.makeText(v!!.context, "One or more fields is missing", Toast.LENGTH_SHORT).show()
                    return
                }
                val gson = Gson()
                var ConfigList = ArrayList<ConfigInfo>()
                val config: String? = sharedPrefrence!!.getString("Config", null)
                val type = object : TypeToken<ArrayList<ConfigInfo>>()
                {

                }.type
                if (config != null)
                {
                    ConfigList = gson.fromJson(config, type)
                }

                //val ConfigList = ArrayList<ConfigInfo>()
                ConfigList.add(ConfigInfo(CPUname!!, CPUid!!, GPUname!!, GPUid!!, RAMname!!, RAMid!!, false))
                val json = gson.toJson(ConfigList)
                Log.d("OKButton Pressed", json.toString())
                editor.putString("Config", json)
                editor.commit()
                finish()
            }


        })




        CPUspinner!!.setStatusListener(
                object : IStatusListener
                {
                    override fun spinnerIsOpening()
                    {
                        GPUspinner!!.hideEdit()
                        RAMspinner!!.hideEdit()
                    }

                    override fun spinnerIsClosing()
                    {

                    }
                })

        GPUspinner!!.setStatusListener(
                object : IStatusListener
                {
                    override fun spinnerIsOpening()
                    {
                        CPUspinner!!.hideEdit()
                        RAMspinner!!.hideEdit()
                    }

                    override fun spinnerIsClosing()
                    {

                    }
                })

        RAMspinner!!.setStatusListener(
                object : IStatusListener
                {
                    override fun spinnerIsOpening()
                    {
                        CPUspinner!!.hideEdit()
                        GPUspinner!!.hideEdit()
                    }

                    override fun spinnerIsClosing()
                    {

                    }
                })


        CPUspinner!!.setOnItemSelectedListener(
                object : OnItemSelectedListener
                {
                    override fun onNothingSelected()
                    {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onItemSelected(view: View?, position: Int, id: Long)
                    {
                        Log.d("pos", position.toString())
                        CPUname = CPUlist.get(position).title
                        CPUid = CPUlist.get(position).id


                    }

                })

        GPUspinner!!.setOnItemSelectedListener(
                object : OnItemSelectedListener
                {
                    override fun onNothingSelected()
                    {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onItemSelected(view: View?, position: Int, id: Long)
                    {
                        GPUname = GPUlist.get(position).title
                        GPUid = GPUlist.get(position).id
                    }
                })

        RAMspinner!!.setOnItemSelectedListener(
                object : OnItemSelectedListener
                {
                    override fun onNothingSelected()
                    {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onItemSelected(view: View?, position: Int, id: Long)
                    {
                        RAMname = RAMlist.get(position).title
                        RAMid = RAMlist.get(position).id

                    }

                })

    }


    fun loadAllList()
    {
        getData("cpu")
        getData("gpu")
        getRAM()
    }

    fun getRAM()
    {
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
        val adapter = ArrayAdapter<Information>(applicationContext, R.layout.drop_down_item_layout, RAMlist)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        RAMspinner!!.setAdapter(adapter)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean
    {
        if (!CPUspinner!!.isInsideSearchEditText(event))
        {
            CPUspinner!!.hideEdit()
        }
        if (!GPUspinner!!.isInsideSearchEditText(event))
        {
            GPUspinner!!.hideEdit()
        }
        if (!RAMspinner!!.isInsideSearchEditText(event))
        {
            RAMspinner!!.hideEdit()
        }
        return super.onTouchEvent(event)
    }


    fun getData(type: String): Unit
    {
        val url = "https://www.game-debate.com/$type/api/list"
        val jsonarrayrequest = JsonArrayRequest(Request.Method.GET, url, null, Response.Listener { response -> handleresponse(response, type) }, Response.ErrorListener { error -> Log.e("Error", error.toString()) })
        val requestqueue = CustomVolleyRequest.getInstance(this.applicationContext).requestQueue
        jsonarrayrequest.retryPolicy = DefaultRetryPolicy(
                7000,
                2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        requestqueue.add(jsonarrayrequest)
    }

    fun handleresponse(response: JSONArray, type: String): Unit
    {
        if (type.equals("cpu"))
        {
            for (i in 0 until response.length())
            {
                val jsonObject = response.getJSONObject(i)
                val pid = jsonObject.getInt("p_id")
                val name = jsonObject.getString("p_deriv")
                CPUlist.add(Information(pid, name))
            }
            Log.e("Kotlin", CPUlist.get(0).title)
            val adapter = ArrayAdapter<Information>(applicationContext, R.layout.drop_down_item_layout, CPUlist)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            CPUspinner!!.setAdapter(adapter)
            count++
            Log.e("pos2", sharedPrefrence!!.getInt("CPUindex", 1).toString())
        } else
        {
            for (i in 0 until response.length())
            {
                val jsonObject = response.getJSONObject(i)
                val pid = jsonObject.getInt("gc_id")
                val name = jsonObject.getString("gc_deriv")
                GPUlist.add(Information(pid, name))
            }
            Log.e("Kotlin", GPUlist.get(0).title)
            val adapter = ArrayAdapter<Information>(applicationContext, R.layout.drop_down_item_layout, GPUlist)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            GPUspinner!!.setAdapter(adapter)
            count++
        }
        Log.d("count", count.toString())
        if(count==2)
        {
            ContentLL!!.visibility = View.VISIBLE
            progressView!!.visibility = View.GONE
        }
    }

}



