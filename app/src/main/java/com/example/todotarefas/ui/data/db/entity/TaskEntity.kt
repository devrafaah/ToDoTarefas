package com.example.todotarefas.ui.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todotarefas.ui.data.model.Status
import com.example.todotarefas.ui.data.model.Task

@Entity(tableName = "task_table")
class TaskEntity(
    @PrimaryKey(true)
    val id: Long = 0,

    val description: String,

    val status: Status
)

fun Task.toTaskEntity(): TaskEntity {

    return with(this) {
        TaskEntity(
            id = this.id,
            description = this.description,
            status = this.status
        )
    }
}