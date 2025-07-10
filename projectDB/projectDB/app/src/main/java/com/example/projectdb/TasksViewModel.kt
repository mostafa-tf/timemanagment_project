package com.example.projectdb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TasksViewModel(val dao: TaskDao) : ViewModel() {
    val newTaskName = MutableLiveData<String>()
    val newTaskDate = MutableLiveData<Long?>() // Nullable to handle null properly

    // Use MutableLiveData for tasks to allow updates
    val tasks = MutableLiveData<List<Task>>()

    // LiveData to observe all tasks from the database
    val allTasks: LiveData<List<Task>> = dao.getAll()

    init {
        // Observe the allTasks LiveData and update tasks MutableLiveData when it changes
        allTasks.observeForever { taskList ->
            tasks.value = taskList
        }
    }

    fun addTask() {
        viewModelScope.launch {
            val task = Task()
            task.taskName = newTaskName.value ?: ""
            // Ensure taskDate is set to the current time if not set
            task.taskDate = newTaskDate.value ?: System.currentTimeMillis()
            dao.insert(task)

            // Clear input fields
            newTaskName.value = ""
            newTaskDate.value = null
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            dao.update(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            dao.delete(task) // Delete the task from the database
        }
    }

    fun filterTasksByDate(selectedDate: Long) {
        viewModelScope.launch {
            // Filter tasks based on the selected date
            tasks.value = dao.getTasksByDate(selectedDate)
        }
    }

    suspend fun getnboftasks(): Int {
        return withContext(Dispatchers.IO){
            dao.getnbtasks()
        }
    }

    suspend fun getnbofdonetasks(): Int {

        return withContext(Dispatchers.IO){

            dao.getnbdonetasks()
        }
    }
    suspend fun getnbofundonetasks(): Int {


        return withContext(Dispatchers.IO){

            dao.getnbundonetasks()
        }

    }





}