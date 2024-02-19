package com.example.todotarefas.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todotarefas.R
import com.example.todotarefas.databinding.FragmentFormTaskBinding
import com.example.todotarefas.ui.data.db.AppDataBase
import com.example.todotarefas.ui.data.model.Status
import com.example.todotarefas.ui.data.model.Task
import com.example.todotarefas.ui.data.repository.TaskRepository
import com.example.todotarefas.util.initToolbar
import com.example.todotarefas.util.showButtonSheet

class FormTaskFragment : BaseFragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: Task
    private var creatednewTask: Boolean = true
    private var status: Status = Status.TODO

    private val viewmodel: TaskViewModel by viewModels {
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

    private val args: FormTaskFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)

        getArgs()
        initListener()
    }



    private fun initListener() {
        binding.btnNewTask.setOnClickListener {
            observeViewModel()
            validateData()
        }
        binding.rbGroup.setOnCheckedChangeListener { _, id ->
            status = when(id){
                R.id.rbTodo -> Status.TODO
                R.id.rbDoing -> Status.DOING
                else -> Status.DONE
            }
        }
    }

    private fun getArgs() {
        args.task.let {
            if(it != null) {
                this.task = it

                configTask()
            }
        }
    }

    private fun configTask() {
        creatednewTask = false
        status = task.status

        binding.textToolbar.text = "Editando Tarefa..."
        binding.editDescription.setText(task.description)
        setStatus()
    }

    private fun setStatus(){
        binding.rbGroup.check(
            when(task.status) {
                Status.TODO -> R.id.rbTodo
                Status.DOING -> R.id.rbDoing
                else -> R.id.rbDone
            }
        )
    }

    private fun validateData() {
        val description = binding.editDescription.text.trim().toString()

        if (description.isNotEmpty()) {
            hidekeyboard()
            binding.progressBar.isVisible = true
            if(creatednewTask) task = Task()
            task.description = description
            task.status = status

            if(creatednewTask) {
                viewmodel.insertOrUpdateTask(
                    description = description,
                    status = status)
            }else {
                viewmodel.insertOrUpdateTask(
                    id = task.id,
                    description = description,
                    status = status)
            }
        }
        else {
            showButtonSheet(message = getString(R.string.text_description_Required_new_task_fragment))
        }
    }

    private fun observeViewModel() {
        viewmodel.taskStateData.observe(viewLifecycleOwner) { Taskstate ->
            if( Taskstate == StateTask.Inserted || Taskstate == StateTask.Update) {
                findNavController().popBackStack()
            }
        }

        viewmodel.taskStateMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(
                requireContext(),
                getString(message),
                Toast.LENGTH_LONG
            ).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
