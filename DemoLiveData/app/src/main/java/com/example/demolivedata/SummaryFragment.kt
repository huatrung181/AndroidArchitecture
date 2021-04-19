package com.example.demolivedata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_summary.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SummaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SummaryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var medalViewModel: MedalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        medalViewModel = ViewModelProviders.of(activity!!).get(MedalViewModel::class.java)

        return inflater.inflate(R.layout.fragment_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        medalViewModel.numberOfGoldMedal.observe(activity!!, Observer {
            displayMedal()
        })

        medalViewModel.numberOfSilverMedal.observe(activity!!, Observer {
            displayMedal()
        })

        medalViewModel.numberOfBronzeMedal.observe(activity!!, Observer {
            displayMedal()
        })
    }

    private fun displayMedal() {

        var totalMedal : Int= medalViewModel?.numberOfGoldMedal.value!!.plus(medalViewModel?.numberOfSilverMedal.value!!).plus(medalViewModel.numberOfBronzeMedal.value!!)
        tvNumberOfMedal.text = activity?.getString(R.string.number_of_medal_label, totalMedal)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SummaryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SummaryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}