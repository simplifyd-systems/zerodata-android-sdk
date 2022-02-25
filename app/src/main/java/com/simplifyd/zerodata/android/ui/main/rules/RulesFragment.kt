package com.simplifyd.zerodata.android.ui.main.rules

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.ui.main.rules.allowed.AllowedFragment
import com.simplifyd.zerodata.android.ui.main.rules.blocked.BlockedFragment
import com.simplifyd.zerodata.android.ui.main.rules.categories.CategoriesFragment
import com.simplifyd.zerodata.android.utils.Status
import com.simplifyd.zerodata.android.utils.showRetrySnackBar
import kotlinx.android.synthetic.main.fragment_rules.*

@ExperimentalStdlibApi
class RulesFragment : Fragment(R.layout.fragment_rules) {

    private val viewModel by activityViewModels<RulesViewModel>()

    private val fragments by lazy {
        listOf<Fragment>(BlockedFragment(), AllowedFragment(), CategoriesFragment())
    }

    private val titles by lazy {
        listOf(
            getString(R.string.blocked),
            getString(R.string.allowed),
            getString(R.string.categories)
        )
    }

    private val rulesTabAdapter by lazy {
        TabsAdapter(childFragmentManager, Pair(fragments, titles))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTabs()
        observeGetCustomerRules()
    }

    private fun observeGetCustomerRules() {
        viewModel.getCustomerRulesStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Status.Error -> {
                    showRetrySnackBar(it.error.localizedMessage) {
                        viewModel.getCustomerRules()
                        viewModel.getAllCategories()
                    }
                }
                else -> {
                }
            }
        })
    }

    private fun setUpTabs() {
        viewPager.adapter = rulesTabAdapter
        tabLayout.setupWithViewPager(viewPager)
    }
}