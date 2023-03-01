package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.repository.SecurityPreferences

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val secury = SecurityPreferences(application.applicationContext)

    private val _user  = MutableLiveData<String>()
    val user: LiveData<String> = _user
    fun logout(){
        secury.remove(TaskConstants.SHARED.TOKEN_KEY)
        secury.remove(TaskConstants.SHARED.PERSON_KEY)
        secury.remove(TaskConstants.SHARED.PERSON_NAME)
    }

    fun loadUSerName(){
        _user.value =  secury.get(TaskConstants.SHARED.PERSON_NAME)
    }
}