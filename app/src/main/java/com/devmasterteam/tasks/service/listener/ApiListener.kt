package com.devmasterteam.tasks.service.listener

interface ApiListener<T> {

    fun onSucesso(result : T)
    fun onFailure(message: String)
}