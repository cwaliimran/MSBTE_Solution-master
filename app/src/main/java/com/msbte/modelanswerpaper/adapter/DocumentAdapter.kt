package com.msbte.modelanswerpaper.adapter

import android.annotation.SuppressLint
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.msbte.modelanswerpaper.databinding.ItemDownloadOnlineBinding
import com.msbte.modelanswerpaper.models.CommonItemModel
import com.msbte.modelanswerpaper.utils.OnClickInteface

class DocumentAdapter(var homeInterface: OnClickInteface) :
    RecyclerView.Adapter<DocumentAdapter.HomeViewHolder>() {
    private var commonItemModelList: MutableList<CommonItemModel> = mutableListOf()
    private var mLastClickTime: Long = 0
    fun checkListIsEmpty(): Boolean {
        return commonItemModelList.size == 0
    }

    fun setData(list: MutableList<CommonItemModel>) {
        commonItemModelList.clear()
        commonItemModelList.addAll(list)
        notifyDataSetChanged()
    }

    inner class HomeViewHolder(var itemCommonUiBinding: ItemDownloadOnlineBinding) :
        RecyclerView.ViewHolder(
            itemCommonUiBinding.root
        )

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return commonItemModelList[position].name.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemCommonUiBinding =
            ItemDownloadOnlineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(itemCommonUiBinding)
    }

    override fun onBindViewHolder(
        holder: HomeViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val exerciseResponseModel = commonItemModelList[position]
        holder.itemCommonUiBinding.tvText.text = "" + exerciseResponseModel.name
        holder.itemCommonUiBinding.btnOnline.setOnClickListener(View.OnClickListener {
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