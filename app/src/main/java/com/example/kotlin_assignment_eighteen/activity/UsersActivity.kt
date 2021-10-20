package com.example.kotlin_assignment_eighteen.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.kotlin_assignment_eighteen.R
import com.example.kotlin_assignment_eighteen.const.Constants.Companion.FILE_PATH
import com.example.kotlin_assignment_eighteen.databinding.ActivityUsersBinding
import com.example.kotlin_assignment_eighteen.model.*
import com.example.kotlin_assignment_eighteen.network.NetworkConfig
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UsersActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityUsersBinding
    private var imageName = ""
    private var tvIdDevice = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDefaultImage()
        getDeviceIdManual()
        binding.btnUploadImage.setOnClickListener { onClickUploadImage() }
        binding.btnCreateUser.setOnClickListener { onClickCreateUser() }
    }

    override fun onResume() {
        super.onResume()
        setLayoutDeviceId()
    }

    private fun setLayoutDeviceId() {
        if (!binding.tvIdUserActivity.text.equals("")) {
            NetworkConfig().getServiceUser().checkDeviceIdByUserId(binding.tvIdUserActivity.text.toString())
                .enqueue(object : Callback<GetAllUserResponse> {
                    override fun onResponse(
                        call: Call<GetAllUserResponse>,
                        response: Response<GetAllUserResponse>
                    ) {
                        tvIdDevice = response.body()?.data?.get(0)?.device_id.toString()
                        Log.d("CDI res", tvIdDevice)
                        binding.boxDeviceId.visibility = View.VISIBLE
                        binding.etDeviceID.setText(tvIdDevice)
                    }

                    override fun onFailure(call: Call<GetAllUserResponse>, t: Throwable) {
                        Log.e("CDI", t.message.toString())
                    }
                })
        }
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceIdManual() {
        val idDevice = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        binding.tvDeviceID.text = idDevice.toString()
    }

    private fun onClickCreateUser() {
        with(binding) {
            if (civProfilePhoto.equals(DEFAULT_IMAGE) || etFullName.text.isNullOrEmpty() ||
                etEmail.text.isNullOrEmpty() || etPassword.text.isNullOrEmpty()
            ) {
                Toast.makeText(this@UsersActivity, "Form must be filled", Toast.LENGTH_SHORT).show()
            } else {

                val tvDeviceId = binding.etDeviceID.text.toString()
                val getDeviceId = checkDeviceId(tvDeviceId)

                Log.d("TAG 1", tvDeviceId)
                Log.d("TAG 1", getDeviceId)

                if (tvIdUserActivity.text.equals("")) {
                    insertUser()
                    resetForm()
                } else {

                    updateUser(tvIdUserActivity.text.toString())
                    binding.boxDeviceId.visibility = View.GONE
                    resetForm()

//                    if (tvDeviceId == getDeviceId) {
//                        Log.d("TAG 2", tvDeviceId)
//                        Log.d("TAG 2", getDeviceId)
//                        Toast.makeText(this@UsersActivity, "Device Id already taken by other account", Toast.LENGTH_SHORT).show()
//                    } else {
//                        updateUser(tvIdUserActivity.text.toString())
//                        resetForm()
//                    }
                }

//                if (tvDeviceId == getDeviceId) {
//                    Toast.makeText(this@UsersActivity, "Device Id already taken by other account", Toast.LENGTH_SHORT).show()
//                } else {
//                    updateUser(tvIdUserActivity.text.toString())
//                    resetForm()
//                }

//                if (tvIdUserActivity.text.equals("")) {
//                    insertUser()
//                    resetForm()
//                } else {
//                    updateUser(tvIdUserActivity.text.toString())
//                    resetForm()
//                }
            }
        }
    }

    private fun checkDeviceId(tvDeviceId: String): String {
        val resultDeviceId = Device()
        NetworkConfig().getServiceUser().checkDeviceId(tvDeviceId)
            .enqueue(object : Callback<GetAllUserResponse> {
                override fun onResponse(
                    call: Call<GetAllUserResponse>,
                    response: Response<GetAllUserResponse>
                ) {
                    resultDeviceId.deviceId = response.body()?.data?.get(0)?.device_id.toString()
                    Log.d("CDI res", resultDeviceId.deviceId)
                }

                override fun onFailure(call: Call<GetAllUserResponse>, t: Throwable) {
                    Log.e("CDI", t.message.toString())
                }

            })
        Log.d("CDI return", resultDeviceId.deviceId)
        return resultDeviceId.deviceId
    }

    private fun insertUser() {
        NetworkConfig().getServiceUser()
            .insertUser(
                id = "",
                binding.etFullName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                photo_user = imageName,
                device_id = "NULL"
            )
            .enqueue(object : Callback<CreateDataResponse> {
                override fun onResponse(
                    call: Call<CreateDataResponse>,
                    response: Response<CreateDataResponse>
                ) {
                    Toast.makeText(this@UsersActivity, "Insert Data Succeed", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onFailure(call: Call<CreateDataResponse>, t: Throwable) {
                    Toast.makeText(this@UsersActivity, "Insert Data Failed", Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }

    private fun updateUser(id: String) {
        Toast.makeText(this@UsersActivity, "Save ketika id = $id", Toast.LENGTH_SHORT)
            .show()
        NetworkConfig().getServiceUser()
            .updateUser(
                binding.etFullName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                photo_user = imageName,
                binding.etDeviceID.text.toString(),
                id
            )
            .enqueue(object : Callback<UpdateDataResponse> {
                override fun onResponse(
                    call: Call<UpdateDataResponse>,
                    response: Response<UpdateDataResponse>
                ) {
                    Toast.makeText(this@UsersActivity, "Update Data Succeed", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onFailure(call: Call<UpdateDataResponse>, t: Throwable) {
                    Toast.makeText(this@UsersActivity, "Update Data Failed", Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == HistoryUserActivity.RESULT_CODE && result.data != null) {
            val dataParcelable =
                result.data!!.getParcelableExtra<DataItemUser>(HistoryUserActivity.PARCEL_DATA_USER)

            binding.apply {
                tvIdUserActivity.text = dataParcelable?.id
                etFullName.setText(dataParcelable?.nameUser)
                etEmail.setText(dataParcelable?.emailUser)
                etPassword.setText(dataParcelable?.password)
                Glide.with(applicationContext)
                    .load(FILE_PATH + dataParcelable?.photoUser)
                    .into(civProfilePhoto)
            }
        }
    }

    fun deleteUser(context: Context, id: String) {
        NetworkConfig().getServiceUser()
            .deleteUser(id)
            .enqueue(object : Callback<DeleteDataResponse> {
                override fun onResponse(
                    call: Call<DeleteDataResponse>,
                    response: Response<DeleteDataResponse>
                ) {
                    Toast.makeText(
                        context.applicationContext,
                        "Berhasil Menghapus User $id", Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }

                override fun onFailure(call: Call<DeleteDataResponse>, t: Throwable) {
                    Toast.makeText(this@UsersActivity, t.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun onClickUploadImage() {
        if (EasyPermissions.hasPermissions(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            val intentOpenGallery = Intent(Intent.ACTION_PICK)
            intentOpenGallery.type = "image/"
            startActivityForResult(intentOpenGallery, REQUEST_IMAGE)

        } else {
            EasyPermissions.requestPermissions(
                this, "Izinkan Aplikasi Mengakses Storage?",
                REQUEST_IMAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    @SuppressLint("Recycle")
    private fun getRealPathFromURIPath(contentURI: Uri?, activity: Activity): String {
        val cursor: Cursor? = activity.contentResolver.query(contentURI!!, null, null, null, null)

        return if (cursor == null) {
            contentURI.path.toString()
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(index)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            val uri: Uri? = data?.data
            uploadImage(uri)
        }
    }

    private fun uploadImage(contentURI: Uri?) {
        val filePath = getRealPathFromURIPath(contentURI, this)
        val file = File(filePath)
        Log.d("File Upload Image", file.name)

        val mFile: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body: MultipartBody.Part = createFormData("file", file.name, mFile)

        NetworkConfig().getServiceUser()
            .uploadImage(body)
            .enqueue(object : Callback<UploadImageResponse> {
                override fun onResponse(
                    call: Call<UploadImageResponse>,
                    response: Response<UploadImageResponse>
                ) {
                    if (response.body()?.status == 0) {
                        Toast.makeText(
                            this@UsersActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        imageName = response.body()!!.file_path?.trim().toString()
                        Toast.makeText(
                            this@UsersActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Glide.with(applicationContext)
                            .load(FILE_PATH + imageName)
                            .into(binding.civProfilePhoto)
                    }
                }

                override fun onFailure(call: Call<UploadImageResponse>, t: Throwable) {
                    Toast.makeText(this@UsersActivity, t.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.history_user_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemHistoryUser -> {
                val intentToAddAccount = Intent(this, HistoryUserActivity::class.java)
                resultLauncher.launch(intentToAddAccount)
                true
            }
            else -> true
        }
    }

    private fun setDefaultImage() {
        Glide.with(applicationContext)
            .load(DEFAULT_IMAGE)
            .into(binding.civProfilePhoto)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == REQUEST_IMAGE) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == REQUEST_IMAGE) {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetForm() {
        setDefaultImage()
        with(binding) {
            tvIdUserActivity.text = ""
            etFullName.text = null
            etEmail.text = null
            etPassword.text = null
        }
    }

    companion object {
        const val REQUEST_IMAGE = 100
        const val DEFAULT_IMAGE =
            "https://t4.ftcdn.net/jpg/00/64/67/63/360_F_64676383_LdbmhiNM6Ypzb3FM4PPuFP9rHe7ri8Ju.jpg"
    }
}