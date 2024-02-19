package com.example.todotarefas.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todotarefas.databinding.FragmentTasksBinding
import com.example.todotarefas.ui.adapter.TaskAdapter
import com.example.todotarefas.ui.data.db.AppDataBase
import com.example.todotarefas.ui.data.model.Status
import com.example.todotarefas.ui.data.model.Task
import com.example.todotarefas.ui.data.repository.TaskRepository
import com.example.todotarefas.util.StateView
import com.example.todotarefas.util.showButtonSheet

class TasksFragment : Fragment() {

    private lateinit var taskAdapter: TaskAdapter

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private val taskListViewModel: TaskListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>):T{
                if(modelClass.isAssignableFrom(TaskListViewModel::class.java)) {
                    // acesso ao banco de dados
                    val database = AppDataBase.getDatabase(requireContext())
                    val repository = TaskRepository(database.taskDao())

                    @Suppress("UNCHECKED_CAST")
                    return TaskListViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

    }


    private val taskViewModel: TaskViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>):T{
                if(modelClass.isAssignableFrom(TaskViewModel::class.java)) {
                    // acesso ao banco de dados
                    val database = AppDataBase.getDatabase(requireContext())
                    val repository = TaskRepository(database.taskDao())

                    @Suppress("UNCHECKED_CAST")
                    return TaskViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initlisteners()

        initRecycleView()
        observeViewModel()


    }

    override fun onResume() {
        super.onResume()
        taskListViewModel.getAllTasks()
    }

    private fun initlisteners() {
        binding.fabAdd.setOnClickListener {
            val action = TasksFragmentDirections.actionTasksFragmentToFormTaskFragment(null)
            findNavController().navigate(action)
        }
    }

    private fun observeViewModel() {
        taskListViewModel.taskList.observe(viewLifecycleOwner) { tasklist ->
            taskAdapter.submitList(tasklist)
            listEmpty(tasklist)
        }

        taskViewModel.taskStateData.observe(viewLifecycleOwner) { stateTask ->
            if(stateTask == StateTask.Delete) {
                taskListViewModel.getAllTasks()
            }

        }
    }

    private fun initRecycleView() {
        taskAdapter = TaskAdapter() { task, option ->
            optionSelected(task, option)
        }


        with(binding.rvTask) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = taskAdapter
        }
    }

    private fun optionSelected(task: Task, option: Int) {
        when (option) {
            TaskAdapter.SELECT_REMOVE -> {
                showButtonSheet(
                    message = "Você tem certeza que deseja remover essa tarefa",
                    onClick = {
                        taskViewModel.deleteTask(task)
                    }
                )
            }

            TaskAdapter.SELECT_EDIT -> {
                val action = TasksFragmentDirections
                    .actionTasksFragmentToFormTaskFragment(task)
                findNavController().navigate(action)
            }

            TaskAdapter.SELECT_DETAILS -> {

            }
        }
    }

    private fun listEmpty(taskList: List<Task>) {
        binding.txtInfo.text = if (taskList.isEmpty()) {
            "Você não possui nenhuma tarefa"
        } else {
            ""
        }

        binding.progressBar.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}