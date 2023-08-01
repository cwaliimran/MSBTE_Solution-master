package com.msbte.modelanswerpaper.fragment_ui;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.msbte.modelanswerpaper.R;
import com.msbte.modelanswerpaper.adapter.CommonItemAdapter;
import com.msbte.modelanswerpaper.databinding.FragmentSemesterBinding;
import com.msbte.modelanswerpaper.models.CommonItemModel;
import com.msbte.modelanswerpaper.utils.OnClickInteface;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SemesterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SemesterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentSemesterBinding binding;
    private CommonItemAdapter commonItemAdapter;
    private DatabaseReference mDatabase;

    public SemesterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SemesterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SemesterFragment newInstance(String param1, String param2) {
        SemesterFragment fragment = new SemesterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (binding == null)
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_semester, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backPressListner();
        setupAdapter();
        setupAds();
        loadData();
    }

    private void setupAds() {
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

    }

    private void loadData() {
        if (commonItemAdapter != null && !commonItemAdapter.checkListIsEmpty()) {
            return;
        }
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.tvText.setVisibility(View.GONE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("stream_data").child("semester").child(mParam2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null && snapshot.getChildrenCount() != 0) {
                    List<CommonItemModel> list = new ArrayList<>();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        CommonItemModel model = postSnapshot.getValue(CommonItemModel.class);
                        list.add(model);
                    }
                    commonItemAdapter.setData(list);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.tvText.setVisibility(View.GONE);
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.tvText.setVisibility(View.VISIBLE);
                    commonItemAdapter.setData(new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                commonItemAdapter.setData(new ArrayList<>());
            }
        });
    }

    private void setupAdapter() {
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(binding.getRoot()).popBackStack();

            }
        });
        if (TextUtils.isEmpty(mParam1)) {
            return;
        }
        binding.tvTitle.setText(mParam1);
        if (commonItemAdapter != null && !commonItemAdapter.checkListIsEmpty()) {
            return;
        }
        commonItemAdapter = new CommonItemAdapter(new OnClickInteface() {
            @Override
            public void onCLickItem(int exerciseId, CommonItemModel title) {
                Bundle bundle = new Bundle();
                bundle.putString(ARG_PARAM1, title.name);
                bundle.putString(ARG_PARAM2, title.sub_domain);
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_semesterFragment_to_subjectFragment, bundle);

            }
        });

        binding.rvCommon.setAdapter(commonItemAdapter);

    }

    private void backPressListner() {

        getActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        Navigation.findNavController(binding.getRoot()).popBackStack();
                    }
                });


    }


}