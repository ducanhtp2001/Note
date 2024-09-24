package com.example.note.activities.register

import android.content.Intent
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.note.R
import com.example.note.activities.login.LoginActivity
import com.example.note.base.BaseActivity
import com.example.note.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityRegisterBinding = { layoutInflater ->
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    override val viewModel: RegisterViewModel by viewModels()


    override fun setupViewEvents() {
        super.setupViewEvents()

        binding.edStudentid.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                view.text?.let {
                    if (it.isEmpty()) {
                        toaster.display(R.string.err_input_student_id_first)
                    } else {
                        viewModel.checkRegister(it.toString())
                    }
                }
            }

            true
        }

        binding.edStudentid.onFocusChangeListener = OnFocusChangeListener {
                view, hasFocus ->
            if (!hasFocus) {
                (view as? TextView)?.text?.let {
                    if (it.isEmpty()) {
                        toaster.display(R.string.err_input_student_id_first)
                    } else {
                        viewModel.checkRegister(it.toString())
                    }
                }
            }
        }

        binding.login.setOnClickListener {
            goToLogin()
        }

        binding.showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Show password
                binding.password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.passwordConfirm.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                // Hide password
                binding.password.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.passwordConfirm.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            // Move cursor to the end of the text
            binding.password.setSelection(binding.password.getText().length)
            binding.passwordConfirm.setSelection(binding.passwordConfirm.getText().length)
        }

        binding.createAccount.setOnClickListener {
            binding.edStudentid.text?.let {
                if (it.isEmpty()) {
                    toaster.display(R.string.err_input_student_id_first)
                    return@setOnClickListener
                } else {
                    viewModel.checkRegister(it.toString()) {
                        runOnUiThread {
                            if (viewModel.sinhVien.value.hoTen?.isEmpty() == true)
                                toaster.display(R.string.err_student_id_not_found)
                            else {
                                val password = binding.password.text.toString()
                                val passwordConfirm = binding.passwordConfirm.text.toString()

                                if (password == passwordConfirm) {
                                    if (checkPass(password)) {
                                        val sinhVien = viewModel.sinhVien.value
                                        viewModel.register(sinhVien.id, sinhVien.idStr, password)
                                    } else {
                                        toaster.display(R.string.err_password_invalid)
                                    }
                                } else {
                                    toaster.display(R.string.err_password_not_match)
                                }
                            }
                        }
                    }
                }


            }
        }

    }

    private fun goToLogin() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.sinhVien.bindTo {
            binding.tvFullname.text = it.hoTen
        }

        viewModel.registerSuccess.bindTo {
            it?.message?.let { msg ->
                toaster.display(msg)
            }
            if (it?.status == true) {
                goToLogin()
                finish()
            }
        }
    }

    override fun viewDidLoad() {

    }

    private fun checkPass(str: String): Boolean {
        var isHasNumber = false
        var isHasLetter = false
        if (str.length < 6) {
            Toast.makeText(this, "password at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        for (i in str.indices) {
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