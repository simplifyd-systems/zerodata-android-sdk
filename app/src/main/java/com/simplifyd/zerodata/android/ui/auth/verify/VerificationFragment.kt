package com.simplifyd.zerodata.android.ui.auth.verify

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.simplifyd.zerodata.android.BuildConfig
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.utils.*
import kotlinx.android.synthetic.main.fragment_verification.*


class VerificationFragment : Fragment(R.layout.fragment_verification) {
    private val viewModel by viewModels<VerificationViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = this.arguments
        if (bundle != null) {
            phone_number.text = bundle.getString("phoneNumber").toString()
        }



        setUpSwipeRefresh()
        showLoading(false)
        observeValidateLogin()
        btnSubmit.setOnClickListener {

                validateLogin()




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

    fun showValidatedDialog() {
        VerifyDialogSuccess(requireActivity(), 1).showDialog()
    }



    private fun validateLogin() {
        var code = etCode.text.toString().trim()

        if (code.isEmpty()) {
            showToast(getString(R.string.error_enter_code))
            return
        }

        if (!code.isValidCode()) {
            showToast(getString(R.string.error_enter_valid_code))
            return
        }

        if(NetworkUtils.isOnline(requireContext())){
            viewModel.validateLogin(code, BuildConfig.VERSION_CODE.toString(), getString(R.string.platform))
        }else{
            showToast(getString(R.string.error_network_connectivity))
        }
//        showValidatedDialog()


    }

    private fun observeValidateLogin() {
        viewModel.validateStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Status.Success -> {
                    showLoading(false)
                    showValidatedDialog()
                }
                is Status.Loading -> showLoading(true)
                is Status.Error -> {
                    showLoading(false)

                    showToast(it.error.localizedMessage)

                }
            }
        })
    }

}