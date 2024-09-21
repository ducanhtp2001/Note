package com.example.note

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.note.ApiService.ApiClient
import com.example.note.Data.model.SinhVien
import com.example.note.Data.model.TaiKhoan
import com.example.note.databinding.ActivityRegisterBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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


class RegisterActivity : AppCompatActivity() {

    var hasStudent: Boolean = false
    
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(LayoutInflater.from(this))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.login.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        })

        binding.edStudentid.setOnEditorActionListener(OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT) {
                if (binding.edStudentid.getText().toString().isEmpty()) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Vui lòng nhập mã sinh viên",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@OnEditorActionListener true
                }
                val logging = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                val client = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build()
                val mediaType: MediaType =  "application/json; charset=utf-8".toMediaType()
                val idSinhVien = binding.edStudentid.getText().toString()
                val id = SinhVien.getIdFromMaSinhVien(
                    binding.edStudentid.getText().toString().trim { it <= ' ' })
                val khoa = SinhVien.getKhoaFromMaSinhVien(idSinhVien)
                val nienKhoa = SinhVien.getNienKhoaFromMaSinhVien(idSinhVien)
                val lop = SinhVien.getLopFromMaSinhVien(idSinhVien)

                val jsonObject = JsonObject()
                jsonObject.addProperty("idSinhVien", id)
                jsonObject.addProperty("khoa", khoa)
                jsonObject.addProperty("nienKhoa", nienKhoa)
                jsonObject.addProperty("lop", lop)

                val json = jsonObject.toString()

                Log.e("bug", "json: $json")

                val request: Request = Request.Builder()
                    .url(ApiClient.SERVER_URL + "checkRegister.php")
                    .post(RequestBody.create(mediaType, json))
                    .build()
                try {
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.e("bug", "Network Error")
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {
                            val json = response.body!!.string()

                            runOnUiThread {
                                val gson = Gson()
                                val jsonElement = gson.fromJson(json, JsonElement::class.java)
                                val jsonObject = jsonElement.asJsonObject
                                var tenSinhVien: String? = ""
                                var status = false
                                if (jsonObject.has("tenSinhVien")) {
                                    tenSinhVien = jsonObject["tenSinhVien"].asString
                                }
                                if (jsonObject.has("status")) {
                                    status = jsonObject["status"].asBoolean
                                }
                                if (status) {
                                    binding.tvFullname.setText(tenSinhVien)
                                    hasStudent = true
                                } else {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Sinh viên không tồn tại",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.tvFullname.setText(tenSinhVien)
                                    hasStudent = false
                                }
                            }
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return@OnEditorActionListener true
            }
            false
        })
        val showPasswordCheckBox = findViewById<CheckBox>(R.id.showPasswordCheckBox)
        showPasswordCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Show password
                binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                binding.passwordConfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
            } else {
                // Hide password
                binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance())
                binding.passwordConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance())
            }
            // Move cursor to the end of the text
            binding.password.setSelection(binding.password.getText().length)
            binding.passwordConfirm.setSelection(binding.passwordConfirm.getText().length)
        }

        binding.edStudentid.onFocusChangeListener = OnFocusChangeListener { view, b ->
            if (!b) {
                // Xử lý khi EditText bị mất focus (chuyển sang thành phần khác)
                if (binding.edStudentid.getText().toString().isEmpty()) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Vui lòng nhập mã sinh viên",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@OnFocusChangeListener
                }
                val logging = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                val client = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build()
                val mediaType: MediaType =  "application/json; charset=utf-8".toMediaType()
                val idSinhVien = binding.edStudentid.getText().toString()
                val id = SinhVien.getIdFromMaSinhVien(
                    binding.edStudentid.getText().toString().trim { it <= ' ' })
                val khoa = SinhVien.getKhoaFromMaSinhVien(idSinhVien)
                val nienKhoa = SinhVien.getNienKhoaFromMaSinhVien(idSinhVien)
                val lop = SinhVien.getLopFromMaSinhVien(idSinhVien)

                // Tạo đối tượng JSON
                val jsonObject = JsonObject()
                jsonObject.addProperty("idSinhVien", id)
                jsonObject.addProperty("khoa", khoa)
                jsonObject.addProperty("nienKhoa", nienKhoa)
                jsonObject.addProperty("lop", lop)

                // Chuyển đối tượng JSON thành chuỗi
                val json = jsonObject.toString()

                Log.e("bug", "json: $json")
                //                    binding.tvFullname.setText(json);
    //                    RequestBody requestBody = RequestBody.create(mediaType, json);
                val request: Request = Request.Builder()
                    .url(ApiClient.SERVER_URL + "checkRegister.php")
                    .post(RequestBody.create(mediaType, json))
                    .build()
                try {
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.e("bug", "Network Error")
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {
                            // Lấy thông tin JSON trả về. Bạn có thể log lại biến json này để xem nó như thế nào.
                            val json = response.body!!.string()

                            runOnUiThread { //                                    Toast.makeText(RegisterActivity.this, json, Toast.LENGTH_SHORT).show();
                                val gson = Gson()
                                val jsonElement = gson.fromJson(json, JsonElement::class.java)
                                val jsonObject = jsonElement.asJsonObject
                                var tenSinhVien: String? = ""
                                var status = false
                                if (jsonObject.has("tenSinhVien")) {
                                    tenSinhVien = jsonObject["tenSinhVien"].asString
                                }
                                if (jsonObject.has("status")) {
                                    status = jsonObject["status"].asBoolean
                                }
                                if (status) {
                                    binding.tvFullname.setText(tenSinhVien)
                                    hasStudent = true
                                } else {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "Sinh viên không tồn tại",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.tvFullname.setText(tenSinhVien)
                                    hasStudent = false
                                }
                            }
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


        binding.createAccount.setOnClickListener(View.OnClickListener {
            //                Toast.makeText(RegisterActivity.this, "" + hasStudent, Toast.LENGTH_SHORT).show();
            if (!hasStudent) {
                Toast.makeText(this@RegisterActivity, "Sinh viên không tồn tại", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }
            if (binding.edStudentid.getText().toString().isEmpty() ||
                binding.password.getText().toString().isEmpty() ||
                binding.passwordConfirm.getText().toString().isEmpty()
            ) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Không được để trông các trường",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            try {
                if (binding.password.getText().toString() == binding.passwordConfirm.getText().toString()) {
                    if (!checkPass(binding.password.getText().toString())) return@OnClickListener

                    val tk = TaiKhoan(
                        SinhVien.getIdFromMaSinhVien(
                            binding.edStudentid.getText().toString()
                        ),
                        binding.edStudentid.getText().toString().uppercase(Locale.getDefault()),
                        binding.password.getText().toString()
                    )

                    val logging = HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                    val client = OkHttpClient.Builder()
                        .addInterceptor(logging)
                        .build()
                    val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
                    val gson = GsonBuilder().create()
                    val json = gson.toJson(tk)
                    val requestBody = RequestBody.create(mediaType, json)
                    val request: Request = Request.Builder()
                        .url(ApiClient.SERVER_URL + "register.php")
                        .post(requestBody)
                        .build()
                    client.newCall(request).enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            Log.e("Error", "Network Error")
                        }

                        @Throws(IOException::class)
                        override fun onResponse(call: Call, response: Response) {
                            // Lấy thông tin JSON trả về. Bạn có thể log lại biến json này để xem nó như thế nào.
                            val json = response.body!!.string()

                            runOnUiThread { //                                    Toast.makeText(RegisterActivity.this, json, Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_LONG)
                                        .show()
                                    finish()
                                } else Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    })
                } else Toast.makeText(
                    this@RegisterActivity,
                    "Mật khẩu nhập lại không chính xác", Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    private fun checkPass(str: String): Boolean {
        var isHasNumber = false
        var isHasLetter = false
        if (str.length < 6) {
            Toast.makeText(this, "password at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        for (i in 0 until str.length) {
            if (Character.isDigit(str[i])) {
                isHasNumber = true
                continue
            }
            if (Character.isLetter(str[i])) {
                isHasLetter = true
                continue
            }
        }
        if (isHasNumber && isHasLetter) return true
        Toast.makeText(this, "Password must contain letters and numbers", Toast.LENGTH_SHORT).show()
        return false
    }
}