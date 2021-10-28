package com.example.kotlin_assignment_eighteen.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_assignment_eighteen.const.Constants.Companion.FCM_TOKEN
import com.example.kotlin_assignment_eighteen.databinding.ActivityFormCloudMessagingBinding
import com.example.kotlin_assignment_eighteen.model.NotificationResponse
import com.example.kotlin_assignment_eighteen.network.NetworkConfig
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormCloudMessagingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormCloudMessagingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormCloudMessagingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tokenFirebase()

        binding.btnSendNotification.setOnClickListener { onClickBtnSendNotif() }
    }

    private fun tokenFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            FCM_TOKEN = task.result.toString()
            // Log and toast
            Log.d("TAG", FCM_TOKEN)
        })
    }

    private fun onClickBtnSendNotif() {
        val fcmToken = FCM_TOKEN
        val titleNotif = binding.etTitleNotification.text.toString()
        val bodyNotif = binding.etMessageNotification.text.toString()

        Log.d("TAG", fcmToken)

        if (fcmToken.isEmpty() || titleNotif.isEmpty() || bodyNotif.isEmpty()) {
            Toast.makeText(this, "invalid fcm token or empty title/message", Toast.LENGTH_SHORT).show()
        } else {
            NetworkConfig().getServiceNotification().pushNotification(
                titleNotif, bodyNotif
            ).enqueue(object: Callback<NotificationResponse> {
                override fun onResponse(
                    call: Call<NotificationResponse>,
                    response: Response<NotificationResponse>
                ) {
                    if (response.body()?.status == "1") {
                        Log.d("TAG", response.body().toString())
                        Toast.makeText(this@FormCloudMessagingActivity, "sukses push notifikasi", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("TAG", response.body()?.status.toString())
                        Toast.makeText(this@FormCloudMessagingActivity, "gagal push notifikasi ke fcm", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<NotificationResponse>, t: Throwable) {
                    Log.d("TAG", t.localizedMessage)
                    Toast.makeText(this@FormCloudMessagingActivity, "gagal push notifikasi ke fcm", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }
}