package com.msbte.modelanswerpaper.adapter

import android.annotation.SuppressLint
import android.os.SystemClock
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.msbte.modelanswerpaper.R
import com.msbte.modelanswerpaper.databinding.ItemCommonUiBinding
import com.msbte.modelanswerpaper.models.CommonItemModel
import com.msbte.modelanswerpaper.utils.OnClickInteface

class CommonItemAdapter(
    var commonItemModelList: MutableList<CommonItemModel?> = mutableListOf(),
    var homeInterface: OnClickInteface
) :
    RecyclerView.Adapter<CommonItemAdapter.HomeViewHolder>() {
    private var mLastClickTime: Long = 0
    fun checkListIsEmpty(): Boolean {
        return commonItemModelList.size == 0
    }

    fun setData(list: MutableList<CommonItemModel?>?) {
        commonItemModelList.clear()
        commonItemModelList.addAll(list!!)
        notifyDataSetChanged()
    }

    inner class HomeViewHolder(var itemCommonUiBinding: ItemCommonUiBinding) :
        RecyclerView.ViewHolder(
            itemCommonUiBinding.root
        )


    override fun getItemId(position: Int): Long {
        return commonItemModelList[position]!!.name.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemCommonUiBinding =
            ItemCommonUiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(itemCommonUiBinding)
    }

    override fun onBindViewHolder(
        holder: HomeViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val exerciseResponseModel = commonItemModelList[position]
        holder.itemCommonUiBinding.tvText.text = "" + exerciseResponseModel!!.name
        if (!TextUtils.isEmpty(exerciseResponseModel.path)) {
            Glide.with(holder.itemCommonUiBinding.imageView.context)
                .load(exerciseResponseModel.path)
                .centerInside()
                .placeholder(R.drawable.a)
                .error(R.drawable.a)
                .into(holder.itemCommonUiBinding.imageView)
        }
        holder.itemCommonUiBinding.layItem.setOnClickListener(View.OnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@OnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            homeInterface.onCLickItem(position, exerciseResponseModel)
        })
    }

    override fun getItemCount(): Int {
        return if (commonItemModelList.size != 0) commonItemModelList.size else 0
    }
}