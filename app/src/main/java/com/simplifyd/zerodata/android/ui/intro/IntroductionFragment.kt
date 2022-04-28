package com.simplifyd.zerodata.android.ui.intro

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.simplifyd.zerodata.android.R
import kotlinx.android.synthetic.main.fragment_introduction.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class IntroductionFragment : Fragment(R.layout.fragment_introduction) {

    private val introAdapter by lazy {
        IntroAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        intro_list.adapter = introAdapter
        dots_indicator.setViewPager2(intro_list)
        introAdapter.submitList(intros)



        intro_list.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                when(position){

                    0 ->{

                        skip.visibility = View.GONE
                        btnPrev.visibility = View.GONE
                        btnNext.visibility = View.GONE
                        btnNext1.visibility = View.VISIBLE


                    }
                    1 ->{
                        btnNext1.visibility = View.GONE
                        skip.visibility = View.VISIBLE
                        btnPrev.visibility = View.VISIBLE
                        btnNext.visibility = View.VISIBLE
                        btnNext.text = "Next"



                    }
                    2 ->{

                        btnNext1.visibility = View.GONE
                        skip.visibility = View.VISIBLE
                        btnPrev.visibility = View.VISIBLE
                        btnNext.visibility = View.VISIBLE
                        btnNext.text = "Next"
                    }
                    3 ->{

                        btnNext.text = "Get started"
                    }
                }

            }
        })

        skip.setOnClickListener {

            findNavController().navigate(R.id.action_navigation_intro_screens_to_navigation_sign_in)
        }

        btnNext1.setOnClickListener {
            intro_list.setCurrentItem(1, true)

        }

        btnPrev.setOnClickListener {
            val currentPosition = intro_list.currentItem
            if (currentPosition in 1..3){
                intro_list.setCurrentItem(currentPosition-1, true)
            }
        }




        btnNext.setOnClickListener {
            val currentPosition = intro_list.currentItem
            if (currentPosition in 1..2){
                intro_list.setCurrentItem(currentPosition+1, true)
            }else{
                findNavController().navigate(R.id.action_navigation_intro_screens_to_navigation_sign_in)

            }
        }
    }

    companion object {
        val intros = listOf(
            Intro(1, R.string.intro_one_title, R.string.intro_one_desc, R.drawable.ic_intro_one),
            Intro(2, R.string.intro_two_title, R.string.intro_two_desc, R.drawable.ic_intro_two),
            Intro(3, R.string.intro_three_title, R.string.intro_three_desc, R.drawable.ic_intro_three),
            Intro(4, R.string.intro_four_title, R.string.intro_four_desc, R.drawable.ic_intro_four)

        )
    }
}