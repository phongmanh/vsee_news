package com.manhnguyen.codebase.data.room.dao

import androidx.room.Dao
import com.manhnguyen.codebase.data.model.MovieDetail

@Dao
interface MovieDAO : IDao<MovieDetail.Movie> {
}