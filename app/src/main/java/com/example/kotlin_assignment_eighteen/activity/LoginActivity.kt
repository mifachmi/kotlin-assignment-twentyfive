package com.example.kotlin_assignment_eighteen.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlin_assignment_eighteen.R
import com.example.kotlin_assignment_eighteen.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnJoinGuest.setOnClickListener { onClickButtonJoinAsGuest() }
    }

    private fun onClickButtonJoinAsGuest() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}