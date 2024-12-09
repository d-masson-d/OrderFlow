package com.example.orderflow

import FirestoreRepository
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private lateinit var firestoreRepository: FirestoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        firestoreRepository = FirestoreRepository()

        val login: TextView = findViewById(R.id.login_edit)
        val password: TextView = findViewById(R.id.password_edit)
        val loginButton: Button = findViewById(R.id.login_button)
        val imageView: ImageView = findViewById(R.id.imageView)

        imageView.setImageResource(R.drawable.logo)


        loginButton.setOnClickListener {
            val login = login.text.toString().trim()
            val passwordText = password.text.toString().trim()

            if (login.isEmpty()) {
                Toast.makeText(this, "Введите логин", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (passwordText.isEmpty() || passwordText.length < 8) {
                Toast.makeText(this, "Введите пароль (не менее 8 символов)", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            firestoreRepository.loginUser (login, passwordText) { success, message ->
                if (success) {
                    val intent = Intent(this, WorkScreen::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, message ?: "Ошибка входа", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}