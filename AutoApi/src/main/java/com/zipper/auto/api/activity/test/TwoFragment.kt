package com.zipper.auto.api.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.zipper.auto.api.R

class TwoFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(context).apply {
            text = "TwoFragment"
            setOnClickListener {
                findNavController().navigate(R.id.action_twoFragment_to_threeFragment)
            }
        }
    }
}