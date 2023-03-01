package com.devmasterteam.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityLoginBinding
import com.devmasterteam.tasks.databinding.ActivityRegisterBinding
import com.devmasterteam.tasks.viewmodel.LoginViewModel
import com.devmasterteam.tasks.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        binding = ActivityRegisterBinding.inflate(layoutInflater)

        // Eventos
        binding.buttonSave.setOnClickListener(this)

        this.observer()
        // Layout
        setContentView(binding.root)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_save){
            this.handleSave()
        }
    }

    private  fun observer(){
        viewModel.create.observe(this){validatiion ->
            if (validatiion.status()){
                startActivity(Intent(this,MainActivity::class.java))
            }else{
                Toast.makeText(this,validatiion.message(),Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSave() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()
        val name = binding.editName.text.toString()
        viewModel.create(name,email,password)
    }
}