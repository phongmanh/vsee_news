package com.manhnguyen.codebase.data.repository

import com.kwabenaberko.newsapilib.NewsApiClient
import com.kwabenaberko.newsapilib.models.request.EverythingRequest
import com.kwabenaberko.newsapilib.models.response.ArticleResponse
import com.manhnguyen.codebase.data.api.Api
import kotlin.coroutines.suspendCoroutine

class NewsRepository constructor(private val api: Api) {

    suspend fun getEverything(pageSize: Int, page: Int) = suspendCoroutine<ArticleResponse> { continuation ->
        api.newsApiClient.getEverything(
            EverythingRequest.Builder()
                .q("bbc-sport")
                .pageSize(pageSize)
                .page(page)
                .sortBy("publishedAt")
                .build(),
            object : NewsApiClient.ArticlesResponseCallback {
                override fun onSuccess(response: ArticleResponse) {
                    continuation.resumeWith(Result.success(response))
                }

                override fun onFailure(t: Throwable) {
                    continuation.resumeWith(Result.failure(t))
                }

            })
    }
}