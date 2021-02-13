package com.simplifydvpn.android.ui.main.activity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.simplifydvpn.android.R
import com.simplifydvpn.android.ui.main.activity.agenda.AgendasFragment
import com.simplifydvpn.android.ui.main.activity.events.AllEventsFragment
import com.simplifydvpn.android.ui.main.rules.TabsAdapter
import kotlinx.android.synthetic.main.fragment_activities.*

class ActivitiesFragment : Fragment(R.layout.fragment_activities) {

    private val fragments by lazy {
        listOf(AgendasFragment(), AllEventsFragment())
    }

    private val titles by lazy {
        listOf(getString(R.string.agenda), getString(R.string.all_events))
    }

    private val activitiesTabAdapter by lazy {
        TabsAdapter(childFragmentManager, Pair(fragments, titles))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTabs()
    }

    private fun setUpTabs() {
        viewPager.adapter = activitiesTabAdapter
        tabLayout.setupWithViewPager(viewPager)
    }
}