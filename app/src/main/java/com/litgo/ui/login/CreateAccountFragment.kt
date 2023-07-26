package com.litgo.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.litgo.R
import com.litgo.data.models.UserRegistration
import com.litgo.databinding.FragmentCreateAccountBinding
import com.litgo.viewModel.LitterSiteViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import kotlin.system.exitProcess

class CreateAccountFragment : Fragment() {
    private val viewModel: LitterSiteViewModel by activityViewModels()
    private var _binding: FragmentCreateAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivityLayout = activity?.findViewById<ConstraintLayout>(R.id.main_activity_layout)
        mainActivityLayout?.setBackgroundColor(resources.getColor(R.color.green))

        // Ensure the bottom navigation bar and top app bar are not visible
        val appBarLayout = activity?.findViewById<AppBarLayout>(R.id.app_bar_layout)
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_bottom)
        appBarLayout?.visibility = View.GONE
        navBar?.visibility = View.GONE

        val fullNameEditText = binding.fullNameEdittext
        val addressEditText = binding.addressEdittext
        val emailEditText = binding.emailEdittext
        val passwordEditText = binding.passwordEdittext
        val createAccountButton = binding.createAccountButton
        val loginButton = binding.signInButton

        createAccountButton.setOnClickListener {
            val register = UserRegistration(
                emailEditText.text.toString(),
                passwordEditText.text.toString(),
                fullNameEditText.text.toString(),
                addressEditText.text.toString()
            )
            if (verifySubmit(register)) {
                // Attempt to create a new user
                try {
                    viewModel.registerUser(register)
                    showCreateSuccess()
                } catch (e : HttpException) {
                    // TODO: Handle exception
                    showCreateFailure()
                }
            } else {
                showCreateFailure()
            }
        }

        loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_CreateAccountFragment_to_LoginFragment)
        }
    }

    private fun verifySubmit(register: UserRegistration): Boolean {
        var result = true
        if (register.email.isNullOrEmpty()) {
            binding.emailEdittext.error = "Email" + R.string.create_account_error_prompt
            result = false
        }
        if (register.name.isNullOrEmpty()) {
            binding.fullNameEdittext.error = "Full name" + R.string.create_account_error_prompt
            result = false
        }
        if (register.password.isNullOrEmpty()) {
            binding.passwordEdittext.error = "Password" + R.string.create_account_error_prompt
            result = false
        }
        if (register.address.isNullOrEmpty()) {
            binding.addressEdittext.error = "Address" + R.string.create_account_error_prompt
            result = false
        }
        return result
    }

    private fun showCreateSuccess() {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, R.string.create_account_popup_text, Toast.LENGTH_LONG).show()
        // Switch to login screen
        findNavController().navigate(R.id.action_CreateAccountFragment_to_LoginFragment)
    }

    private fun showCreateFailure() {
        binding.createAccountErrorTextview.visibility = View.VISIBLE
    }

}