package com.simplifydvpn.android.ui.main.protection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.simplifydvpn.android.R
import com.simplifydvpn.android.data.model.Rule
import com.simplifydvpn.android.ui.main.overview.adapter.TableAdapter
import com.simplifydvpn.android.ui.main.overview.adapter.TableItem
import com.simplifydvpn.android.ui.main.rules.DomainsAdapter
import com.simplifydvpn.android.utils.Status
import com.simplifydvpn.android.utils.showRetrySnackBar
import kotlinx.android.synthetic.main.fragment_protection.*
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.HashMap


@ExperimentalStdlibApi
class ProtectionFragment : Fragment(R.layout.fragment_protection) {

    private val viewModel by viewModels<ProtectionViewModel>()

    private val domainsAdapter by lazy {
        ProtectionAdapter( R.drawable.ic_no_blocked_domains, R.string.no_data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeGetDashboardData()
    }


    private fun observeGetDashboardData() {
        viewModel.getDashboardDataStatus.observe(viewLifecycleOwner, {
            when (it) {
                is Status.Loading -> {
                    progressBar.isVisible = true
                }
                is Status.Error -> {
                    progressBar.isVisible = false
                    showRetrySnackBar(it.error.localizedMessage) { viewModel.getDashboardData() }
                }
                is Status.Success -> {
                    progressBar.isVisible = false
                    protection_count.text = "${it.data.topDomains?.values?.sum() ?: 0}"
                    drawTable(it.data.topDomains)
                }
            }
        })
    }

    private fun drawTable(topDomains: HashMap<String, Int>?) {
        top_domains_parent.isVisible = true
        topDomainsRv.apply {
            adapter = domainsAdapter
        }
        domainsAdapter.domains = topDomains?.map {
            it.key
        }?.toList() ?: emptyList()
    }

}