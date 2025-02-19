package com.shabelnikd.bilimtrack.ui.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.shabelnikd.bilimtrack.databinding.FragmentAuthBinding
import com.shabelnikd.bilimtrack.ui.MainActivity
import com.shabelnikd.bilimtrack.utils.PreferenceHelper
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class AuthFragment : Fragment() {
    private val viewModel: AuthViewModel by viewModels()

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private var isUsernameFilled: Boolean = false
    private var isPasswordFilled: Boolean = false

    private val preferenceHelper: PreferenceHelper by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etUsernameStateChange = fun(state: Boolean) { isUsernameFilled = state }
        val etPasswordStateChange = fun(state: Boolean) { isPasswordFilled = state }

        binding.etUsername.editText?.doOnTextChanged { inputText, _, _, _ ->
            etUsernameStateChange(!inputText.isNullOrEmpty())
            if (isUsernameFilled && isPasswordFilled) {
                binding.logInButton.visibility = View.VISIBLE
            } else {
                binding.logInButton.visibility = View.GONE
            }
        }
        binding.etPassword.editText?.doOnTextChanged { inputText, _, _, _ ->
            etPasswordStateChange(!inputText.isNullOrEmpty())
            if (isUsernameFilled && isPasswordFilled) {
                binding.logInButton.visibility = View.VISIBLE
            } else {
                binding.logInButton.visibility = View.GONE
            }
        }

        binding.logInButton.setOnClickListener {
            viewModel.userLogIn(
                binding.etUsername.editText?.text.toString(),
                binding.etPassword.editText?.text.toString()
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginResult.collect { result ->
                    when (result) {
                        is AuthViewModel.LoginResult.Success -> {
                            val accessToken = result.accessToken
                            val refreshToken = result.refreshToken

                            preferenceHelper.accessToken = accessToken
                            preferenceHelper.refreshToken = refreshToken

                            activity?.finish()
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                        }

                        is AuthViewModel.LoginResult.Error -> {
                            val errorMessage = result.errorMessage
                            Snackbar.make(binding.root, errorMessage, 2000).show()
                        }
                    }
                }
            }
        }


    }
}