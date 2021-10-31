package com.example.libros_fly.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.libros_fly.MainActivity
import com.example.libros_fly.R


internal class LoginActivity : AppCompatActivity() {

    //private lateinit var auth : FirebaseAuth;
    lateinit var progressBarLogin: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnRegistro = this.findViewById<Button>(R.id.btnRegistrar)
        val btnInicio = this.findViewById<Button>(R.id.btnInicioSesion)
        val txtEmail = this.findViewById<EditText>(R.id.etxtEmail)
        val txtPassword = this.findViewById<EditText>(R.id.etxtPWD)
        progressBarLogin = this.findViewById<ProgressBar>(R.id.progressbarLogin)

        btnRegistro.setOnClickListener(View.OnClickListener {
            if (txtEmail.text.toString().isNotEmpty() && txtPassword.text.toString().isNotEmpty()) {

                progressBarLogin.setVisibility(View.VISIBLE)

                progressBarLogin.setVisibility(View.GONE)
                //pasamos al home de la aplicacion
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()


            } else {
                Toast.makeText(this, "Rellene los campos!", Toast.LENGTH_SHORT).show()
            }
        })

        btnInicio.setOnClickListener(View.OnClickListener {
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            val registrar = RegistrarFragment()
            transaction.replace(R.id.constraintLayout, registrar)
            transaction.addToBackStack(null)
            transaction.commit()

        })

    }
}