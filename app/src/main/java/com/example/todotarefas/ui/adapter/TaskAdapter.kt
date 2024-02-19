package com.example.todotarefas.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todotarefas.databinding.ItemTaskBinding
import com.example.todotarefas.ui.data.model.Task

class TaskAdapter(
    private val taskSelected:(Task, Int) -> Unit
) : ListAdapter<Task, TaskAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val SELECT_REMOVE: Int = 2
        val SELECT_EDIT: Int = 3
        val SELECT_DETAILS: Int = 4


        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {

            // esses metodos iram comparar lista antiga - lista nova
            // diffUtil sempre trabalha com 2 listas

            override fun areItemsTheSame(
                oldItem: Task, newItem: Task
            ): Boolean {
                return oldItem.id == newItem.id && oldItem.description == newItem.description
            }

            override fun areContentsTheSame(
                oldItem: Task, newItem: Task
            ): Boolean {
                return oldItem == newItem && oldItem.description == newItem.description
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = getItem(position)

        holder.binding.txtTaskTitle.text = task.description

        holder.binding.btnRemoverTask.setOnClickListener{taskSelected(task, SELECT_REMOVE)}
        holder.binding.btnEditTask.setOnClickListener{taskSelected(task, SELECT_EDIT)}
        holder.binding.btnDetailsTask.setOnClickListener{taskSelected(task, SELECT_DETAILS)}
    }


    inner class MyViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}