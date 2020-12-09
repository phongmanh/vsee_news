package com.manhnguyen.codebase.data.api

import com.manhnguyen.codebase.domain.usercase.login.ILoginRepository
import com.manhnguyen.codebase.presentation.login.data.model.LoggedInUser
import com.manhnguyen.codebase.presentation.login.data.model.Result
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource : ILoginRepository {

    override fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            // TODO: handle loggedInUser authentication
            if (!username.equals("admin", true) || !password.equals("123456", true))
                Result.Error(
                    IOException(
                        "Error logging in",
                        Throwable("Username or Password is invalid")
                    )
                )
            else {
                val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
                Result.Success(fakeUser)
            }
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    override fun logout() {
        // TODO: revoke authentication
    }
}