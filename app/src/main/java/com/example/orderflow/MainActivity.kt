package com.example.orderflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val login:TextView = findViewById(R.id.login_edit)
        val password:TextView = findViewById(R.id.password_edit)
        val login_button:Button = findViewById(R.id.login_button)

        login_button.setOnClickListener(){
                    val intent = Intent(this, WorkScreen::class.java)
                    startActivity(intent)
            }
        }


    }
