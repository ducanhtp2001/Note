package com.example.note.activities.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.note.ApiService.ApiClient
import com.example.note.ForgetPassActivity
import com.example.note.MainActivity
import com.example.note.R
import com.example.note.activities.register.RegisterActivity
import com.example.note.databinding.ActivityLoginBinding
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.Locale

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private val idSinhVienStr: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

//        binding.createAccount = findViewById(R.id.createAccount)
//        binding.loginBtn = findViewById(R.id.binding.loginBtn)
//        binding.lIdStuden = findViewById(R.id.binding.lIdStuden)
//        binding.lPassword = findViewById(R.id.binding.lPassword)
//        binding.forgotPasword = findViewById(R.id.binding.forgotPasword)

        

        binding.loginBtn.setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(binding.lIdStuden.getText().toString())) {
                binding.lIdStuden.setError("Vui lòng nhập họ tên")
                return@OnClickListener
            }
            if (TextUtils.isEmpty(binding.lPassword.getText().toString())) {
                binding.lPassword.setError("Vui lòng nhập Password")
                return@OnClickListener
            }

//            val spec: ConnectionSpec = Request.Builder(ConnectionSpec.MODERN_TLS)
//                .tlsVersions(TlsVersion.TLS_1_2)
//                .cipherSuites(
//                    TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
//                    TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
//                )
//                .build()

            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
            val idSinhVien = binding.lIdStuden.getText().toString().uppercase(Locale.getDefault())
            val password = binding.lPassword.getText().toString()

            // Tạo đối tượng JSON
            val jsonObject = JsonObject()
            jsonObject.addProperty("idSinhVien", idSinhVien)
            jsonObject.addProperty("password", password)

            // Chuyển đối tượng JSON thành chuỗi
            val json = jsonObject.toString()

            val request: Request = Request.Builder()
                .url(ApiClient.SERVER_URL + "login.php")
                .post(RequestBody.create(mediaType, json))
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("Error", "Network Error")
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    // Lấy thông tin JSON trả về. Bạn có thể log lại biến json này để xem nó như thế nào.
                    val json = response.body!!.string()

                    runOnUiThread {
                        val gson = Gson()
                        val jsonElement = gson.fromJson(json, JsonElement::class.java)
                        val jsonObject = jsonElement.asJsonObject
                        var message: String? = ""
                        var status = false
                        if (jsonObject.has("message")) {
                            message = jsonObject["message"].asString
                        }
                        if (jsonObject.has("status")) {
                            status = jsonObject["status"].asBoolean
                        }
                        if (status) {
                            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("ID_SINHVIEN", idSinhVien)
                            startActivity(intent)
                            overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim)
                        } else {
                            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        })

        val showPasswordCheckBox = findViewById<CheckBox>(R.id.showPasswordCheckBox)
        showPasswordCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Show password
                binding.lPassword.setTransformationMethod(SingleLineTransformationMethod.getInstance())
            } else {
                // Hide password
                binding.lPassword.setTransformationMethod(PasswordTransformationMethod.getInstance())
            }
        }

        binding.createAccount.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        })

        binding.forgotPasword.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LoginActivity, ForgetPassActivity::class.java)
            intent.putExtra("idSinhVien", idSinhVienStr)
            startActivity(intent)
        })
    }
}
