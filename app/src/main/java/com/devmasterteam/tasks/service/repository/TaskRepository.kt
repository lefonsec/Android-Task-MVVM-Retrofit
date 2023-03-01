package com.devmasterteam.tasks.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(val context: Context) : BaseRepository() {

    private val remote = RetrofitClient.getService(TaskService::class.java)

    fun create(task: TaskModel, listener: ApiListener<Boolean>) {
        val call = remote.create(task.priorityId, task.description, task.dueDate, task.complete)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSucesso(it) }
                } else {
                    val str = convertJsonToString(response.errorBody()!!.string())
                    listener.onFailure(str)
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }


    fun listTask(listener: ApiListener<List<TaskModel>>) {
//        if (!this.isConnectionAvaliable()){
//            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
//            return
//        }
        val call = remote.listTasks()
        call.enqueue(object : Callback<List<TaskModel>> {
            override fun onResponse(
                call: Call<List<TaskModel>>,
                response: Response<List<TaskModel>>
            ) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSucesso(it) }
                } else {
                    val str = convertJsonToString(response.errorBody()!!.string())
                    listener.onFailure(str)
                }
            }

            override fun onFailure(call: Call<List<TaskModel>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })

    }

    fun listNext(listener: ApiListener<List<TaskModel>>) {
        val call = remote.listnext()
        call.enqueue(object : Callback<List<TaskModel>> {
            override fun onResponse(
                call: Call<List<TaskModel>>,
                response: Response<List<TaskModel>>
            ) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSucesso(it) }
                } else {
                    val str = convertJsonToString(response.errorBody()!!.string())
                    listener.onFailure(str)
                }
            }

            override fun onFailure(call: Call<List<TaskModel>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }

    fun listOverdue(listener: ApiListener<List<TaskModel>>) {
        val call = remote.listOverdue()
        call.enqueue(object : Callback<List<TaskModel>> {
            override fun onResponse(
                call: Call<List<TaskModel>>,
                response: Response<List<TaskModel>>
            ) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSucesso(it) }
                } else {
                    val str = convertJsonToString(response.errorBody()!!.string())
                    listener.onFailure(str)
                }
            }

            override fun onFailure(call: Call<List<TaskModel>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })

    }

    fun delete(id: Int, listener: ApiListener<Boolean>) {
        val call = remote.delete(id)

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {

                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSucesso(it) }
                } else {
                    val str = convertJsonToString(response.errorBody()!!.string())
                    listener.onFailure(str)
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }

    fun complete(id: Int, listener: ApiListener<Boolean>) {
        val call = remote.complete(id)

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {

                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSucesso(it) }
                } else {
                    val str = convertJsonToString(response.errorBody()!!.string())
                    listener.onFailure(str)
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }

    fun undo(id: Int, listener: ApiListener<Boolean>) {
        val call = remote.undo(id)

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {

                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSucesso(it) }
                } else {
                    val str = convertJsonToString(response.errorBody()!!.string())
                    listener.onFailure(str)
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }

    fun load(id: Int, listener: ApiListener<TaskModel>) {
        val call = remote.load(id)
        call.enqueue(object : Callback<TaskModel> {
            override fun onResponse(call: Call<TaskModel>, response: Response<TaskModel>) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSucesso(it) }
                } else {
                    val str = convertJsonToString(response.errorBody()!!.string())
                    listener.onFailure(str)
                }
            }

            override fun onFailure(call: Call<TaskModel>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }

    fun update(task: TaskModel, listener: ApiListener<Boolean>) {
        val call =
            remote.update(task.id, task.priorityId, task.description, task.dueDate, task.complete)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSucesso(it) }
                } else {
                    val str = convertJsonToString(response.errorBody()!!.string())
                    listener.onFailure(str)
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
        })
    }

//    @RequiresApi(Build.VERSION_CODES.M)
//    fun isConnectionAvaliable(): Boolean {
//        var resulte = false
//        val cm =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork = cm.activeNetwork ?: return false
//        val capacillyti = cm.getNetworkCapabilities(activeNetwork) ?: return false
//        resulte = when{
//            capacillyti.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)-> true
//            capacillyti.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//            else -> false
//        }
//        return  resulte
//    }

}