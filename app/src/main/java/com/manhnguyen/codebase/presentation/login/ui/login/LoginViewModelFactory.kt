package com.manhnguyen.codebase.presentation.login.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manhnguyen.codebase.data.api.LoginDataSource
import com.manhnguyen.codebase.data.repository.LoginRepository
import com.manhnguyen.codebase.domain.usercase.LoginUseCase
import com.manhnguyen.codebase.domain.usercase.login.ILoginRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        /*if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginUseCase = LoginUseCase()
            ) as T
        }*/
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}