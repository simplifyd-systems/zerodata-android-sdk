package com.simplifydvpn.android.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.simplifydvpn.android.R
import com.simplifydvpn.android.utils.Status
import com.simplifydvpn.android.utils.getColorInt
import com.simplifydvpn.android.utils.showToast
import kotlinx.android.synthetic.main.fragment_login.*

class SignUpFragment : Fragment(R.layout.fragment_login) {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLogin()
        setUpSwipeRefresh()
        showLoading(false)
        btnSubmit.setOnClickListener { performLogin() }
        btnForgotPassword.setOnClickListener { goToForgotPasswordScreen() }
    }

    private fun goToForgotPasswordScreen() {

    }

    private fun showLoading(isLoading: Boolean) {
        swipeRefresh.isEnabled = isLoading
        swipeRefresh.isRefreshing = isLoading
        etEmail.isEnabled = !isLoading
        etPassword.isEnabled = !isLoading
        btnSubmit.isEnabled = !isLoading
    }

    private fun setUpSwipeRefresh() {
        swipeRefresh.setColorSchemeColors(
            getColorInt(R.color.colorPrimary),
            getColorInt(R.color.colorGreen),
            getColorInt(R.color.colorRed),
            getColorInt(R.color.colorBlack)
        )
    }

    private fun goToMainScreen() {
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }

    private fun observeLogin() {
        viewModel.loginStatus.observe(viewLifecycleOwner, Observer{
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


    private fun performLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty()) {
            showToast(getString(R.string.error_enter_email))
            return
        }

        if (!email.isValidEmail()) {
            showToast(getString(R.string.error_enter_valid_email))
            return
        }

        if (password.isEmpty()) {
            showToast(getString(R.string.error_enter_password))
            return
        }

        if (password.length < 5) {
            showToast(getString(R.string.error_short_password))
            return
        }

        viewModel.login(email, password)
    }
}