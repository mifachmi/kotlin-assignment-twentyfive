package com.example.kotlin_assignment_eighteen.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.kotlin_assignment_eighteen.const.Constants.Companion.FCM_TOKEN
import com.example.kotlin_assignment_eighteen.databinding.ActivityLoginBinding
import com.example.kotlin_assignment_eighteen.model.GetAllUserResponse
import com.example.kotlin_assignment_eighteen.network.NetworkConfig
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
//    private var resultDeviceId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executor = ContextCompat.getMainExecutor(this)
        sharedPref = getSharedPreferences("GENERAL_KEY", Context.MODE_PRIVATE)
        tokenFirebase()
        checkLogin()
        getDeviceIdManual()
        configureBiometric(executor)

        binding.btnJoinGuest.setOnClickListener { onClickButtonJoinAsGuest() }
        binding.btnLogin.setOnClickListener { onClickButtonLogin() }
    }

    private fun configureBiometric(executor: Executor) {
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    val tvDeviceId = binding.tvDeviceID.text.toString()
                    val getDeviceId: String = checkDeviceId(tvDeviceId)

                    Log.d("TAG get", getDeviceId)
                    Log.d("TAG tv", tvDeviceId)

                    if (getDeviceId == tvDeviceId) {
                        Toast.makeText(
                            applicationContext,
                            "Authentication succeeded!", Toast.LENGTH_SHORT
                        ).show()
                        getInfoLoginBiometric(tvDeviceId)
                    } else {
                        Log.d("TAG get fail", getDeviceId)
                        Log.d("TAG tv fail", tvDeviceId)
                        Toast.makeText(
                            applicationContext,
                            "Device Id Unknown", Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        binding.btnBiometric.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun checkDeviceId(tvDeviceId: String): String{
        var resultDeviceId: String = "lalala"
        Log.d("TAG CDI awal", tvDeviceId)
        NetworkConfig().getServiceUser().checkDeviceId(tvDeviceId)
            .enqueue(object : Callback<GetAllUserResponse> {
                override fun onResponse(
                    call: Call<GetAllUserResponse>,
                    response: Response<GetAllUserResponse>
                ) {
                    if (response.body()?.status == 1) {
                        resultDeviceId = response.body()?.data?.get(0)?.device_id.toString()
                        Log.d("TAG CDI res", resultDeviceId)
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            response.body()?.status.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
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
        Log.d("TAG CDI return", resultDeviceId)
        Log.d("TAG CDI return", tvDeviceId)
        return tvDeviceId
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceIdManual() {
        val idDevice = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        binding.tvDeviceID.text = idDevice
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

    private fun checkLogin() {
        if (this.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun isLoggedIn(): Boolean {
        return this.sharedPref.getBoolean("IS_LOGIN", false)
    }

    private fun onClickButtonLogin() {
        with(binding) {
            if (etEmail.text.isNullOrEmpty() || etPassword.text.isNullOrEmpty()) {
                Toast.makeText(
                    this@LoginActivity,
                    "Can't login, must be filled",
                    Toast.LENGTH_SHORT
                ).show()
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
            .enqueue(object : Callback<GetAllUserResponse> {
                override fun onResponse(
                    call: Call<GetAllUserResponse>,
                    response: Response<GetAllUserResponse>
                ) {
                    if (response.body()?.status == 0) {
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
                        finish()
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

    private fun getInfoLoginBiometric(tvDeviceId: String) {
        NetworkConfig().getServiceUser()
            .checkLoginBiometric(tvDeviceId)
            .enqueue(object : Callback<GetAllUserResponse> {
                override fun onResponse(
                    call: Call<GetAllUserResponse>,
                    response: Response<GetAllUserResponse>
                ) {
                    if (response.body()?.status == 0) {
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
                        finish()
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