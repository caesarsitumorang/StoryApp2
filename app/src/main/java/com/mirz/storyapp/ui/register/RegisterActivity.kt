package com.mirz.storyapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mirz.storyapp.Locator
import com.mirz.storyapp.R
import com.mirz.storyapp.databinding.ActivityRegisterBinding
import com.mirz.storyapp.ui.login.LoginActivity
import com.mirz.storyapp.utils.ResultState
import com.mirz.storyapp.utils.launchAndCollectIn

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<RegisterViewModel>(factoryProducer = { Locator.registerViewModelFactory })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        playAnimation()

        viewModel.registerViewState.launchAndCollectIn(this) {
            when (it.resultRegisterUser) {
                is ResultState.Success<String> -> {
                    binding.btRegister.setLoading(false)
                    Toast.makeText(
                        this@RegisterActivity,
                        getString(R.string.register_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(
                        Intent(
                            this@RegisterActivity, LoginActivity::class.java
                        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                }

                is ResultState.Loading -> binding.btRegister.setLoading(true)
                is ResultState.Error -> {
                    binding.btRegister.setLoading(false)
                    Toast.makeText(
                        this@RegisterActivity, it.resultRegisterUser.message, Toast.LENGTH_SHORT
                    ).show()
                }

                else -> Unit
            }
        }
        binding.btRegister.setOnClickListener {
            if (!binding.edRegisterName.text.isNullOrBlank() && !binding.edRegisterEmail.text.isNullOrBlank() && !binding.edRegisterPassword.text.isNullOrBlank()) {
                viewModel.registerUser(
                    name = binding.edRegisterName.text.toString(),
                    email = binding.edRegisterEmail.text.toString(),
                    password = binding.edRegisterPassword.text.toString()
                )
            } else {
                Toast.makeText(this, getString(R.string.input_invalid), Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvAlreadyHaveAnAccount.setOnClickListener {
            startActivity(
                Intent(
                    this, LoginActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        }
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvHello, View.ALPHA, 1f).setDuration(500)
        val create = ObjectAnimator.ofFloat(binding.tvCreateAccount, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val daftar = ObjectAnimator.ofFloat(binding.btRegister, View.ALPHA, 1f).setDuration(500)
        val text = ObjectAnimator.ofFloat(binding.tvAlreadyHaveAnAccount, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(daftar)
        }
        AnimatorSet().apply {
            playSequentially(title, create, name, email,password, together,text)
            start()
        }
    }
}