package com.msbte.modelanswerpaper.fragment_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.ads.MobileAds
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.msbte.modelanswerpaper.R
import com.msbte.modelanswerpaper.adapter.CommonItemAdapter
import com.msbte.modelanswerpaper.databinding.FragmentStreamBinding
import com.msbte.modelanswerpaper.models.CommonItemModel
import com.msbte.modelanswerpaper.utils.OnClickInteface

/**
 * A simple [Fragment] subclass.
 * Use the [StreamFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StreamFragment : Fragment() {

    private lateinit var mParam1: String
    private lateinit var mParam2: String

    private lateinit var binding: FragmentStreamBinding

    private lateinit var commonItemAdapter: CommonItemAdapter
    private lateinit var options: FirebaseRecyclerOptions<CommonItemModel>
    private lateinit var mDatabase: DatabaseReference
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
        binding = FragmentStreamBinding.inflate(layoutInflater)
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
        mDatabase.child("stream_data").child("stream")
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
        binding.imgBack.setOnClickListener { //                showAlertDialog("Exit", "Are you sure, want to exist from app?", 2);
            requireActivity().finish()
        }
        commonItemAdapter = CommonItemAdapter(mutableListOf(),object : OnClickInteface {
            override fun onCLickItem(exerciseId: Int, title: CommonItemModel) {
                val bundle = Bundle()
                bundle.putString(ARG_PARAM1, title.name)
                bundle.putString(ARG_PARAM2, title.sub_domain)
                findNavController(binding.root)
                    .navigate(R.id.action_streamFragment_to_semesterFragment, bundle)

            }
        })
        binding.rvCommon.adapter = commonItemAdapter
    }

    private fun backPressListner() {
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
//                        showAlertDialog("Exit", "Are you sure, want to exist from app?", 2);
                    requireActivity().finish()
                }
            })

    }

    private fun showAlertDialog(title: String, subtitle: String, i: Int) {
        AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setTitle(title)
            .setMessage(subtitle)
            .setPositiveButton(android.R.string.yes) { dialog, which -> // Continue with delete operation
                requireActivity().finish()
                dialog.dismiss()
            } // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(android.R.string.cancel) { dialogInterface, i -> dialogInterface.cancel() }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    override fun onStart() {
        super.onStart()
    }

    companion object {

        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        fun newInstance(param1: String?, param2: String?): StreamFragment {
            val fragment = StreamFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}