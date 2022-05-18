package com.simplifyd.zerodata.android.ui.auth.verify

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.simplifyd.zerodata.android.BuildConfig
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.ui.auth.getStarted.SharedAuthViewModel
import com.simplifyd.zerodata.android.utils.*
import com.wynsbin.vciv.VerificationCodeInputView
import kotlinx.android.synthetic.main.fragment_verification.*


class VerificationFragment : Fragment(R.layout.fragment_verification) {
    private val viewModel by viewModels<SharedAuthViewModel>()

    private  var codeValue: String = ""

    private val phoneNumber by lazy { arguments?.getString("phoneNumber").toString() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        phone_number.text = getString(R.string.verification_desc, phoneNumber)


        setUpSwipeRefresh()

        showLoading(false)

        observeValidateLogin()

        countdown_timer.setTimeOutMinutes(10)

        ic_back.setOnClickListener {
            findNavController().popBackStack()
        }


        countdown_timer.countDownListener = object : CountDownTimerView.CountDownTimerListener{
            override fun onCountDownDone() {
                enableResendButton()
            }

            override fun onTimerStarted() {
                TODO("Not yet implemented")
            }

            override fun onCountDownCancelled() {
                TODO("Not yet implemented")
            }

        }

        countdown_timer.runCountDownTimer()

        tap_here.setOnClickListener {
            viewModel.initiateLogin(phoneNumber)
        }

        editNumber.setOnClickListener {
            findNavController().popBackStack()
        }

        etCode.requestFocus()
        etCode.setOnInputListener(object : VerificationCodeInputView.OnInputListener {
            override fun onComplete(code: String) {
                codeValue = code
            }

            override fun onInput() {}
        })

        btnSubmit.setOnClickListener {

            if (codeValue.isNullOrEmpty()){
                showToast(getString(R.string.error_enter_valid_code))
            }else {

                validateLogin(codeValue)
            }

        }
    }

    private fun enableResendButton() {
        countdown_timer.visibility = View.GONE
        tap_here.visibility = View.VISIBLE
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


    private fun validateLogin(code: String) {

        if (code.isEmpty()) {
            showToast(getString(R.string.error_enter_code))
            return
        }

        if (!code.isValidCode()) {
            showToast(getString(R.string.error_enter_valid_code))
            return
        }

        if (NetworkUtils.isOnline(requireContext())) {
            viewModel.validateLogin(
                code,
                BuildConfig.VERSION_CODE.toString(),
                getString(R.string.platform)
            )
        } else {
            showToast(getString(R.string.error_network_connectivity))
        }

    }

    private fun observeValidateLogin() {
        viewModel.validateStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Status.Success -> {
                    showLoading(false)
                    findNavController().navigate(R.id.action_navigation_verification_to_navigation_referral)
                }
                is Status.Loading -> showLoading(true)
                is Status.Error -> {

                    showLoading(false)

                    showToast(it.error.localizedMessage)

                }
            }
        })

        viewModel.initiateStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Status.Success -> {
                    showLoading(false)
                    Toast.makeText(context, "Code successfully sent", Toast.LENGTH_SHORT).show()
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