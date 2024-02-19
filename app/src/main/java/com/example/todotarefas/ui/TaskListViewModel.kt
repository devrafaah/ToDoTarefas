package com.example.todotarefas.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todotarefas.ui.data.model.Task
import com.example.todotarefas.ui.data.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskListViewModel(private val repository: TaskRepository): ViewModel()  {

    private val _taskList = MutableLiveData<List<Task>>()
    val taskList: LiveData<List<Task>> = _taskList

    fun getAllTasks() = viewModelScope.launch {
        try {
            _taskList.postValue(repository.getAllTask())
        }catch(e: Exception) {

        }
    }
}