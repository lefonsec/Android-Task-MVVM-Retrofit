package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.model.Validation
import com.devmasterteam.tasks.service.repository.TaskRepository

class TaskListViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(application.applicationContext)
    private  var taskFilter = 0

    private val _listTask = MutableLiveData<List<TaskModel>>()
    val listTask: LiveData<List<TaskModel>> = _listTask

    private val _delete = MutableLiveData<Validation>()
    val delete: LiveData<Validation> = _delete

    private val _status = MutableLiveData<Validation>()
    val status: LiveData<Validation> = _status

    fun listarTasks(filter: Int) {
        val listener = object : ApiListener<List<TaskModel>>{
            override fun onSucesso(result: List<TaskModel>) {
                _listTask.value = result
            }

            override fun onFailure(message: String) {
            }

        }
        when (filter ){
            TaskConstants.FILTER.ALL -> taskRepository.listTask(listener)
             TaskConstants.FILTER.NEXT -> taskRepository.listNext(listener)
            else -> taskRepository.listOverdue(listener)

        }
    }
    fun delete(id: Int) {
        taskRepository.delete(id,object : ApiListener<Boolean>{
            override fun onSucesso(result: Boolean) {
                listarTasks(taskFilter)
            }

            override fun onFailure(message: String) {
                _delete.value = Validation(message)
            }
        })
    }
    fun stutus(id: Int,complete: Boolean) {

        if (complete){
            taskRepository.complete(id,object :ApiListener<Boolean>{
                override fun onSucesso(result: Boolean) {
                    listarTasks(taskFilter)
                }

                override fun onFailure(message: String) {
                    _delete.value = Validation(message)
                }

            })
        }else{
            taskRepository.undo(id,object :ApiListener<Boolean>{
                override fun onSucesso(result: Boolean) {
                    listarTasks(taskFilter)
                }

                override fun onFailure(message: String) {
                    _delete.value = Validation(message)
                }
            })
        }

    }

}