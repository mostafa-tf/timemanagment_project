package com.example.projectdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task)
    @Update
    suspend fun update(task: Task)
    @Delete
    suspend fun delete(task: Task)
    @Query("SELECT * FROM task_table WHERE taskId = :key")
    fun get(key: Long): LiveData<Task>
    @Query("SELECT * FROM task_table ORDER BY taskId DESC")
    fun getAll(): LiveData<List<Task>>
    @Query("SELECT * FROM task_table WHERE strftime('%Y-%m-%d', task_date / 1000, 'unixepoch') = strftime('%Y-%m-%d', :date / 1000, 'unixepoch') ORDER BY task_date ASC")
    suspend fun getTasksByDate(date: Long): List<Task>

    @Query("SELECT count(*) FROM task_table")
    suspend fun getnbtasks():Int

    @Query("SELECT count(*) FROM task_table WHERE task_done=1")
    suspend fun getnbdonetasks():Int
    @Query("SELECT count(*) FROM task_table WHERE task_done=0")
    suspend fun getnbundonetasks():Int


}