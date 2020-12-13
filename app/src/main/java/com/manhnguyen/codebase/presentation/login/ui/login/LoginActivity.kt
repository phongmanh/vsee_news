package com.manhnguyen.codebase.presentation.login.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import butterknife.BindView
import butterknife.ButterKnife
import com.manhnguyen.codebase.ApplicationController
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.base.ActivityBase
import com.manhnguyen.codebase.presentation.login.data.model.LoggedInUserView
import com.manhnguyen.codebase.presentation.main.MainActivity
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : ActivityBase() {

    /*private lateinit var  loginViewModel: LoginViewModel*/
    private val loginViewModel: LoginViewModel by viewModel()

    @BindView(R.id.login)
    lateinit var login: Button

    @BindView(R.id.username)
    lateinit var username: EditText

    @BindView(R.id.password)
    lateinit var password: EditText

    @BindView(R.id.loading)
    lateinit var loading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("Login", "onCreate")
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)
        /*loginViewModel = getViewModel()*/
        init()
    }

    private fun init() {

/*        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)*/

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
                startMainActivity(MainActivity())
            }
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
    }


    override fun onRestart() {
        super.onRestart()
        Log.e("Login", "onRestart")
    }

    override fun onStart() {
        super.onStart()
        Log.e("Login", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e("Login", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("Login", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("Login", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("Login", "OnDestroy")
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}