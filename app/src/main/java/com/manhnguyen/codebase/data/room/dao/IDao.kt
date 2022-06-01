package com.manhnguyen.codebase.data.room.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Update

interface IDao<T> {

    /**
     * @param entity
     * @return the number of rows effected
     */
    @Update
    fun update(vararg entity: T): Int

    /**
     * @param entity
     * @return a list of rows effected
     */
    @Insert(onConflict = IGNORE)
    fun insert(vararg entity: T): List<Long>

    /**
     * @param entity
     * @return a list of rows effected
     */
    @Insert(onConflict = REPLACE)
    fun insertOrUpdate(vararg entity: T): List<Long>

    /**
     * @param entity
     * @return the number of rows effected
     */
    @Delete
    fun delete(vararg entity: T): Int

}