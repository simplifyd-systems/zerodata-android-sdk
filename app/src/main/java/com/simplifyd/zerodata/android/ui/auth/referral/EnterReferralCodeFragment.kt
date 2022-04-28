package com.simplifyd.zerodata.android.ui.auth.referral

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.ui.auth.LoginActivity
import com.simplifyd.zerodata.android.ui.main.MainActivity
import com.simplifyd.zerodata.android.utils.*
import kotlinx.android.synthetic.main.fragment_enter_referral_code.*


class EnterReferralCodeFragment : Fragment(R.layout.fragment_enter_referral_code) {

    private val viewModel by viewModels<ReferralViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSwipeRefresh()

        showLoading(false)

        observeLogReferralCode()

        btnSubmit.setOnClickListener {

            validateReferralCode()
        }

        noReferralCode.setOnClickListener {
            goToMainScreen()
        }


    }


    private fun setUpSwipeRefresh() {
        swipeRefresh.setColorSchemeColors(
            getColorInt(R.color.colorPrimary),
            getColorInt(R.color.colorGreen),
            getColorInt(R.color.colorRed),
            getColorInt(R.color.colorBlack)
        )
    }


    private fun showLoading(isLoading: Boolean) {
        swipeRefresh.isEnabled = isLoading
        swipeRefresh.isRefreshing = isLoading
        etCode.isEnabled = !isLoading
        btnSubmit.isEnabled = !isLoading
    }

    private fun validateReferralCode() {

        val code = etCode.text.toString().trim()

        if (code.isEmpty()) {
            showToast(getString(R.string.error_enter_code))
            return
        }

        if (!code.isValidCode()) {
            showToast(getString(R.string.error_enter_valid_code))
            return
        }

        if (NetworkUtils.isOnline(requireContext())) {
            viewModel.validateReferral(
                code
            )
        } else {
            showToast(getString(R.string.error_network_connectivity))
        }

    }

    private fun observeLogReferralCode() {
        viewModel.logReferralCode.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Status.Success -> {
                    showLoading(false)
                    goToMainScreen()
                }
                is Status.Loading -> showLoading(true)
                is Status.Error -> {

                    showLoading(false)

                    showToast(it.error.localizedMessage)

                }
            }
        })
    }

    private fun goToMainScreen() {
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity?.startActivity(intent)
        activity?.finish()
    }


}