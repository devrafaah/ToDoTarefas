package com.example.todotarefas.ui.data.repository

import com.example.todotarefas.ui.data.db.dao.taskDao
import com.example.todotarefas.ui.data.db.entity.TaskEntity
import com.example.todotarefas.ui.data.model.Task

class TaskRepository(private val taskDao: taskDao) {
    suspend fun getAllTask(): List<Task> {
        return taskDao.getAllTask()
    }

    suspend fun insertTask(taskEntity: TaskEntity): Long {
        return taskDao.insertTask(taskEntity)
    }

    suspend fun deleteTask(id: Long) = taskDao.deleteTask(id)

    suspend fun updateTask(taskEntity: TaskEntity) {
        taskDao.updateTask(
            id = taskEntity.id,
            description = taskEntity.description,
            status = taskEntity.status
        )
    }
}