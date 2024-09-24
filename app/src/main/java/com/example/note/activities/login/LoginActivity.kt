package com.example.note.activities.login

import android.content.Intent
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.note.activities.forgot.ForgetPassActivity
import com.example.note.activities.home.MainActivity
import com.example.note.R
import com.example.note.activities.register.RegisterActivity
import com.example.note.base.BaseActivity
import com.example.note.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override val viewModel: LoginViewModel by viewModels()
    override val bindingInflater: (LayoutInflater) -> ActivityLoginBinding = { inflater ->
        ActivityLoginBinding.inflate(inflater)
    }

    override fun viewDidLoad() {
        super.viewDidLoad()
        viewModel.getLastLogin()
    }

    override fun setupViewEvents() {
        super.setupViewEvents()

        binding.showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.lPassword.transformationMethod = SingleLineTransformationMethod.getInstance()
            } else {
                binding.lPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        binding.createAccount.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.forgotPasword.setOnClickListener{
            val intent = Intent(this@LoginActivity, ForgetPassActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            val username = binding.lIdStuden.text.toString()
            val password = binding.lPassword.text.toString()
            viewModel.login(username, password)
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        viewModel.lastLogin.bindTo {
            binding.lIdStuden.setText(it?.taiKhoan ?: "")
            binding.lPassword.setText(it?.matKhau ?: "")
        }

        viewModel.loginSuccess.bindTo {  loginStatus ->
            if (loginStatus) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim)
                startActivity(intent)
            }
        }
    }
}
