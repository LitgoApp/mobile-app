package com.litgo.ui.login

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.litgotesting.viewModel.LitgoUiState
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.litgo.MainActivity
import com.litgo.databinding.FragmentLoginBinding
import com.litgo.R
import com.litgo.data.models.Login
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
//        mainActivity.setBackgroundColor(R.color.white)
        val mainActivityLayout = activity?.findViewById<ConstraintLayout>(R.id.main_activity_layout)
        mainActivityLayout?.setBackgroundColor(resources.getColor(R.color.white))
        // Ensure the bottom navigation bar and top app bar are not visible
//        mainActivity.hideAppAndNavBars()
        val appBarLayout = activity?.findViewById<AppBarLayout>(R.id.app_bar_layout)
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_bottom)
        appBarLayout?.visibility = View.GONE
        navBar?.visibility = View.GONE

        val emailEditText = binding.emailEdittext
        val passwordEditText = binding.passwordEdittext
        val loginButton = binding.signInButton
        val createAccountButton = binding.createAccountButton

        loginButton.setOnClickListener {
            var login = Login(emailEditText.text.toString(), passwordEditText.text.toString())
            try {
                viewModel.loginUser(login)
            } catch (e: HttpException) {
                showLoginFailure()
            }
        }

        createAccountButton.setOnClickListener {
            showCreateAccount()
        }

        lifecycleScope.launch {
            viewModel.observeState().collect {
                renderState(it)
            }
        }
    }

    private fun renderState(it: LitgoUiState) {
        // User has successfully logged in, show user profile
        if (!it.userUiState.name.isNullOrEmpty()) {
            showLoginSuccess(it)
        }
    }

    private fun showLoginFailure() {
        val loginErrorTextView = binding.loginErrorTextview
        loginErrorTextView.visibility = View.VISIBLE
    }

    private fun showLoginSuccess(it: LitgoUiState) {
        val welcome = getString(R.string.welcome) + it.userUiState.name
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_LoginFragment_to_UserProfileFragment)
        // Show the app bar and navbar on successful login
//        val appBarLayout = activity?.findViewById<AppBarLayout>(R.id.app_bar_layout)
//        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_bottom)
//        appBarLayout?.visibility = View.VISIBLE
//        navBar?.visibility = View.VISIBLE
    }

    private fun showCreateAccount() {
        findNavController().navigate(R.id.action_LoginFragment_to_CreateAccountFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
