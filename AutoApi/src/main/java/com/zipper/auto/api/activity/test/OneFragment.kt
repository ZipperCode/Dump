package com.zipper.auto.api.activity.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.zipper.auto.api.R

class OneFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(context).apply {
            text = "OneFragment"
            setOnClickListener {
                findNavController().navigate(R.id.action_oneFragment_to_twoFragment)
            }
        }
    }
}