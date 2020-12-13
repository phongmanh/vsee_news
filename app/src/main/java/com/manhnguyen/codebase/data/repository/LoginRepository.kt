package com.manhnguyen.codebase.data.repository

import com.manhnguyen.codebase.data.api.ApiInterface
import com.manhnguyen.codebase.data.api.LoginDataSource
import com.manhnguyen.codebase.di.module.APIServiceModule
import com.manhnguyen.codebase.domain.usercase.login.ILoginRepository
import com.manhnguyen.codebase.presentation.login.data.model.LoggedInUser
import com.manhnguyen.codebase.presentation.login.data.model.Result

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository constructor(private val apiServiceModule: APIServiceModule) :
    ILoginRepository {

    private val apiInterface: ApiInterface = apiServiceModule.aipService()
    private val dataSource = LoginDataSource()

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    override fun logout() {
        user = null
        dataSource.logout()
    }

    override fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login
        val result = dataSource.login(username, password)
        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}