package com.msbte.modelanswerpaper.adapter;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.msbte.modelanswerpaper.R;
import com.msbte.modelanswerpaper.databinding.ItemCommonUiBinding;
import com.msbte.modelanswerpaper.models.CommonItemModel;
import com.msbte.modelanswerpaper.utils.OnClickInteface;

import java.util.ArrayList;
import java.util.List;

public class CommonItemAdapter extends RecyclerView.Adapter<CommonItemAdapter.HomeViewHolder> {

    private List<CommonItemModel> commonItemModelList;
    OnClickInteface homeInterface;
    private long mLastClickTime = 0;


    public boolean checkListIsEmpty() {
        if (commonItemModelList == null || commonItemModelList.size() == 0)
            return true;
        else
            return false;
    }

    public void setData(List<CommonItemModel> list) {
        if (commonItemModelList != null)
            commonItemModelList.clear();
        else commonItemModelList = new ArrayList<>();
        commonItemModelList.addAll(list);
        notifyDataSetChanged();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        public ItemCommonUiBinding itemCommonUiBinding;

        public HomeViewHolder(ItemCommonUiBinding view) {
            super(view.getRoot());
            itemCommonUiBinding = view;
        }
    }

    public CommonItemAdapter(OnClickInteface homeInterface) {
        if (commonItemModelList == null)
            commonItemModelList = new ArrayList<>();
        this.homeInterface = homeInterface;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return commonItemModelList.get(position).name.hashCode();
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCommonUiBinding itemCommonUiBinding = ItemCommonUiBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new HomeViewHolder(itemCommonUiBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CommonItemModel exerciseResponseModel = commonItemModelList.get(position);
        holder.itemCommonUiBinding.tvText.setText("" + exerciseResponseModel.name);
        if (!TextUtils.isEmpty(exerciseResponseModel.path)) {
            Glide.with(holder.itemCommonUiBinding.imageView.getContext())
                    .load(exerciseResponseModel.path)
                    .centerInside()
                    .placeholder(R.drawable.a)
                    .error(R.drawable.a)
                    .into(holder.itemCommonUiBinding.imageView);
        }
        holder.itemCommonUiBinding.layItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                homeInterface.onCLickItem(position, exerciseResponseModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (commonItemModelList != null && commonItemModelList.size() != 0)
            return commonItemModelList.size();
        else
            return 0;
    }


}
