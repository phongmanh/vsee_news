package com.manhnguyen.codebase.domain.usercase

import com.manhnguyen.codebase.data.repository.LoginRepository
import com.manhnguyen.codebase.domain.usercase.login.ILoginRepository
import com.manhnguyen.codebase.presentation.login.data.model.LoggedInUser
import com.manhnguyen.codebase.presentation.login.data.model.Result
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: LoginRepository) {

    fun login(username: String, password: String): Result<LoggedInUser> {
        return repository.login(username, password)
    }

}