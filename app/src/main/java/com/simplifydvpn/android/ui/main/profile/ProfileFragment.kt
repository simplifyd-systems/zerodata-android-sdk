package com.simplifydvpn.android.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.simplifydvpn.android.R
import com.simplifydvpn.android.data.model.User
import com.simplifydvpn.android.ui.login.LoginActivity
import com.simplifydvpn.android.ui.main.MainViewModel
import com.simplifydvpn.android.utils.Status
import com.simplifydvpn.android.utils.showToast
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel by viewModels<ProfileViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEditProfile()
        observeSetProtectMe()
        observeSetNotifyMe()
        observeGetUserDetails()
        observeGetNotifyMe()

        btnEditProfile.setOnClickListener { performEditProfile() }

        switchNotify.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setNotifyMe(isChecked)
        }

        btnLogOut.setOnClickListener { performLogOut() }

        etFullName.addTextChangedListener(onTextChanged = buttonStateChangeListener())

        etPhoneNumber.addTextChangedListener(onTextChanged = buttonStateChangeListener())
    }

    private fun performLogOut() {
        viewModel.logOut()

        startActivity(Intent(context, LoginActivity::class.java))
        activity?.finish()
    }

    private fun observeGetNotifyMe() {
        viewModel.getNotifyMeStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Status.Success -> {
                    if (switchNotify.isChecked != it.data) {
                        switchNotify.isChecked = it.data
                    }
                }
            }
        })
    }

    private fun performEditProfile() {
        val fullName = etFullName.text.toString().trim()
        val phoneNumber = etPhoneNumber.text.toString().trim()

        if (fullName.isEmpty()) {
            showToast(getString(R.string.error_empty_name))
            return
        }

        viewModel.updateUserDetails(fullName, phoneNumber)
    }

    private fun observeGetUserDetails() {
        viewModel.getUserDetailsStatus.observe(viewLifecycleOwner, Observer {
            viewModel.user = it
            updateUserDetails(it)
        })
    }

    private fun observeSetNotifyMe() {
        viewModel.setNotifyMeStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Status.Loading -> showLoading()
                is Status.Error -> {
                    hideLoading()
                    showToast(it.error.localizedMessage)
                }
            }
        })
    }

    private fun observeSetProtectMe() {
        mainViewModel.setProtectMeStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Status.Loading -> showLoading()
                is Status.Error -> {
                    hideLoading()
                    showToast(it.error.localizedMessage)
                }
            }
        })
    }

    private fun observeEditProfile() {
        viewModel.editProfileStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Status.Success -> showToast(getString(R.string.profile_updated))
                is Status.Loading -> showLoading()
                is Status.Error -> {
                    hideLoading()
                    showToast(it.error.localizedMessage)
                }
            }
        })
    }

    private fun hideLoading() {

    }

    private fun showLoading() {

    }

    private fun updateUserDetails(user: User) {
        etFullName.setText(user.customer)
        etEmail.setText(user.email)
        etPhoneNumber.setText(user.mobile)
    }

    private fun buttonStateChangeListener(): (CharSequence?, Int, Int, Int) -> Unit {
        return { _: CharSequence?, _: Int, _: Int, _: Int ->
            val enable = viewModel.user?.customer != etFullName.text.toString().trim()
                    || viewModel.user?.mobile != etPhoneNumber.text.toString().trim()

            btnEditProfile.isEnabled = enable
        }
    }
}