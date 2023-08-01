package com.msbte.modelanswerpaper.fragment_ui;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import com.msbte.modelanswerpaper.databinding.FragmentStreamBinding;
import com.msbte.modelanswerpaper.models.CommonItemModel;
import com.msbte.modelanswerpaper.utils.OnClickInteface;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StreamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StreamFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentStreamBinding binding;
    private CommonItemAdapter commonItemAdapter;
    private FirebaseRecyclerOptions<CommonItemModel> options;
    private DatabaseReference mDatabase;

    public StreamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StreamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StreamFragment newInstance(String param1, String param2) {
        StreamFragment fragment = new StreamFragment();
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
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stream, container, false);
        }
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
        mDatabase.child("stream_data").child("stream").addValueEventListener(new ValueEventListener() {
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
                    commonItemAdapter.setData(new ArrayList<>());
                    binding.progressBar.setVisibility(View.GONE);
                    binding.tvText.setVisibility(View.VISIBLE);
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

//                showAlertDialog("Exit", "Are you sure, want to exist from app?", 2);
                getActivity().finish();
            }
        });
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
                        .navigate(R.id.action_streamFragment_to_semesterFragment, bundle);
            }
        });
        binding.rvCommon.setAdapter(commonItemAdapter);
    }


    private void backPressListner() {

        getActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
//                        showAlertDialog("Exit", "Are you sure, want to exist from app?", 2);
                        getActivity().finish();
                    }
                });

        // for getting calback of exercise solved if true --> refetch data list.
    /*    Navigation.findNavController(binding.getRoot()).
                getCurrentBackStackEntry().getSavedStateHandle().getLiveData(ARG_PARAM1)
                .observe(getViewLifecycleOwner(), result -> {
                    try {
//                        Log.d("TAG", "getCurrentBackStackEntry: " + ((Boolean) result));
                        if (((Boolean) result)) {
                            reload();
                        }

                    } catch (Exception exception) {
                        Log.d("TAG", " ");
                    }
                });

*/
    }


    private void showAlertDialog(String title, String subtitle, int i) {
        new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setTitle(title)
                .setMessage(subtitle)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        getActivity().finish();
                        dialog.dismiss();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onStart() {
        super.onStart();

    }


}