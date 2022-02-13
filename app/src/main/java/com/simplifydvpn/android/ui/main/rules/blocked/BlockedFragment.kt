package com.simplifydvpn.android.ui.main.rules.blocked

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.simplifydvpn.android.R
import com.simplifydvpn.android.data.model.Rule
import com.simplifydvpn.android.data.model.RuleAction
import com.simplifydvpn.android.ui.main.rules.DomainsAdapter
import com.simplifydvpn.android.ui.main.rules.RulesViewModel
import com.simplifydvpn.android.utils.*
import kotlinx.android.synthetic.main.fragment_blocked.*
import java.util.*

@ExperimentalStdlibApi
class BlockedFragment : Fragment(R.layout.fragment_blocked), (Rule) -> Unit {

    private val domainsAdapter by lazy {
        DomainsAdapter(this, R.drawable.ic_no_blocked_domains, R.string.no_blocked_domains)
    }

    private val viewModel by activityViewModels<RulesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvDomains.apply {
            adapter = domainsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        observeGetBlockedDomains()
        observeLoading()

        btnAddDomain.setOnClickListener { performAddDomain() }
    }

    private fun observeLoading() {
        viewModel.loadingStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Status.Loading -> showLoadingDialog()
                else -> {
                }
            }
        })
    }

    private fun observeGetBlockedDomains() {
        viewModel.getDomainsStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Status.Success -> {
                    showListLoading(false)
                    hideLoadingDialog()
                    domainsAdapter.domains =
                        it.data.filter { rule -> rule.action == RuleAction.BLOCK }
                }
                is Status.Loading -> showListLoading(true)
                is Status.Error -> showListLoading(false)
            }
        })
    }

    private fun showListLoading(isLoading: Boolean) {
        progressBar.isVisible = isLoading
        rvDomains.isVisible = !isLoading
    }

    private fun performAddDomain() {
        val domainName = etDomain.text.toString().trim()

        if (domainName.isEmpty()) {
            showToast(getString(R.string.error_enter_domain))
            return
        }

        if (!domainName.isValidDomain()) {
            showToast(getString(R.string.error_valid_domain))
            return
        }

        viewModel.addDomain(domainName.toLowerCase(Locale.getDefault()), "block")

        etDomain.text?.clear()
        etDomain.clearFocus()
    }

    // Remove domain
    override fun invoke(domain: Rule) {
        viewModel.removeDomain(domain)
    }
}