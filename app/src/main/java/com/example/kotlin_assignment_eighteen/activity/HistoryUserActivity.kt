package com.example.kotlin_assignment_eighteen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlin_assignment_eighteen.R
import com.example.kotlin_assignment_eighteen.adapter.ListUserAdapter
import com.example.kotlin_assignment_eighteen.databinding.ActivityHistoryUserBinding
import com.example.kotlin_assignment_eighteen.model.DataItemUser
import com.example.kotlin_assignment_eighteen.model.GetAllUserResponse
import com.example.kotlin_assignment_eighteen.network.NetworkConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getAllListUsers()
    }

    private fun getAllListUsers() {
        NetworkConfig().getServiceUser()
            .getAllUsers()
            .enqueue(object: Callback<GetAllUserResponse> {
                override fun onResponse(
                    call: Call<GetAllUserResponse>,
                    response: Response<GetAllUserResponse>
                ) {
                    binding.rvHistoryUser.layoutManager = LinearLayoutManager(this@HistoryUserActivity)
                    binding.rvHistoryUser.adapter = ListUserAdapter(response.body()?.data as List<DataItemUser>)
                }

                override fun onFailure(call: Call<GetAllUserResponse>, t: Throwable) {
                    Toast.makeText(this@HistoryUserActivity,
                        t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

            })
    }
}