package com.example.kotlin_assignment_eighteen.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("GENERAL_KEY", Context.MODE_PRIVATE)
        val name = sharedPref.getString("NAME", "")
        val email = sharedPref.getString("EMAIL", "")
        val password = sharedPref.getString("PASSWORD", "")

        showExistingPreference(name.toString())

        binding.ibCalender.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)

        with(binding) {
            pbMain.visibility = View.VISIBLE
            btnSave.setOnClickListener { onClickBtnSave() }
            constraintLayout.setOnClickListener { onClickLayout() }
            btnLogout.setOnClickListener { onClickBtnLogout() }
        }

        getAllTask()

    }

    private fun onClickBtnLogout() {
        clearSharedPref()
        binding.llLogin.visibility = View.GONE
        finishAndRemoveTask()
    }

    private fun clearSharedPref() {
        val editor = sharedPref.edit()
        editor.putBoolean("IS_LOGIN", false)
        editor.putString("NAME", "")
        editor.putString("EMAIL", "")
        editor.putString("PASSWORD", "password")
        editor.apply()
    }

    private fun showExistingPreference(name: String) {
        if (name != "") {
            showLinearLayout(name)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showLinearLayout(name: String) {
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
        with(binding) {
            llLogin.visibility = View.VISIBLE
            tvWelcome.text = "Welcome, $name"
        }
    }

    private fun onClickBtnSave() {
        if (binding.etTaskName.text.isNullOrEmpty() || binding.etDueDate.text.isNullOrEmpty()) {
            Toast.makeText(this@MainActivity, "isi data dulu", Toast.LENGTH_SHORT).show()
        } else {
            addNewTask()
            binding.etTaskName.text = null
            binding.etDueDate.text = null
        }
    }

    private fun onClickLayout() {
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
        binding.etTaskName.clearFocus()
        binding.etDueDate.clearFocus()
    }

    private fun getAllTask() {
        NetworkConfig().getService()
            .getAllTasks()
            .enqueue(object : Callback<GetAllTaskResponse> {
                override fun onFailure(call: Call<GetAllTaskResponse>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        t.localizedMessage, Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(
                    call: Call<GetAllTaskResponse>,
                    response: Response<GetAllTaskResponse>
                ) {
                    binding.rvTasks.layoutManager = LinearLayoutManager(this@MainActivity)
                    val btnEdit = TaskAdapter(response.body()?.data as List<DataItem>)
                    binding.rvTasks.adapter = btnEdit
                    binding.pbMain.visibility = View.GONE

                    btnEdit.setOnItemClickCallback(object : TaskAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: DataItem) {
                            Toast.makeText(
                                this@MainActivity,
                                "Edit task id ${data.id}", Toast.LENGTH_SHORT
                            ).show()
                            binding.tvIdTaskMain.text = data.id
                            binding.etTaskName.setText(data.taskName)
                            binding.etDueDate.setText(data.taskDate)
                            binding.btnSave.setOnClickListener {
                                updateTask(context = applicationContext, data.id.toString())
                                binding.etTaskName.text = null
                                binding.etDueDate.text = null
                            }
                        }
                    })

                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemAddAccount -> {
                val intentToAddAccount = Intent(this, UsersActivity::class.java)
                startActivity(intentToAddAccount)
                true
            }
            else -> true
        }
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
        when (v?.id) {
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
            .enqueue(object : Callback<CreateDataResponse> {
                override fun onResponse(
                    call: Call<CreateDataResponse>,
                    response: Response<CreateDataResponse>
                ) {
                    Toast.makeText(
                        this@MainActivity,
                        "Berhasil Menambahkan Task Baru",
                        Toast.LENGTH_SHORT
                    ).show()
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
            .enqueue(object : Callback<UpdateDataResponse> {
                override fun onResponse(
                    call: Call<UpdateDataResponse>,
                    response: Response<UpdateDataResponse>
                ) {
                    Toast.makeText(
                        context.applicationContext,
                        "Berhasil Mengedit Task $id", Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(call: Call<UpdateDataResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    fun deleteTask(context: Context, id: String) {
        NetworkConfig().getService()
            .deleteTask(id)
            .enqueue(object : Callback<DeleteDataResponse> {
                override fun onResponse(
                    call: Call<DeleteDataResponse>,
                    response: Response<DeleteDataResponse>
                ) {
                    Toast.makeText(
                        context.applicationContext,
                        "Berhasil Menghapus Task $id", Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(call: Call<DeleteDataResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

}