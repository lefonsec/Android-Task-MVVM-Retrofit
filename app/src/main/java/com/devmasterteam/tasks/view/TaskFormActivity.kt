package com.devmasterteam.tasks.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.databinding.ActivityRegisterBinding
import com.devmasterteam.tasks.databinding.ActivityTaskFormBinding
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.viewmodel.RegisterViewModel
import com.devmasterteam.tasks.viewmodel.TaskFormViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

class TaskFormActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var viewModel: TaskFormViewModel
    private lateinit var binding: ActivityTaskFormBinding
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    private var listPriority : List<PriorityModel> = mutableListOf()
    private var taskIdentification = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Variáveis da classe
        viewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)

        // Eventos
        binding.buttonSave.setOnClickListener(this)
        binding.buttonDate.setOnClickListener(this)

        viewModel.loadPriorities()

        loadDataFromActivity()
        this.observer()
        // Layout
        setContentView(binding.root)
    }
    override fun onClick(v: View) {
        if (v.id == R.id.button_date) {
            this.handleDate()
        }else if (v.id == R.id.button_save){
            this.handleSave()
        }
    }
    private fun handleSave() {
        val taskModel = TaskModel().apply {
            this.id = taskIdentification
            this.description = binding.editDescription.text.toString()
            this.complete = binding.checkComplete.isChecked
            this.dueDate = binding.buttonDate.text.toString()
            val index = binding.spinnerPriority.selectedItemPosition
            this.priorityId = listPriority[index].id
        }

        viewModel.save(taskModel)
    }
    private fun handleDate() {
        val instance = Calendar.getInstance()
        val year = instance.get(Calendar.YEAR)
        val month = instance.get(Calendar.MONTH)
        val day = instance.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(this, this, year, month, day).show()
    }
    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {

        val calendar = Calendar.getInstance()
        calendar.set(year,month,dayOfMonth)
        val date = dateFormat.format(calendar.time)
        binding.buttonDate.text = date

    }
    private fun observer() {
        viewModel.priorityList.observe(this) {
            listPriority = it
            val list = mutableListOf<String>()
            for (prioridade in it) {
                list.add(prioridade.description)
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, list)
            binding.spinnerPriority.adapter = adapter
        }

        viewModel.taskSave.observe(this) {
            if (it.status()) {
                Toast.makeText(applicationContext, "sucesso", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(applicationContext, it.message(), Toast.LENGTH_SHORT).show()
            }
        }


        viewModel.task.observe(this) {
            val parse = SimpleDateFormat("yyyy-MM-dd").parse(it.dueDate)
            val data = SimpleDateFormat("dd/MM/yyyy").format(parse)
            binding.editDescription.setText(it.description)
            binding.checkComplete.isChecked = it.complete
            binding.buttonDate.text = data
            binding.spinnerPriority.setSelection(getIndex(it.priorityId))
        }

        viewModel.taskValidation.observe(this) {
            if (!it.status()) {
                Toast.makeText(applicationContext, it.message(), Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }

    private fun getIndex(priorityId: Int): Int {
        var index = 0
            for (l in listPriority){
                if (l.id == priorityId){
                    break
                }
                index++
            }
        return index
    }

    private fun loadDataFromActivity() {
        val bundle = intent.extras
        if (bundle != null) {
            taskIdentification = bundle.getInt(TaskConstants.BUNDLE.TASKID)
            viewModel.load(taskIdentification)
        }

    }
}