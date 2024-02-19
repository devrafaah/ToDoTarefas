package com.example.todotarefas.ui.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Task(
    var id: Long = 0,
    var description: String = "",
    var status: Status = Status.TODO
): Parcelable