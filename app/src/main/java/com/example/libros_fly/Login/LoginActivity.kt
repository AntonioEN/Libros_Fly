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
import com.example.libros_fly.clases.Usuario
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

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
        val btnInicioGoogle = this.findViewById<Button>(R.id.btnInicioSesionGoogle)

        //pedirMultiplesPermisos()

        progressBarLogin = this.findViewById<ProgressBar>(R.id.progressbarLogin)
        auth = FirebaseAuth.getInstance()


        //Al pulsar el boton de 'Iniciar Sesion', se comprobaran un par de cosas
        btnInicio.setOnClickListener(View.OnClickListener {
            //Se comprobara que los campos no esten vacios
            if (txtEmail.text.toString().isNotEmpty() && txtPassword.text.toString().isNotEmpty()) {

                progressBarLogin.setVisibility(View.VISIBLE)

                auth.signInWithEmailAndPassword(
                    txtEmail.text.toString(),
                    txtPassword.text.toString()
                ).addOnCompleteListener {
                    //Si no lo esta, el usuario podra iniciar perfectamente
                    //Si no estan vacios, pero al iniciar sesion falla se le comunicara que algun dato
                    // de los introduccidos esta mal
                    if(it.isSuccessful){

                        progressBarLogin.setVisibility(View.GONE)

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else{
                        Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show()
                        progressBarLogin.setVisibility(View.GONE)
                    }

                }.addOnFailureListener {
                    Toast.makeText(this, "Revisa la conexion", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Rellena los campos", Toast.LENGTH_SHORT).show()
            }
        })

        //Al pulsar el boton 'Registrarse', llevara al usuario al registrarse (RegistrarFragment)
        btnRegistro.setOnClickListener(View.OnClickListener {
            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            val registrar = RegistrarFragment()
            transaction.replace(R.id.constraintLayout, registrar)
            transaction.addToBackStack(null)
            transaction.commit()
        })

        //Al pulsar el boton 'Iniciar sesion con google', le llevara a una ventana emergente para loguearse con google
        btnInicioGoogle.setOnClickListener(View.OnClickListener{

            val googleConf =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
                    getString(
                        R.string.default_web_client_id_
                    )
                ).requestEmail().build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        })
    }

    //Al loguearse con google, se tendra que recoger los datos de ese usuario para guardarlo en la base de datos
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                progressBarLogin.setVisibility(View.VISIBLE)
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val user = FirebaseAuth.getInstance().currentUser
                                val random = (0..100000).random()
                                val nickname = user?.displayName.toString()+ random.toString()
                                val foto = user?.photoUrl.toString()
                                val nombre = user?.displayName.toString()
                                val id = user?.uid.toString()
                                val correo = user?.email.toString()
                                val usu = Usuario(nombre,correo, id, nickname)

                                progressBarLogin.setVisibility(View.GONE)
                                FirebaseAuth.getInstance().currentUser?.let { it1 ->
                                    FirebaseDatabase.getInstance("https://librosfly-default-rtdb.europe-west1.firebasedatabase.app/")
                                        .getReference("usuarios").child(
                                            it1.uid
                                        ).setValue(usu).addOnCompleteListener {

                                        }
                                }

                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()

                            } else {
                                progressBarLogin.setVisibility(View.GONE)
                            }
                        }

                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Error, revisa tu internet", Toast.LENGTH_SHORT).show()
            }

        }
    }
}