package com.litgo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        LoginScreen()

//        setContent {
//            LitgoTheme {
//                Surface(
//                        modifier = Modifier.fillMaxSize(),
//                        color = MaterialTheme.colorScheme.background
//                ) {
//                    LoginScreen(modifier, viewModel)
//                }
//            }
//        }

    }

    data class LoginUiState(
        val isSignedIn: Boolean = false,
        val enteredName: String = "",
        val enteredPassword: String = ""
    )

    class LoginViewModel(
            private val repository: LoginRepository
    ) : ViewModel() {
        var uiState by mutableStateOf(LoginUiState())
            private set

        private var fetchJob: Job? = null

        fun authenticateCredentials(
                username: String,
                password: String
        ) {
            fetchJob?.cancel()
            fetchJob = viewModelScope.launch {
                // TODO: Fetching the username and password entered by the user. Pass to Login repository for actual verification
                val authenticated = repository.authenticateCredentials(username, password)
                uiState = uiState.copy(isSignedIn = authenticated)
            }
        }
    }

    @Composable
    fun LoginScreen(
//        modifier: Modifier = Modifier
        viewModel: LoginViewModel = LoginViewModel(LoginRepository())
    ) {
        var isError by remember { mutableStateof(false) }
        var enteredUsername by rememberSaveable { mutableStateOf("") }
        var enteredPassword by rememberSaveable { mutableStateOf("") }
        var enteredPasswordHidden by rememberSaveable { mutableStateOf(true) }

        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        editTextUsername.addTextChangedListener(
            object: TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) { }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    findViewById<Button>(R.id.btnLogin).isEnabled = enteredPassword && s?.isNotEmpty() ?: false
                }
                override fun afterTextChanged(s: Editable?) { }
            }
        )
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        editTextPassword.addTextChangedListener(
            object: TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) { }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    findViewById<Button>(R.id.btnLogin).isEnabled = enteredUsername && s?.isNotEmpty() ?: false
                }
                override fun afterTextChanged(s: Editable?) { }
            }
        )
        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            enteredUsername = editTextUsername.text
            enteredPassword = editTextPassword.text
            isError = viewModel.authenticateCredentials(enteredUsername, enteredPassword)
            if (isError) {
                // User has entered the wrong credentials, prompt them for re-entry

            }
        }
    }
}
