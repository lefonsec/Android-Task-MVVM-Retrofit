package com.devmasterteam.tasks.service.model

class Validation(message : String = "") {

    private var status: Boolean = true
    private var vaidationMessage = ""

    init {
        if (message != ""){
            vaidationMessage = message
            status = false
        }
    }
    fun status() = status
    fun message() = vaidationMessage
}