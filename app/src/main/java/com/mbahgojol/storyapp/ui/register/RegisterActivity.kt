package com.mbahgojol.storyapp.ui.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.mbahgojol.storyapp.R
import com.mbahgojol.storyapp.data.model.RegisterBody
import com.mbahgojol.storyapp.databinding.ActivityRegisterBinding
import com.mbahgojol.storyapp.utils.ResultState
import com.mbahgojol.storyapp.utils.text
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnLogin.setOnClickListener {
                finish()
            }

            btnRegister.setOnClickListener {
                val nama = etNama.text()
                val email = etEmail.text()
                val password = etPassword.text()

                when {
                    nama.isEmpty() -> {
                        etNama.error = "Silahkan Isi Nama Dulu"
                    }

                    email.isEmpty() -> {
                        etEmail.error = "Silahkan Isi Email Dulu"
                    }

                    password.isEmpty() -> {
                        etPassword.error = "Silahkan Isi Password Dulu"
                    }
                    else -> {
                        viewModel.register(RegisterBody(email, nama, password))
                    }
                }
            }
        }

        viewModel.resultStateRegister.observe(this) {
            when (it) {
                is ResultState.Error -> {
                    val snackbar = Snackbar.make(
                        binding.root,
                        it.e.message.toString(),
                        Snackbar.LENGTH_LONG
                    )
                    snackbar.show()
                }
                is ResultState.Loading -> {
                    binding.progressBar.isVisible = it.loading
                }
                is ResultState.Success<*> -> {
                    Toast.makeText(this, getString(R.string.sukses_register), Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }
        }
    }
}