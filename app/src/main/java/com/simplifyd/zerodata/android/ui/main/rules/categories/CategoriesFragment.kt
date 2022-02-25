package com.simplifyd.zerodata.android.ui.main.rules.categories

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.data.model.Rule
import com.simplifyd.zerodata.android.ui.main.rules.RulesViewModel
import com.simplifyd.zerodata.android.utils.Status
import com.simplifyd.zerodata.android.utils.hideLoadingDialog
import com.simplifyd.zerodata.android.utils.showLoadingDialog
import com.simplifyd.zerodata.android.utils.showToast
import kotlinx.android.synthetic.main.fragment_categories.*

class CategoriesFragment : Fragment(R.layout.fragment_categories), (Rule) -> Unit {

    private val categoriesAdapter by lazy { CategoriesAdapter(this) }

    private val viewModel by activityViewModels<RulesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvCategories.adapter = categoriesAdapter

        cbShowOnlyBlocked.setOnCheckedChangeListener { _, isChecked ->
            viewModel.filterCategories(isChecked, etSearch.text.toString())
        }

        etSearch.addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                viewModel.filterCategories(cbShowOnlyBlocked.isChecked, text.toString())
            }
        )

        observeGetCategories()
        observeLoading()
    }

    private fun observeLoading() {
        viewModel.loadingStatus.observe(viewLifecycleOwner, Observer{
            when (it) {
                is Status.Loading -> showLoadingDialog()
                else -> {
                }
            }
        })
    }

    private fun observeGetCategories() {
        viewModel.getCategoriesStatus.observe(viewLifecycleOwner, Observer{
            when (it) {
                is Status.Loading -> showListLoading(true)
                is Status.Success -> {
                    showListLoading(false)
                    hideLoadingDialog()
                    categoriesAdapter.categories = it.data
                }
                is Status.Error -> {
                    showListLoading(false)
                    hideLoadingDialog()
                    showToast(it.error.localizedMessage)
                }
            }
        })
    }

    private fun showListLoading(isLoading: Boolean) {
        progressBar.isVisible = isLoading
        rvCategories.isVisible = !isLoading
    }

    override fun invoke(category: Rule) {
        viewModel.updateCategoryStatus(category, cbShowOnlyBlocked.isChecked)
    }
}