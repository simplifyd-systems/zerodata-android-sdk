package com.simplifyd.zerodata.android.ui.auth.getStarted

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.simplifyd.zerodata.android.R
import com.simplifyd.zerodata.android.utils.*
import kotlinx.android.synthetic.main.fragment_get_started.*
import org.apache.commons.lang3.StringUtils


class GetStartedFragment : Fragment(R.layout.fragment_get_started) {

    private val viewModel by viewModels<SharedAuthViewModel>()
    lateinit var phoneNumber: String
    private val args: GetStartedFragmentArgs by navArgs()

    private lateinit var countrySelection: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSwipeRefresh()
        observeInitiateLogin()
        showLoading(false)

        countrySelection = args.countryCode

        if (countrySelection!= "234"){
            country_code_tv.text  = "+${countrySelection}"
            flagImage.setImageResource(R.drawable.kenya_flag)
        }else{
            country_code_tv.text  = "+${countrySelection}"
            flagImage.setImageResource(R.drawable.nigerian_flag)
        }


        countryCodeLayout.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_sign_in_to_countryCodeDialog)
        }

        btnSubmit.setOnClickListener {
           initiateLogin()
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
        etPhoneNumber.isEnabled = !isLoading
        btnSubmit.isEnabled = !isLoading
    }

    private fun initiateLogin() {
        phoneNumber = etPhoneNumber.text.toString().trim()

        if (phoneNumber.isEmpty()) {
            showToast(getString(R.string.error_enter_phone_number))
            return
        } else {
            if (countrySelection == "234"){
                if (phoneNumber.startsWith("0")) {
                    phoneNumber = StringUtils.replaceOnce(
                        StringUtils.strip(
                            StringUtils.strip(
                                StringUtils.strip(
                                    StringUtils.deleteWhitespace(
                                        phoneNumber
                                    )
                                ), "-"
                            ), "+"
                        ),
                        "0", "234"
                    )
                }
                else if(!phoneNumber.startsWith("2")){
                    phoneNumber="234${phoneNumber}"
                }
            }else{
                if (phoneNumber.startsWith("0")) {
                    phoneNumber = StringUtils.replaceOnce(
                        StringUtils.strip(
                            StringUtils.strip(
                                StringUtils.strip(
                                    StringUtils.deleteWhitespace(
                                        phoneNumber
                                    )
                                ), "-"
                            ), "+"
                        ),
                        "0", "254"
                    )
                }else if(!phoneNumber.startsWith("2")){
                    phoneNumber="254${phoneNumber}"
                }
            }

        }

        if (!(phoneNumber.isValidPhoneNumber()||phoneNumber.isValidKenyanNumber())) {
            showToast(getString(R.string.error_enter_valid_phone_number))
            return
        }

        if(NetworkUtils.isOnline(requireContext())){
            viewModel.initiateLogin(phoneNumber)
        }else{
            hideKeyboard(requireActivity())
            Handler().postDelayed({
                showToast(getString(R.string.error_network_connectivity)) }, 200)

        }

    }

    private fun gotoVerifyScreen() {
        val bundle = Bundle()

        bundle.putString("phoneNumber", phoneNumber)

        findNavController().navigate(R.id.action_navigation_sign_in_to_navigation_verification, bundle)
    }

    private fun observeInitiateLogin() {
        viewModel.initiateStatus.observe(viewLifecycleOwner) {
            when (it) {
                is Status.Success -> {
                    showLoading(false)
                    gotoVerifyScreen()
                }
                is Status.Loading -> showLoading(true)
                is Status.Error -> {
                    showLoading(false)
                    showToast(it.error.localizedMessage)
                }
            }
        }
    }




}