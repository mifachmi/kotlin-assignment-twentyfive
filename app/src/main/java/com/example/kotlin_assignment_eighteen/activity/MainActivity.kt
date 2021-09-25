package com.example.kotlin_assignment_eighteen.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin_assignment_eighteen.R
import com.example.kotlin_assignment_eighteen.adapter.TaskAdapter
import com.example.kotlin_assignment_eighteen.databinding.ActivityMainBinding
import com.example.kotlin_assignment_eighteen.fragment.DatePickerFragment
import com.example.kotlin_assignment_eighteen.model.*
import com.example.kotlin_assignment_eighteen.network.NetworkConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener,
    View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ibCalender.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        binding.pbMain.visibility = View.VISIBLE

        binding.btnSave.setOnClickListener {
            if(binding.etTaskName.text.isNullOrEmpty() || binding.etDueDate.text.isNullOrEmpty()) {
                Toast.makeText(this@MainActivity, "isi data dulu", Toast.LENGTH_SHORT).show()
            } else {
                addNewTask()
                binding.etTaskName.text = null
                binding.etDueDate.text = null
            }
        }

        binding.constraintLayout.setOnClickListener {
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
                hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            }
            binding.etTaskName.clearFocus()
            binding.etDueDate.clearFocus()
        }

        NetworkConfig().getService()
            .getAllTasks()
            .enqueue(object : Callback<GetAllTaskResponse> {
                override fun onFailure(call: Call<GetAllTaskResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<GetAllTaskResponse>,
                    response: Response<GetAllTaskResponse>
                ) {
                    binding.rvTasks.layoutManager = LinearLayoutManager(this@MainActivity)
                    val btnEdit = TaskAdapter(response.body()?.data as List<DataItem>)
                    binding.rvTasks.adapter = btnEdit
                    binding.pbMain.visibility = View.GONE

                    btnEdit.setOnItemClickCallback(object : TaskAdapter.OnItemClickCallback{
                        override fun onItemClicked(data: DataItem) {
                            Toast.makeText(this@MainActivity, "Edit task id ${data.id}", Toast.LENGTH_SHORT).show()
                            binding.tvIdTaskMain.text = data.id
                            binding.etTaskName.setText(data.taskName)
                            binding.etDueDate.setText(data.taskDate)
                            binding.btnSave.setOnClickListener {
                                updateTask(context = applicationContext, data.id.toString())
                                binding.etTaskName.text = null
                                binding.etDueDate.text = null
//                                clickBtnSave(data)
                            }
                        }
                    })

                }
            })

    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        // Siapkan date formatter-nya terlebih dahulu
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Set text dari textview once
        binding.etDueDate.setText(dateFormat.format(calendar.time))
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.ibCalender -> {
                val datePickerFragment = DatePickerFragment()
                datePickerFragment.show(supportFragmentManager, "DATE_PICKER_TAG")
            }
        }
    }

    private fun addNewTask() {
        NetworkConfig().getService()
            .insertTask(
                id = "",
                binding.etTaskName.text.toString(),
                binding.etDueDate.text.toString(),
                is_done = "0"
            )
            .enqueue(object : Callback<CreateDataResponse>{
                override fun onResponse(
                    call: Call<CreateDataResponse>,
                    response: Response<CreateDataResponse>
                ) {
                    Toast.makeText(this@MainActivity, "Berhasil Menambahkan Task Baru", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<CreateDataResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    fun updateTask(context: Context, id: String) {
        NetworkConfig().getService()
            .updateTask(
                binding.etTaskName.text.toString(),
                binding.etDueDate.text.toString(),
                is_done = "0",
                id
            )
            .enqueue(object : Callback<UpdateTaskResponse>{
                override fun onResponse(
                    call: Call<UpdateTaskResponse>,
                    response: Response<UpdateTaskResponse>
                ) {
                    Toast.makeText(context.applicationContext, "Berhasil Mengedit Task $id", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<UpdateTaskResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    fun deleteTask(context: Context, id: String) {
        NetworkConfig().getService()
            .deleteTask(id)
            .enqueue(object : Callback<DeleteDataResponse>{
                override fun onResponse(
                    call: Call<DeleteDataResponse>,
                    response: Response<DeleteDataResponse>
                ) {
                    Toast.makeText(context.applicationContext, "Berhasil Menghapus Task $id", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<DeleteDataResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

}