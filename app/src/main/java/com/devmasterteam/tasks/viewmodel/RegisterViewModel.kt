package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.Validation
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val  repository = PersonRepository(application.applicationContext)
    val securityPreference = SecurityPreferences(application.applicationContext)

    private val _create = MutableLiveData<Validation>()
    val create : LiveData<Validation> = _create

    fun create(name: String, email: String, password: String) {
        repository.create(name,email,password, object : ApiListener<PersonModel>{
            override fun onSucesso(result: PersonModel) {

                securityPreference.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)
                securityPreference.store(TaskConstants.SHARED.TOKEN_KEY, result.token)
                securityPreference.store(TaskConstants.SHARED.PERSON_NAME, result.name)

                RetrofitClient.addHeaders(result.token, result.personKey)
               _create.value = Validation()
            }

            override fun onFailure(message: String) {
                _create.value = Validation(message)
            }

        })
    }

}