package com.simplifyd.zerodata.android.ui.main.rules

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class TabsAdapter(
    fragmentManager: FragmentManager,
    private val fragments: Pair<List<Fragment>, List<String>>
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = fragments.first[position]

    override fun getCount(): Int = fragments.first.size

    override fun getPageTitle(position: Int): CharSequence? = fragments.second[position]

}
