package com.manhnguyen.codebase.data.repository

import com.manhnguyen.codebase.data.api.ApiInterface
import com.manhnguyen.codebase.di.component.ActivityScope
import com.manhnguyen.codebase.domain.model.user.UserModel
import com.manhnguyen.codebase.domain.model.login.UserRequestBody
import com.manhnguyen.codebase.domain.usercase.login.ILoginRepository
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

@ActivityScope
class LoginRepository @Inject constructor(private val service: ApiInterface) {

    fun logIn(user: UserRequestBody): Observable<Response<ResponseBody>> {
        return service.signIn(user)
    }


    fun getProfile(token: String): Observable<Response<ResponseBody>> {
        return service.getUserProfile(token)
    }
}