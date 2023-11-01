package com.mirz.storyapp.ui.login

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
import com.mirz.storyapp.databinding.ActivityLoginBinding
import com.mirz.storyapp.ui.register.RegisterActivity
import com.mirz.storyapp.ui.story.StoryActivity
import com.mirz.storyapp.utils.ResultState
import com.mirz.storyapp.utils.launchAndCollectIn

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<LoginViewModel>(factoryProducer = { Locator.loginViewModelFactory })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        playAnimation()

        viewModel.loginState.launchAndCollectIn(this) { state ->
            when (state.resultVerifyUser) {
                is ResultState.Success<String> -> {
                    binding.btLogin.setLoading(false)
                    startActivity(
                        Intent(
                            this@LoginActivity, StoryActivity::class.java
                        )
                    )
                    finish()
                }

                is ResultState.Loading -> binding.btLogin.setLoading(true)
                is ResultState.Error -> {
                    binding.btLogin.setLoading(false)
                    Toast.makeText(
                        this@LoginActivity, state.resultVerifyUser.message, Toast.LENGTH_SHORT
                    ).show()
                }

                else -> Unit
            }

        }

        binding.btLogin.setOnClickListener {
            if (!binding.edLoginEmail.text.isNullOrBlank() && !binding.edLoginPassword.text.isNullOrBlank()) {
                viewModel.doLogin(
                    email = binding.edLoginEmail.text.toString(),
                    password = binding.edLoginPassword.text.toString()
                )
            } else {
                Toast.makeText(this, getString(R.string.input_invalid), Toast.LENGTH_SHORT).show()
            }
        }


        binding.tvDonTHaveAnAccount.setOnClickListener {
            startActivity(
                Intent(
                    this, RegisterActivity::class.java
                )
            )
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvHello, View.ALPHA, 1f).setDuration(500)
        val welcome = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val btlogin = ObjectAnimator.ofFloat(binding.btLogin, View.ALPHA, 1f).setDuration(500)
        val text = ObjectAnimator.ofFloat(binding.tvDonTHaveAnAccount, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(btlogin)
        }
        AnimatorSet().apply {
            playSequentially(title, welcome, email, password,together,text)
            start()
        }
    }
}