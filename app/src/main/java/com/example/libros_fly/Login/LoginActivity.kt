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
import com.google.firebase.auth.FirebaseAuth


internal class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val GOOGLE_SIGN_IN = 100
    lateinit var progressBarLogin: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(findViewById(R.id.toolbar))

        val btnRegistro = this.findViewById<Button>(R.id.btnRegistrar)
        val btnInicio = this.findViewById<Button>(R.id.btnInicioSesion)
        val txtEmail = this.findViewById<EditText>(R.id.etxtEmail)
        val txtPassword = this.findViewById<EditText>(R.id.etxtPWD)
        progressBarLogin = this.findViewById<ProgressBar>(R.id.progressbarLogin)
        auth = FirebaseAuth.getInstance()



        btnInicio.setOnClickListener(View.OnClickListener {
            if (txtEmail.text.toString().isNotEmpty() && txtPassword.text.toString().isNotEmpty()) {

                progressBarLogin.setVisibility(View.VISIBLE)

                auth.signInWithEmailAndPassword(
                    txtEmail.text.toString(),
                    txtPassword.text.toString()
                ).addOnCompleteListener {

                    if(it.isSuccessful){

                        //val per

                        progressBarLogin.setVisibility(View.GONE)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        Toast.makeText(this, "Datos incorrectos!", Toast.LENGTH_SHORT).show()
                        progressBarLogin.setVisibility(View.GONE)
                    }

                }.addOnFailureListener {
                    Toast.makeText(this, "Revisa la conexion!", Toast.LENGTH_SHORT).show()
                }
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