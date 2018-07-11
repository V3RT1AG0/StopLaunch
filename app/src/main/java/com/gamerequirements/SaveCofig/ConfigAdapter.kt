package com.gamerequirements.SaveCofig

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.gamerequirements.MyApplication
import com.gamerequirements.R

/**
 * Created by v3rt1ag0 on 12/23/17.
 */


/**The list is stored to sharedprefrence when back button is pressed**/
class ConfigAdapter(val info: MutableList<ConfigInfo>, var context: Context = MyApplication.getContext()) : RecyclerView.Adapter<ConfigAdapter.myViewHolder>()
{
    //TODO
    var selectedposition: Int = -1
    val pref: SharedPreferences = context.getSharedPreferences(MyApplication.getSharedPrefrenceKey(), Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = pref.edit()

    init
    {


    }

    override fun getItemCount(): Int
    {
        return info.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int)
    {
        var configInfo: ConfigInfo = info.get(position)
        holder.CPU.text = "CPU: ${configInfo.CPUname}"
        holder.GPU.text = "GPU: ${configInfo.GPUname}"
        holder.RAM.text = "RAM: ${configInfo.RAMname}"
        if (selectedposition == position && configInfo.Activated == false)
        {
            configInfo.Activated = true
            holder.toggle.isChecked = true
            storeOffline(configInfo)
        } else if (selectedposition == position && configInfo.Activated == true)
        {
            configInfo.Activated = false
            holder.toggle.isChecked = false
            editor.putBoolean("StoredConfigEnabled", false)
            editor.commit()
        }else if (selectedposition==-1)
        {
            holder.toggle.isChecked = configInfo.Activated
        }
        else
        {
            configInfo.Activated = false
            holder.toggle.isChecked = false
        }


    }

    private fun storeOffline(configInfo: ConfigInfo)
    {
        editor.putString("CPUkey", configInfo.CPUid.toString())
        editor.putString("GPUkey", configInfo.GPUid.toString())
        editor.putString("RAMkey", configInfo.RAMid.toString())
        editor.putString("CPUname", configInfo.CPUname)
        editor.putString("GPUname", configInfo.GPUname)
        editor.putString("RAMname", configInfo.RAMname)
        editor.putBoolean("StoredConfigEnabled", true)
        editor.commit()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): myViewHolder
    {
        val v = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.config_cardview, parent, false)
        return myViewHolder(v)
    }

    inner class myViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {


        /*  override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

             selectedposition = getAdapterPosition();
             notifyDataSetChanged()
             //toggle.setOnCheckedChangeListener(null)
            Log.d("xxx","tag1")
             if (isChecked) {
                 if(prev==null)
                 {
                     Log.d("xxx","tag2")
                     prev = adapterPosition
                     info[adapterPosition].Activated= true
                     //notifyItemChanged(adapterPosition)
                 }
                 else
                 {
                     Log.d("xxx","tag3")
                     info.get(prev!!).Activated= false

                     info[adapterPosition].Activated= true
                     notifyItemChanged(prev!!)
                     prev = adapterPosition


                 }
                 /* for (i in info.indices) {
                      info[i].Activated = false
                  }
                  val pref: SharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
                  val editor: SharedPreferences.Editor = pref.edit()
                  editor.putInt("CPUid", info.get(getAdapterPosition()).CPUid)
                  editor.putInt("GPUid", info.get(getAdapterPosition()).GPUid)
                  editor.putInt("RAMid", info.get(getAdapterPosition()).RAMid)
                  editor.commit()*/
             }
             else {
                 Log.d("xxx","tag4")
                 info.get(getAdapterPosition()).Activated = isChecked
             }
           //  notifyDataSetChanged()
            // toggle.setOnCheckedChangeListener(this)

        }*/

        val CPU: TextView = itemview.findViewById(R.id.CPU_text) as TextView
        val GPU: TextView = itemview.findViewById(R.id.GPU_text) as TextView
        val RAM: TextView = itemview.findViewById(R.id.RAM_text) as TextView
        val toggle: Switch = itemview.findViewById(R.id.toggle) as Switch
        val bin:ImageView = itemview.findViewById(R.id.Bin) as ImageView

        init
        {
            //  toggle.setOnCheckedChangeListener(this)
            toggle.setOnClickListener { v: View ->
                selectedposition = adapterPosition
                notifyDataSetChanged()
            }

            bin.setOnClickListener{v:View ->
                if(info.get(adapterPosition).Activated)
                {
                    info.get(adapterPosition).Activated = false
                    editor.putBoolean("StoredConfigEnabled", false)
                    editor.commit()
                }
                info.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)

            }
        }

    }
}


