package com.msbte.modelanswerpaper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.msbte.modelanswerpaper.databinding.MobileBinding
import com.msbte.modelanswerpaper.interfaces.OnItemClick

class ImageAdapter(private val mobileValues: Array<String>, var listener: OnItemClick) :
    RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {
    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val binding = MobileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = mobileValues[position]

        holder.binding.gridItemLabel.text = model

        // set image based on selected text

        // set image based on selected text
        val imageView = holder.binding.gridItemImage

        val mobile = mobileValues[position]

        when (mobile) {
            "MSBTE Content" -> {
                imageView.setImageResource(R.drawable.a)
            }

            "Projects" -> {
                imageView.setImageResource(R.drawable.b)
            }

            "Question Paper" -> {
                imageView.setImageResource(R.drawable.c)
            }

            "Model Answer" -> {
                imageView.setImageResource(R.drawable.d)
            }

            "MSBTE Solution Pro" -> {
                imageView.setImageResource(R.drawable.pro)
            }

            "Video Lectures" -> {
                imageView.setImageResource(R.drawable.f)
            }

            else -> {
                imageView.setImageResource(R.drawable.refresh)
            }
        }
        holder.itemView.setOnClickListener {
            listener.onClick(position, data = model)
        }
    }

    override fun getItemCount(): Int {
        return mobileValues.size
    }


    inner class MyViewHolder(
        val binding: MobileBinding,
    ) : RecyclerView.ViewHolder(binding.root)
}