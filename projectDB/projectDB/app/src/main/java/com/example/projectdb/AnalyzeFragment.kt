package com.example.projectdb

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView


class AnalyzeFragment : Fragment() {

lateinit var progressbar:ProgressBar
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_analyze, container, false)
     val nbtasks=AnalyzeFragmentArgs.fromBundle(requireArguments()).nbtasks
        val nbdonetasks=AnalyzeFragmentArgs.fromBundle(requireArguments()).nbdonetasks
        val nbundonetasks=AnalyzeFragmentArgs.fromBundle(requireArguments()).nbundonetasks

        val txtview11=view.findViewById<TextView>(R.id.textView11)
        val txtview22=view.findViewById<TextView>(R.id.textView22)
        val txtview33=view.findViewById<TextView>(R.id.textView33)
        progressbar=view.findViewById<ProgressBar>(R.id.progressBar)
       var percentage= ((nbdonetasks.toFloat() / nbtasks.toFloat()) * 100).toInt()


        txtview11.text = "${txtview11.text}$nbtasks"
        txtview22.text = "${txtview22.text}$nbdonetasks"
        txtview33.text = "${txtview33.text}$nbundonetasks"


val perc=view.findViewById<TextView>(R.id.textView5)
        perc.text="${percentage} %"

animateProgressBar(percentage)


        return view
    }

    private fun animateProgressBar(targetProgress: Int) {
        val animator = ObjectAnimator.ofInt(progressbar, "progress", 0, targetProgress)
        animator.duration = 2000 // Duration of the animation in milliseconds
        animator.start()
    }
    }
