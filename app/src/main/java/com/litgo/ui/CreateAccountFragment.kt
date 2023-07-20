package com.litgo.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.litgo.R
import com.litgo.data.models.User
import com.litgo.databinding.FragmentCreateAccountBinding

class CreateAccountFragment : Fragment() {

    companion object {
        fun newInstance() = CreateAccountFragment()
    }

    private val userViewModel: UserViewModel by viewModels()
    private var _binding: FragmentCreateAccountBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    //        return inflater.inflate(R.layout.fragment_create_account, container, false)
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
        val emailEditText = binding.emailEdittext
        val passwordEditText = binding.passwordEdittext
        val createAccountButton = binding.createAccountButton
        val loginButton = binding.signInButton

        createAccountButton.setOnClickListener {
            var accountDetails = User("", emailEditText.text.toString(), fullNameEditText.text.toString(), 0, "")
            // TODO: call the userViewModel to create the new user. Ensure all fields are correctly filled
            showSuccess()
        }

        loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_CreateAccountFragment_to_LoginFragment)
        }
    }

    private fun showSuccess() {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, R.string.create_account_popup_text, Toast.LENGTH_LONG).show()
        // Switch to login screen
        findNavController().navigate(R.id.action_CreateAccountFragment_to_LoginFragment)
        val mainActivityLayout = activity?.findViewById<ConstraintLayout>(R.id.main_activity_layout)
    }
}