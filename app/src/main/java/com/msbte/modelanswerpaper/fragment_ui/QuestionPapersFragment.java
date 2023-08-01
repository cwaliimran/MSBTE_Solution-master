package com.msbte.modelanswerpaper.fragment_ui;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.msbte.modelanswerpaper.R;
import com.msbte.modelanswerpaper.adapter.DocumentAdapter;
import com.msbte.modelanswerpaper.databinding.FragmentQuestionPapersBinding;
import com.msbte.modelanswerpaper.models.CommonItemModel;
import com.msbte.modelanswerpaper.utils.OnClickInteface;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionPapersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionPapersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference mDatabase;
    private DocumentAdapter documentAdapter;
    private FragmentQuestionPapersBinding binding;
    private RewardedAd mRewardedAd;

    public QuestionPapersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionPapersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionPapersFragment newInstance(String param1, String param2) {
        QuestionPapersFragment fragment = new QuestionPapersFragment();
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
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_question_papers, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backPressListner();
        setupAdapter();
        setupAds();
        setAds();

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
        if (documentAdapter != null && !documentAdapter.checkListIsEmpty()) {
            return;
        }
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.tvText.setVisibility(View.GONE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("stream_data").child("pdf").child(mParam2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null && snapshot.getChildrenCount() != 0) {
                    List<CommonItemModel> list = new ArrayList<>();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        CommonItemModel model = postSnapshot.getValue(CommonItemModel.class);
                        list.add(model);
                    }
                    documentAdapter.setData(list);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.tvText.setVisibility(View.GONE);
                } else {
                    documentAdapter.setData(new ArrayList<>());
                    binding.progressBar.setVisibility(View.GONE);
                    binding.tvText.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                documentAdapter.setData(new ArrayList<>());
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
        if (documentAdapter != null && !documentAdapter.checkListIsEmpty()) {
            return;
        }
        documentAdapter = new DocumentAdapter(new OnClickInteface() {
            @Override
            public void onCLickItem(int exerciseId, CommonItemModel title) {
                new AlertDialog.Builder(requireActivity()) // alert the person knowing they are about to close
                        .setTitle("Watch Ad to Continue")
                        .setMessage("Watch Ad to see the PDF File")
                        .setPositiveButton("Watch", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                loadreward(title);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); // Close the dialog
                            }
                        })
                        .show();


            }
        });

        binding.rvCommon.setAdapter(documentAdapter);

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

    //load ads
    private void setAds() {

        AdRequest adRequest = new AdRequest.Builder().build();

        RewardedAd.load(getContext(), getString(R.string.reward_id),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.

                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;

                    }
                });

    }

    // show ads
    private void loadreward(CommonItemModel title) {

        if (mRewardedAd != null) {
            mRewardedAd.show(getActivity(), new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    String checkad = String.valueOf(rewardItem.getAmount());
                    String rewardType = rewardItem.getType();


                }
            });

            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    Log.d(TAG, "Ad dismissed fullscreen content.");
                    mRewardedAd = null;
//                    setAds();
                    binding.progressBar.setVisibility(View.GONE);
                    Bundle bundle = new Bundle();
                    bundle.putString(ARG_PARAM1, title.name);
                    bundle.putString(ARG_PARAM2, title.path);
                    Navigation.findNavController(binding.getRoot())
                            .navigate(R.id.action_questionPapersFragment_to_questionPaperViewerFragment, bundle);

                }
            });

        } else {

//            StartAppAd.showAd(getContext());
            binding.progressBar.setVisibility(View.GONE);
            Log.d(TAG, "The rewarded ad wasn't ready yet.I'm Calling Again");
//            Toast.makeText(getContext(), "No Ads available right now. Try after some time", Toast.LENGTH_LONG).show();
            Bundle bundle = new Bundle();
            bundle.putString(ARG_PARAM1, title.name);
            bundle.putString(ARG_PARAM2, title.path);
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_questionPapersFragment_to_questionPaperViewerFragment, bundle);

        }


    }
}