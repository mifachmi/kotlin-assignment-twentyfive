package com.example.kotlin_assignment_eighteen.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
            .enqueue(object : Callback<GetAllUserResponse> {
                override fun onResponse(
                    call: Call<GetAllUserResponse>,
                    response: Response<GetAllUserResponse>
                ) {
                    binding.rvHistoryUser.layoutManager =
                        LinearLayoutManager(this@HistoryUserActivity)
                    val btnEditUser = ListUserAdapter(response.body()?.data as List<DataItemUser>)
                    binding.rvHistoryUser.adapter = btnEditUser

                    btnEditUser.setOnItemClickCallback(object :
                        ListUserAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: DataItemUser) {
                            val editDataUserIntent = Intent()
                            editDataUserIntent.putExtra(PARCEL_DATA_USER, data)
                            setResult(RESULT_CODE, editDataUserIntent)
                            finish()
                        }
                    })
                }

                override fun onFailure(call: Call<GetAllUserResponse>, t: Throwable) {
                    Toast.makeText(
                        this@HistoryUserActivity,
                        t.localizedMessage, Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }

    companion object {
        const val PARCEL_DATA_USER = "PARCEL_DATA_USER"
        const val RESULT_CODE = 110
    }

}