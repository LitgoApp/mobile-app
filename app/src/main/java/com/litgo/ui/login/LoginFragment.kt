package com.litgo.ui.login

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.litgotesting.viewModel.LitgoUiState
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.litgo.MainActivity
import com.litgo.databinding.FragmentLoginBinding
import com.litgo.R
import com.litgo.data.models.Login
import com.litgo.ui.user.UserProfileFragment
import com.litgo.viewModel.LitgoViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginFragment : Fragment() {
    private val viewModel: LitgoViewModel by activityViewModels()
    private var _binding: FragmentLoginBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailEditText = binding.emailEdittext
        val passwordEditText = binding.passwordEdittext
        val loginButton = binding.signInButton.apply {
            setOnClickListener {
                var login = Login(emailEditText.text.toString(), passwordEditText.text.toString())
                try {
                    viewModel.loginUser(login)
                    showLoginSuccess(viewModel.uiState.value)
                } catch (e: HttpException) {
                    showLoginFailure()
                }
            }
        }
        val createAccountButton = binding.createAccountButton.apply {
            setOnClickListener { showCreateAccount() }
        }
    }

    private fun showLoginFailure() {
        val loginErrorTextView = binding.loginErrorTextview.apply {
            visibility = View.VISIBLE
        }
    }

    private fun showLoginSuccess(it: LitgoUiState) {
        val welcome = getString(R.string.welcome) + it.userUiState.name
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
        activity?.supportFragmentManager?.commit {
            replace(R.id.nav_host_fragment_content_main, UserProfileFragment())
            activity?.findViewById<TextView>(R.id.app_bar_title_textview)?.text = "My Profile"
            addToBackStack("My Profile")
        }
    }

    private fun showCreateAccount() {
        findNavController().navigate(R.id.action_LoginFragment_to_CreateAccountFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
