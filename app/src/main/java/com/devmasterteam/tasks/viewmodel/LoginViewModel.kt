package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.Validation
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {


    private val personRepository = PersonRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)

    private val securityPreference = SecurityPreferences(application.applicationContext)

    private val _Login = MutableLiveData<Validation>()
    val login: LiveData<Validation> = _Login

    private val _LoggerUser = MutableLiveData<Boolean>()
    val loggerUser: LiveData<Boolean> = _LoggerUser


    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        personRepository.login(email, password, object : ApiListener<PersonModel> {
            override fun onSucesso(result: PersonModel) {
                securityPreference.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)
                securityPreference.store(TaskConstants.SHARED.TOKEN_KEY, result.token)
                securityPreference.store(TaskConstants.SHARED.PERSON_NAME, result.name)

                RetrofitClient.addHeaders(result.token, result.personKey)
                _Login.value = Validation()
            }

            override fun onFailure(message: String) {
                _Login.value = Validation(message)
            }
        })

    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
        val person = securityPreference.get(TaskConstants.SHARED.PERSON_KEY)
        val token = securityPreference.get(TaskConstants.SHARED.TOKEN_KEY)
        RetrofitClient.addHeaders(token, person)
        val logged = token != "" && person != ""
        _LoggerUser.value = logged

        if (!logged){
            priorityRepository.lista(object :ApiListener<List<PriorityModel>>{
                override fun onSucesso(result: List<PriorityModel>) {
                    priorityRepository.save(result)
                }

                override fun onFailure(message: String) {
                }

            })
        }

    }

}