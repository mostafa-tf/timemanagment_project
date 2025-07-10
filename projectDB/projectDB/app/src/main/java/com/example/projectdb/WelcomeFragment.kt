package com.example.projectdb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController



class WelcomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vw = inflater.inflate(R.layout.fragment_welcome, container, false)
        val btn=vw.findViewById<Button>(R.id.conquer_button)
        btn.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_tasksFragment)

        }

        return vw
    }





}

