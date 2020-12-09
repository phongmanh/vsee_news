package com.manhnguyen.codebase.domain.usercase.login

import com.manhnguyen.codebase.presentation.login.data.model.LoggedInUser
import com.manhnguyen.codebase.presentation.login.data.model.Result

interface ILoginRepository {
    fun login(username: String, password: String): Result<LoggedInUser>
    fun logout()
}