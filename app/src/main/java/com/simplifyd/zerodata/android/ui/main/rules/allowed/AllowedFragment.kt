package com.simplifyd.zerodata.android.ui.main.rules.allowed

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.data.model.Rule
import com.simplifyd.zerodata.android.data.model.RuleAction
import com.simplifyd.zerodata.android.ui.main.rules.DomainsAdapter
import com.simplifyd.zerodata.android.ui.main.rules.RulesViewModel
import com.simplifyd.zerodata.android.utils.*
import kotlinx.android.synthetic.main.fragment_allowed.*
import java.util.*

@ExperimentalStdlibApi
class AllowedFragment : Fragment(R.layout.fragment_allowed), (Rule) -> Unit {

    private val domainsAdapter by lazy {
        DomainsAdapter(this, R.drawable.ic_no_allowed_domains, R.string.no_allowed_domains)
    }

    private val viewModel by activityViewModels<RulesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvDomains.apply {
            adapter = domainsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        observeLoading()
        observeGetAllowedDomains()

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

    private fun observeGetAllowedDomains() {
        viewModel.getDomainsStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Status.Success -> {
                    showListLoading(false)
                    hideLoadingDialog()
                    domainsAdapter.domains =
                        it.data.filter { rule -> rule.action == RuleAction.ALLOW }
                }
                is Status.Loading -> showListLoading(true)
                is Status.Error -> showListLoading(false)
            }
        })
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

        viewModel.addDomain(domainName.toLowerCase(Locale.getDefault()), "allow")

        etDomain.text?.clear()
        etDomain.clearFocus()
    }

    private fun showListLoading(isLoading: Boolean) {
        progressBar.isVisible = isLoading
        rvDomains.isVisible = !isLoading
    }

    // Remove domain
    override fun invoke(domain: Rule) {
        viewModel.removeDomain(domain)
    }
}