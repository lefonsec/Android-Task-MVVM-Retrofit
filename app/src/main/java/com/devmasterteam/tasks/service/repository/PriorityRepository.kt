package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(val context: Context) : BaseRepository() {
    private val remote = RetrofitClient.getService(PriorityService::class.java)
    private val database = TaskDatabase.getDatabase(context).priorityDao()

    companion object {
        private val cache = mutableMapOf<Int, String>()

        fun getDescription(id: Int): String {
            return cache[id] ?: ""
        }

        fun setDescription(id: Int, string: String){
            cache[id] = string
        }
    }

    fun lista(listener: ApiListener<List<PriorityModel>>) {
        val call = remote.list()
        call.enqueue(object : Callback<List<PriorityModel>> {
            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>
            ) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    response.body()?.let { listener.onSucesso(it) }
                } else {
                    val str = convertJsonToString(response.errorBody()!!.string())
                    listener.onFailure(str)
                }
            }

            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

        })

    }

    fun save(lista: List<PriorityModel>) {
        database.clear()
        database.save(lista)
    }

    fun listBanco(): List<PriorityModel> {
        return database.list()
    }

    fun getDescription(id: Int): String {
        return if (PriorityRepository.getDescription(id) == ""){
            val description =  database.getDescription(id)
            PriorityRepository.setDescription(id,description)
            description
        }else{
            PriorityRepository.getDescription(id)
        }

    }


}