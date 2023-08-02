package com.msbte.modelanswerpaper

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.msbte.modelanswerpaper.myadapter.myviewholder

class myadapter(options: FirebaseRecyclerOptions<model?>) :
    FirebaseRecyclerAdapter<model, myviewholder>(options) {
    override fun onBindViewHolder(holder: myviewholder, position: Int, model: model) {
        holder.name.text = model.filename
        holder.download.setOnClickListener {
            val intent = Intent(holder.download.context, download::class.java)
            intent.putExtra("filename", model.filename)
            intent.putExtra("fileurl", model.fileurl)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            holder.download.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.candle_iteam, parent, false)
        return myviewholder(view)
    }

    inner class myviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var name: TextView
        lateinit var download: Button

        init {
            name = itemView.findViewById(R.id.mpname)
            download = itemView.findViewById(R.id.downloadfile2)
        }
    }
}