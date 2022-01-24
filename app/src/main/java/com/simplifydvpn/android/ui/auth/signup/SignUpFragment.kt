package com.simplifydvpn.android.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.simplifydvpn.android.R
import com.simplifydvpn.android.utils.Status
import com.simplifydvpn.android.utils.getColorInt
import com.simplifydvpn.android.utils.showToast
import kotlinx.android.synthetic.main.fragment_signup.*
import com.simplifydvpn.android.ui.main.MainActivity
import com.simplifydvpn.android.utils.isValidEmail

class SignUpFragment : Fragment(R.layout.fragment_signup) {

    private val viewModel by viewModels<SignUpViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeSignUp()
        setUpSwipeRefresh()
        showLoading(false)
        btnSubmit.setOnClickListener {
            performSignUp()
        }
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

    private fun goToLoginScreen() {
        findNavController().navigate(R.id.navigation_sign_in)
    }

    private fun observeSignUp() {
        viewModel.loginStatus.observe(viewLifecycleOwner, Observer{
            when (it) {
                is Status.Success -> {
                    showLoading(false)
                    goToLoginScreen()
                }
                is Status.Loading -> showLoading(true)
                is Status.Error -> {
                    showLoading(false)
                    showToast(it.error.localizedMessage)
                }
            }
        })
    }


    private fun performSignUp() {
        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val phoneNumber = etPhoneNumber.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val passwordConfirm = etConfirmPassword.text.toString().trim()

        if (firstName.isEmpty()) {
            showToast(getString(R.string.error_empty_name))
            return
        }

        if (lastName.isEmpty()) {
            showToast(getString(R.string.error_empty_last_name))
            return
        }

        if (email.isEmpty() || !email.isValidEmail()) {
            showToast(getString(R.string.error_enter_valid_email))
            return
        }

        if (password.isEmpty() || passwordConfirm.isEmpty()) {
            showToast(getString(R.string.error_enter_password))
            return
        }

        if (password.length < 5 || passwordConfirm.length < 5) {
            showToast(getString(R.string.error_short_password))
            return
        }

        if (password!= passwordConfirm) {
            showToast(getString(R.string.password_dont_match))
            return
        }

        viewModel.signUp(firstName, lastName, email, phoneNumber, password)
    }
}