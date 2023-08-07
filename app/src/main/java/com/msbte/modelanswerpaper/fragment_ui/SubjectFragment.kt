package com.msbte.modelanswerpaper.fragment_ui

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.msbte.modelanswerpaper.R
import com.msbte.modelanswerpaper.adapter.CommonItemAdapter
import com.msbte.modelanswerpaper.databinding.FragmentSubjectBinding
import com.msbte.modelanswerpaper.models.CommonItemModel
import com.msbte.modelanswerpaper.utils.OnClickInteface

/**
 * A simple [Fragment] subclass.
 * Use the [SubjectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubjectFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var mParam1: String
    private lateinit var mParam2: String
    private lateinit var mDatabase: DatabaseReference
    private lateinit var commonItemAdapter: CommonItemAdapter
    private lateinit var binding: FragmentSubjectBinding

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
        binding = FragmentSubjectBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backPressListner()
        setupAdapter()
        setupAds()
        loadData()
    }

    private fun setupAds() {
        MobileAds.initialize(requireContext()) { }
    }

    private fun loadData() {
        if (!commonItemAdapter.checkListIsEmpty()) {
            return
        }
        binding.progressBar.visibility = View.VISIBLE
        binding.tvText.visibility = View.GONE
        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("stream_data").child("subject").child(mParam2)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.childrenCount != 0L) {
                        val list: MutableList<CommonItemModel?> = ArrayList()
                        for (postSnapshot in snapshot.children) {
                            val model = postSnapshot.getValue(
                                CommonItemModel::class.java
                            )
                            list.add(model)
                        }
                        commonItemAdapter.setData(list)
                        binding.progressBar.visibility = View.GONE
                        binding.tvText.visibility = View.GONE
                    } else {
                        commonItemAdapter.setData(ArrayList())
                        binding.progressBar.visibility = View.GONE
                        binding.tvText.visibility = View.VISIBLE
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    commonItemAdapter.setData(ArrayList())
                }
            })
    }

    private fun setupAdapter() {
        binding.imgBack.setOnClickListener { findNavController(binding.root).popBackStack() }
        if (TextUtils.isEmpty(mParam1)) {
            return
        }
        binding.tvTitle.text = mParam1

        commonItemAdapter = CommonItemAdapter(mutableListOf(),object : OnClickInteface {
            override fun onCLickItem(exerciseId: Int, title: CommonItemModel) {
                val bundle = Bundle()
                bundle.putString(ARG_PARAM1, title.name)
                bundle.putString(ARG_PARAM2, title.sub_domain)
                findNavController(binding.root)
                    .navigate(R.id.action_subjectFragment_to_questionPapersFragment, bundle)

            }
        })
        binding.rvCommon.adapter = commonItemAdapter
    }

    private fun backPressListner() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController(binding.root).popBackStack()
                }
            })
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubjectFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): SubjectFragment {
            val fragment = SubjectFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}