package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.Validation
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskFormViewModel(application: Application) : AndroidViewModel(application) {

    private val priorityRepository = PriorityRepository(application.applicationContext)
    private val taskRepository = TaskRepository(application.applicationContext)

    private val _priorityList = MutableLiveData<List<PriorityModel>>()
    val priorityList: LiveData<List<PriorityModel>> = _priorityList

    private val _taskSave = MutableLiveData<Validation>()
    val taskSave: LiveData<Validation> = _taskSave

    private val _task = MutableLiveData<TaskModel>()
    val task: LiveData<TaskModel> = _task

    private val _taskValidation = MutableLiveData<Validation>()
    val taskValidation: LiveData<Validation> = _taskValidation

    fun loadPriorities() {
        val listBanco = priorityRepository.listBanco()
        _priorityList.value = listBanco
    }

    fun load(id: Int) {
        taskRepository.load(id, object : ApiListener<TaskModel> {
            override fun onSucesso(result: TaskModel) {
                _task.value = result
            }

            override fun onFailure(message: String) {
                _taskValidation.value = Validation(message)
            }
        })
    }

    fun save(taskModel: TaskModel) {
        val listener = object : ApiListener<Boolean> {
            override fun onSucesso(result: Boolean) {
                _taskSave.value = Validation()
            }

            override fun onFailure(message: String) {
                _taskSave.value = Validation(message)
            }
        }
        if (taskModel.id == 0) {
            taskRepository.create(taskModel, listener)
        } else {
            taskRepository.update(taskModel, listener)
        }
    }
}