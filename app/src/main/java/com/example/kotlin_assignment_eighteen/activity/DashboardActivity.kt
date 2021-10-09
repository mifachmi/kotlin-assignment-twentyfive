package com.example.kotlin_assignment_eighteen.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_assignment_eighteen.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cvDashboard.setOnClickListener { onClickCvDashboard() }
        binding.cvSendNotification.setOnClickListener { onClickCvNotification() }
    }

    private fun onClickCvNotification() {
        val intent = Intent(this, FormCloudMessagingActivity::class.java)
        startActivity(intent)
    }

    private fun onClickCvDashboard() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}