package com.msbte.modelanswerpaper.adapter;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.msbte.modelanswerpaper.databinding.ItemCommonUiBinding;
import com.msbte.modelanswerpaper.databinding.ItemDownloadOnlineBinding;
import com.msbte.modelanswerpaper.models.CommonItemModel;
import com.msbte.modelanswerpaper.utils.OnClickInteface;

import java.util.ArrayList;
import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.HomeViewHolder> {

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
        public ItemDownloadOnlineBinding itemCommonUiBinding;

        public HomeViewHolder(ItemDownloadOnlineBinding view) {
            super(view.getRoot());
            itemCommonUiBinding = view;
        }
    }

    public DocumentAdapter(OnClickInteface homeInterface) {
        if (commonItemModelList == null)
            commonItemModelList = new ArrayList<>();
        this.homeInterface = homeInterface;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return commonItemModelList.get(position).name.hashCode();
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDownloadOnlineBinding itemCommonUiBinding =
                ItemDownloadOnlineBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new HomeViewHolder(itemCommonUiBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CommonItemModel exerciseResponseModel = commonItemModelList.get(position);
        holder.itemCommonUiBinding.tvText.setText("" + exerciseResponseModel.name);
        holder.itemCommonUiBinding.btnOnline.setOnClickListener(new View.OnClickListener() {
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
