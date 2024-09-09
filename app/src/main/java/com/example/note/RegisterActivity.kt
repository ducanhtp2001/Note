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
import com.example.note.Model.SinhVien
import com.example.note.Model.TaiKhoan
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
//    var binding.edStudentid: EditText? = null
//    var binding.tvFullname: TextView? = null
//    var binding.password: EditText? = null
//    var binding.passwordConfirm: EditText? = null
//    var binding.createAccount: Button? = null
//    var tv_login: TextView? = null

    var hasStudent: Boolean = false
    
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(LayoutInflater.from(this))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

//        binding.edStudentid = findViewById(R.id.binding.edStudentid)
//        binding.tvFullname = findViewById(R.id.binding.tvFullname)
//        binding.password = findViewById(R.id.password)
//        binding.passwordConfirm = findViewById(R.id.passwordConfirm)
//        binding.createAccount = findViewById(R.id.createAccount)
//        tv_login = findViewById(R.id.login)

        binding.login.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        })

        binding.edStudentid.setOnEditorActionListener(OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_NEXT) {
                // Xử lý khi nhập xong (hoàn thành)
                if (binding.edStudentid.getText().toString().isEmpty()) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Vui lòng nhập mã sinh viên",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@OnEditorActionListener true
                }
                val client = OkHttpClient()
                val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
                val idSinhVien = binding.edStudentid.getText().toString()
                val id = SinhVien.getIdFromMaSinhVien(idSinhVien)
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

                //                    binding.tvFullname.setText(json);
//                    RequestBody requestBody = RequestBody.create(mediaType, json);
                val request: Request = Request.Builder()
                    .url("https://ttcs-test.000webhostapp.com/androidApi/checkRegister.php")
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
                            //                                    Toast.makeText(RegisterActivity.this, tenSinhVien + status, Toast.LENGTH_SHORT).show();
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

                return@OnEditorActionListener true // Trả về true để ngăn việc xử lý mặc định của EditText
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

        binding.edStudentid.setOnFocusChangeListener(OnFocusChangeListener { view, b ->
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

                //                    binding.tvFullname.setText(json);
//                    RequestBody requestBody = RequestBody.create(mediaType, json);
                val request: Request = Request.Builder()
                    .url("http://http://192.168.48.1//note/api//checkRegister.php")
                    .post(RequestBody.create(mediaType, json))
                    .build()
                try {
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
        })


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
            if (binding.password.getText().toString() == binding.passwordConfirm.getText().toString()) {
                if (!checkPass(binding.password.getText().toString())) return@OnClickListener

                val tk = TaiKhoan(
                    SinhVien.getIdFromMaSinhVien(binding.edStudentid.getText().toString()),
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
                    .url("https://ttcs-test.000webhostapp.com/androidApi/register.php")
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