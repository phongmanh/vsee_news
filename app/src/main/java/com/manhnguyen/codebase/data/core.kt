package com.manhnguyen.codebase.data

import com.kwabenaberko.newsapilib.models.response.ArticleResponse
import com.manhnguyen.codebase.data.model.News

internal fun ArticleResponse.toDataModel() = this.articles.map {
    News(article = it)
}