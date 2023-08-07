package com.msbte.modelanswerpaper

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.msbte.modelanswerpaper.databinding.CandleIteamBinding

class MyProjectDownloadAdapter(var mData: MutableList<model>) :
    RecyclerView.Adapter<MyProjectDownloadAdapter.MyViewHolder>() {
    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val binding = CandleIteamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = mData[position]
        holder.binding.mpname.text = model.filename
        holder.binding.downloadfile2.setOnClickListener {
            val intent = Intent(holder.binding.downloadfile2.context, download::class.java)
            intent.putExtra("filename", model.filename)
            intent.putExtra("fileurl", model.fileurl)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            holder.binding.downloadfile2.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }


    inner class MyViewHolder(
        val binding: CandleIteamBinding,
    ) : RecyclerView.ViewHolder(binding.root)
}