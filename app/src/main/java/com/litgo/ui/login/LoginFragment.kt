package com.litgo.ui.login

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.litgo.MainActivity
import com.litgo.databinding.FragmentLoginBinding

import com.litgo.R
import com.litgo.data.models.Login
import com.litgo.viewModel.LitterSiteViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private val viewModel: LitterSiteViewModel by activityViewModels()
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
        val mainActivityLayout = activity?.findViewById<ConstraintLayout>(R.id.main_activity_layout)
        mainActivityLayout?.setBackgroundColor(resources.getColor(R.color.white))
        // Ensure the bottom navigation bar and top app bar are not visible
        val appBarLayout = activity?.findViewById<AppBarLayout>(R.id.app_bar_layout)
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_bottom)
        appBarLayout?.visibility = View.GONE
        navBar?.visibility = View.GONE

        lifecycleScope.launch {
            viewModel.observeState().collect {
                renderState(it)
            }
        }

        val emailEditText = binding.emailEdittext
        val passwordEditText = binding.passwordEdittext
        val loginButton = binding.signInButton
        val createAccountButton = binding.createAccountButton

        loginButton.setOnClickListener {
            var login = Login(emailEditText.text.toString(), passwordEditText.text.toString())
            // TODO: temporary login credentials for testing. Should change to the below eventually
            var result = viewModel.loginUser(login)
            if (emailEditText.text.toString() == "hi") showLoginSuccess() else showLoginFailed()
            // Upon successful login, the user state changes to indicate successful login
            // UI will be updated
            if (result) showLoginSuccess() else showLoginFailed()
        }

        createAccountButton.setOnClickListener {
            showCreateAccount()
        }

    }

    private fun showLoginFailed() {
        val loginErrorTextView = binding.loginErrorTextview
        loginErrorTextView.visibility = View.VISIBLE
    }

    private fun showLoginSuccess() {
        val welcome = getString(R.string.welcome)
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_LoginFragment_to_UserProfileFragment)
        // Show the app bar and navbar on successful login
        val appBarLayout = activity?.findViewById<AppBarLayout>(R.id.app_bar_layout)
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_bottom)
        appBarLayout?.visibility = View.VISIBLE
        navBar?.visibility = View.VISIBLE
    }

    private fun showCreateAccount() {
        findNavController().navigate(R.id.action_LoginFragment_to_CreateAccountFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//class LoginFragment : Fragment() {
//
//    private lateinit var loginViewModel: LoginViewModel
//    private var _binding: FragmentLoginBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        _binding = FragmentLoginBinding.inflate(inflater, container, false)
//        return binding.root
//
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
//            .get(LoginViewModel::class.java)
//
//        val usernameEditText = binding.usernameEdittext
//        val passwordEditText = binding.passwordEdittext
//        val loginButton = binding.signInButton
//
//        loginViewModel.loginFormState.observe(viewLifecycleOwner,
//            Observer { loginFormState ->
//                if (loginFormState == null) {
//                    return@Observer
//                }
//                loginButton.isEnabled = loginFormState.isDataValid
//                loginFormState.usernameError?.let {
//                    usernameEditText.error = getString(it)
//                }
//                loginFormState.passwordError?.let {
//                    passwordEditText.error = getString(it)
//                }
//            })
//
//        loginViewModel.loginResult.observe(viewLifecycleOwner,
//            Observer { loginResult ->
//                loginResult ?: return@Observer
//                loginResult.error?.let {
//                    showLoginFailed(it)
//                }
//                loginResult.success?.let {
//                    updateUiWithUser(it)
//                }
//            })
//
//        val afterTextChangedListener = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//                // ignore
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                // ignore
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                loginViewModel.loginDataChanged(
//                    usernameEditText.text.toString(),
//                    passwordEditText.text.toString()
//                )
//            }
//        }
//        usernameEditText.addTextChangedListener(afterTextChangedListener)
//        passwordEditText.addTextChangedListener(afterTextChangedListener)
//        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                loginViewModel.login(
//                    usernameEditText.text.toString(),
//                    passwordEditText.text.toString()
//                )
//            }
//            false
//        }
//    }
//
//    private fun updateUiWithUser(model: LoggedInUserView) {
//        val welcome = getString(R.string.welcome) + model.displayName
//        // TODO : initiate successful logged in experience
//        val appContext = context?.applicationContext ?: return
//        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
//        findNavController().navigate(R.id.action_LoginFragment_to_UserProfileFragment)
//    }
//
//    private fun showLoginFailed(@StringRes errorString: Int) {
//        val appContext = context?.applicationContext ?: return
//        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}