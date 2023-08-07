package com.msbte.modelanswerpaper.fragment_ui

import android.content.ContentValues
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.msbte.modelanswerpaper.R
import com.msbte.modelanswerpaper.adapter.DocumentAdapter
import com.msbte.modelanswerpaper.databinding.FragmentQuestionPapersBinding
import com.msbte.modelanswerpaper.models.CommonItemModel
import com.msbte.modelanswerpaper.utils.OnClickInteface

/**
 * A simple [Fragment] subclass.
 * Use the [QuestionPapersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuestionPapersFragment : Fragment() {

    private lateinit var mParam1: String
    private lateinit var mParam2: String
    private lateinit var mDatabase: DatabaseReference
    private lateinit var documentAdapter: DocumentAdapter
    private lateinit var binding: FragmentQuestionPapersBinding

    private var mRewardedAd: RewardedAd ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1).toString()
            mParam2 = requireArguments().getString(ARG_PARAM2).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        binding = FragmentQuestionPapersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backPressListner()
        setupAdapter()
        setupAds()
        setAds()
        loadData()
    }

    private fun setupAds() {
        MobileAds.initialize(requireContext()) { }
    }

    private fun loadData() {
        if (!documentAdapter.checkListIsEmpty()) {
            return
        }
        binding.progressBar.visibility = View.VISIBLE
        binding.tvText.visibility = View.GONE
        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("stream_data").child("pdf").child(mParam2)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.childrenCount != 0L) {
                        val list: MutableList<CommonItemModel> = ArrayList()
                        for (postSnapshot in snapshot.children) {
                            val model = postSnapshot.getValue(
                                CommonItemModel::class.java
                            )
                            if (model != null) {
                                list.add(model)
                            }
                        }
                        documentAdapter.setData(list)
                        binding.progressBar.visibility = View.GONE
                        binding.tvText.visibility = View.GONE
                    } else {
                        documentAdapter.setData(ArrayList())
                        binding.progressBar.visibility = View.GONE
                        binding.tvText.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    documentAdapter.setData(ArrayList())
                }
            })
    }

    private fun setupAdapter() {
        binding.imgBack.setOnClickListener { findNavController(binding.root).popBackStack() }
        if (TextUtils.isEmpty(mParam1)) {
            return
        }
        binding.tvTitle.text = mParam1

        documentAdapter = DocumentAdapter(object : OnClickInteface{
            override fun onCLickItem(exerciseId: Int, title: CommonItemModel) {
                AlertDialog.Builder(requireActivity()) // alert the person knowing they are about to close
                    .setTitle("Watch Ad to Continue")
                    .setMessage("Watch Ad to see the PDF File")
                    .setPositiveButton("Watch") { dialog, which -> loadreward(title) }
                    .setNegativeButton("Cancel") { dialog, which ->
                        dialog.dismiss() // Close the dialog
                    }
                    .show()
            }

        })
        binding.rvCommon.adapter = documentAdapter
    }

    private fun backPressListner() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController(binding.root).popBackStack()
                }
            })
    }

    //load ads
    private fun setAds() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            requireContext(), getString(R.string.reward_id),
            adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    // Handle the error.
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mRewardedAd = rewardedAd
                }
            })
    }

    // show ads
    private fun loadreward(title: CommonItemModel) {
        if (mRewardedAd != null) {
            mRewardedAd!!.show(requireActivity()) { rewardItem ->
                val checkad = rewardItem.amount.toString()
                val rewardType = rewardItem.type
            }
            mRewardedAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    Log.d(ContentValues.TAG, "Ad dismissed fullscreen content.")
                    mRewardedAd = null
                    //                    setAds();
                    binding.progressBar.visibility = View.GONE
                    val bundle = Bundle()
                    bundle.putString(ARG_PARAM1, title.name)
                    bundle.putString(ARG_PARAM2, title.path)
                    findNavController(binding.root)
                        .navigate(
                            R.id.action_questionPapersFragment_to_questionPaperViewerFragment,
                            bundle
                        )
                }
            }
        } else {

//            StartAppAd.showAd(getContext());
            binding.progressBar.visibility = View.GONE
            Log.d(ContentValues.TAG, "The rewarded ad wasn't ready yet.I'm Calling Again")
            //            Toast.makeText(getContext(), "No Ads available right now. Try after some time", Toast.LENGTH_LONG).show();
            val bundle = Bundle()
            bundle.putString(ARG_PARAM1, title.name)
            bundle.putString(ARG_PARAM2, title.path)
            findNavController(binding.root)
                .navigate(R.id.action_questionPapersFragment_to_questionPaperViewerFragment, bundle)
        }
    }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        fun newInstance(param1: String?, param2: String?): QuestionPapersFragment {
            val fragment = QuestionPapersFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}