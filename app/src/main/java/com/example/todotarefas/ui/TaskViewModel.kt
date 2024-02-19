package com.example.todotarefas.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todotarefas.R
import com.example.todotarefas.ui.data.db.entity.toTaskEntity
import com.example.todotarefas.ui.data.model.Status
import com.example.todotarefas.ui.data.model.Task
import com.example.todotarefas.ui.data.repository.TaskRepository
import com.example.todotarefas.util.StateView
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository): ViewModel() {

    private val _taskStateData = MutableLiveData<StateTask>()
    val taskStateData: LiveData<StateTask> = _taskStateData

    private val _taskStateMessage = MutableLiveData<Int>()
    val taskStateMessage: LiveData<Int> = _taskStateMessage

    fun insertOrUpdateTask(
        id: Long = 0,
        description: String,
        status: Status
    ) {
        if(id == 0L){
            InsertTask(Task(description = description, status = status))
        }else{
            updateTask(Task(id, description, status))
        }
    }

    private fun InsertTask(task: Task) = viewModelScope.launch {
        try {

            val id = repository.insertTask(task.toTaskEntity())
            if(id > 0) {
                _taskStateData.postValue(StateTask.Inserted)
                _taskStateMessage.postValue(R.string.text_save_sucess_task)
            }

        } catch(e: Exception) {
            _taskStateMessage.postValue(R.string.text_save_erro_task)
        }
    }
    private fun updateTask(task: Task) = viewModelScope.launch {
        try {

            repository.updateTask(task.toTaskEntity())
            _taskStateData.postValue(StateTask.Update)
            _taskStateMessage.postValue(R.string.text_edit_sucess_task)

        } catch(e: Exception) {
            _taskStateMessage.postValue(R.string.text_edit_error_sucess_task)
        }
    }
    fun deleteTask(task: Task) = viewModelScope.launch {
        try {

            repository.deleteTask(task.id)
            _taskStateData.postValue(StateTask.Delete)
            _taskStateMessage.postValue(R.string.text_remove_sucess_task)

        } catch(e: Exception) {
            _taskStateMessage.postValue(R.string.text_error_remove_task)
        }
    }
}

sealed class StateTask {
    object Inserted: StateTask()
    object Update: StateTask()
    object Delete: StateTask()
}
