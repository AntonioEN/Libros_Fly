package com.example.libros_fly.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.libros_fly.R

class LoginActivity : AppCompatActivity() {

    //private lateinit var auth : FirebaseAuth;
    lateinit var progressBarLogin: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = this.findViewById<Button>(R.id.btnRegistrar)
        val btnRegistro = this.findViewById<Button>(R.id.btnInicioSesion)
        val txtEmail = this.findViewById<EditText>(R.id.etxtEmail)
        val txtPassword = this.findViewById<EditText>(R.id.etxtPWD)
        progressBarLogin = this.findViewById<ProgressBar>(R.id.progressbarLogin)

        btnLogin.setOnClickListener(View.OnClickListener{
            if(txtEmail.text.toString().isNotEmpty() && txtPassword.text.toString().isNotEmpty()){

                progressBarLogin.setVisibility(View.VISIBLE)



            }else{
                Toast.makeText(this, "Rellene los campos!", Toast.LENGTH_SHORT).show()
            }
        })

        btnRegistro.setOnClickListener(View.OnClickListener {
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            val registrar = RegistrarFragment()
            transaction.replace(R.id.constraintLayout, registrar)
            transaction.addToBackStack(null)
            transaction.commit()

        })

    }
}