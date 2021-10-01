package com.example.kotlin_assignment_eighteen.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_assignment_eighteen.databinding.ActivityLoginBinding
import com.example.kotlin_assignment_eighteen.model.GetAllUserResponse
import com.example.kotlin_assignment_eighteen.network.NetworkConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = getSharedPreferences("GENERAL_KEY", Context.MODE_PRIVATE)
        checkLogin()

        binding.btnJoinGuest.setOnClickListener { onClickButtonJoinAsGuest() }
        binding.btnLogin.setOnClickListener { onClickButtonLogin() }
    }

    private fun checkLogin() {
        if (this.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun isLoggedIn(): Boolean {
        return this.sharedPref.getBoolean("IS_LOGIN", false)
    }

    private fun onClickButtonLogin() {
        with(binding) {
            if (etEmail.text.isNullOrEmpty() || etPassword.text.isNullOrEmpty()) {
                Toast.makeText(this@LoginActivity, "Can't login, must be filled", Toast.LENGTH_SHORT).show()
            } else {
                getInfoLogin()
            }
        }
    }

    private fun getInfoLogin() {
        NetworkConfig().getServiceUser()
            .checkLogin(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
            .enqueue(object : Callback<GetAllUserResponse>{
                override fun onResponse(
                    call: Call<GetAllUserResponse>,
                    response: Response<GetAllUserResponse>
                ) {
                    if(response.body()?.status == 0) {
                        Toast.makeText(
                            this@LoginActivity,
                            response.body()!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Selamat Datang ${response.body()?.data?.get(0)?.nameUser}",
                            Toast.LENGTH_SHORT
                        ).show()
                        val name = response.body()?.data?.get(0)?.nameUser.toString()
                        val email = response.body()?.data?.get(0)?.emailUser.toString()
                        val password = response.body()?.data?.get(0)?.password.toString()

                        saveLoginSession(name, email, password)

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<GetAllUserResponse>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity,
                        t.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }

    private fun saveLoginSession(name: String, email: String, password: String) {
        val editor = sharedPref.edit()
        editor.putBoolean("IS_LOGIN", true)
        editor.putString("NAME", name)
        editor.putString("EMAIL", email)
        editor.putString("PASSWORD", password)
        editor.apply()
    }

    private fun onClickButtonJoinAsGuest() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}