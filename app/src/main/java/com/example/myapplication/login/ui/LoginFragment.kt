package com.example.myapplication.login.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.app.ui.util.Navigator
import com.example.myapplication.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.performLoginButton.setOnClickListener {
            val username = binding.enterUsernameEditText.text?.toString().orEmpty()
            val password = binding.enterPasswordEditText.text?.toString().orEmpty()
            // TODO: hook in real auth
            if (username.isNotBlank() && password.isNotBlank()) {
                Navigator.Companion.from(this).goToImageSearch()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}