package com.example.libros_fly.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.libros_fly.MainActivity
import com.example.libros_fly.R
import com.example.libros_fly.clases.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.regex.Pattern

//Fragment para que el usuario se registre
class RegistrarFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var nickCreados: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var txtNombre : EditText
    private lateinit var txtPasswd : EditText
    private lateinit var txtCorreo : EditText
    private lateinit var txtNick : EditText



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_registrar, container, false)

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val btnRegistrarse = root.findViewById<Button>(R.id.btnRegistrarse)
         val txtNombre = root.findViewById<EditText>(R.id.etxtNombre)
         txtPasswd = root.findViewById<EditText>(R.id.etxtPassword)
         txtCorreo = root.findViewById<EditText>(R.id.editCorreo)
         txtNick = root.findViewById<EditText>(R.id.etxtNick)
        val btnVolver = root.findViewById<ImageButton>(R.id.btnVolver)



        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://librosfly-default-rtdb.europe-west1.firebasedatabase.app/")

        databaseReference = database.reference.child("usuarios")
        nickCreados = database.reference.child("UsuariosCreados")


        //Al pulsar el boton 'Registrarse', se comprobaran unos puntos para que se haga un registro completo y sin fallos
        btnRegistrarse.setOnClickListener(View.OnClickListener {

            txtNick.setBackgroundTintList(activity?.applicationContext?.let { it1 ->
                ContextCompat.getColorStateList(it1, R.color.white)
            })
            txtPasswd.setBackgroundTintList(activity?.applicationContext?.let { it1 ->
                ContextCompat.getColorStateList(it1, R.color.white)
            })
            txtCorreo.setBackgroundTintList(activity?.applicationContext?.let { it1 ->
                ContextCompat.getColorStateList(it1, R.color.white)
            })

            var nombre = txtNombre.text.toString()
            var contrasenna = txtPasswd.text.toString()
            var correo = txtCorreo.text.toString()
            var nick = txtNick.text.toString()

            //Se comprobara si el usuario ha rellenado todos los campos
             if(!correo.isEmpty() && !contrasenna.isEmpty() && !nombre.isEmpty() && !nick.isEmpty()){
                 if(comprobarCorreo(correo.trim())){
                     Toast.makeText(context,"Correo valido",Toast.LENGTH_SHORT).show()
                     txtCorreo.setBackgroundTintList(activity?.applicationContext?.let { it1 ->
                         ContextCompat.getColorStateList(it1, R.color.white)
                     })
                     registrarUsuario(nombre, correo, contrasenna, nick)
                 }else{
                     Toast.makeText(context,"Correo no valido",Toast.LENGTH_SHORT).show()
                     txtCorreo.setBackgroundTintList(activity?.applicationContext?.let { it1 ->
                         ContextCompat.getColorStateList(it1, R.color.rojo)
                     })
                 }

             }else{
                    Toast.makeText(context,"Rellena todos los datos",Toast.LENGTH_SHORT).show()
             }
        })

        //Boton que hara volver para atras al usuario, llevandolo de nuevo al LoginActivity
        btnVolver.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            activity?.startActivity(intent)
        })

        return root
    }

    //Metodo para registrar al usuario, donde se le pasara el nombre, correo, contrasenna y nick
    private fun registrarUsuario(nombre: String, correo: String, contrasenna: String, nick: String){
        //Se comprueba si el usuario esta creado, se comprobara mediante el nick
            nickCreados.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(!snapshot.hasChild(nick)){
                        Log.e("h",correo)
                        Log.e("h", contrasenna)
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, contrasenna).addOnCompleteListener()
                        {
                            Log.e("h",it.isSuccessful.toString())
                            //Si va bien, dejara crearlo, si no, sera porque al querrer crearlo, da fallos en el correo porque ya exista
                            // o porque la contrasenna tiene menos de 6 caracteres
                            if(it.isSuccessful){
                                val id = auth.uid.toString()
                                val usu = Usuario(nombre, correo, id, nick)
                                FirebaseAuth.getInstance().currentUser?.let { it1 ->
                                    FirebaseDatabase.getInstance("https://librosfly-default-rtdb.europe-west1.firebasedatabase.app/")
                                        .getReference("usuarios").child(it1.uid)
                                        .setValue(usu).addOnCompleteListener {
                                            if(it.isSuccessful){
                                                nickCreados.child(nick).setValue(id)
                                                val t = Toast.makeText(
                                                    context, "Usuario guardado", Toast.LENGTH_SHORT
                                                )
                                                t.show()
                                            }else{
                                                val t = Toast.makeText(
                                                    context,
                                                    it.result.toString(), Toast.LENGTH_SHORT
                                                )
                                                t.show()
                                            }
                                        }
                                }
                                val intent = Intent(activity, LoginActivity::class.java)
                                activity?.startActivity(intent)
                            }else{
                                if(txtPasswd.length() < 6){
                                    val t = Toast.makeText(
                                        context,
                                        "La contraseña no contiene 6 o mas caracteres", Toast.LENGTH_SHORT
                                    )
                                    t.show()
                                    txtPasswd.setBackgroundTintList(activity?.applicationContext?.let { it1 ->
                                        ContextCompat.getColorStateList(it1, R.color.rojo)
                                    })
                                }else{
                                    val t = Toast.makeText(
                                        context,
                                        "El correo esta ya registrado", Toast.LENGTH_SHORT
                                    )
                                    t.show()
                                    txtCorreo.setBackgroundTintList(activity?.applicationContext?.let { it1 ->
                                        ContextCompat.getColorStateList(it1, R.color.rojo)
                                    })
                                }

                            }
                        }
                    }else{

                        val t = Toast.makeText(
                            context,
                            "Otro usuario ya creado con ese nick", Toast.LENGTH_SHORT
                        )
                        t.show()
                        txtNick.setBackgroundTintList(activity?.applicationContext?.let { it1 ->
                            ContextCompat.getColorStateList(it1, R.color.rojo)
                        })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    val t = Toast.makeText(
                        context,
                        "Error", Toast.LENGTH_SHORT
                    )
                    t.show()
                }

            })
    }

    //Metodo para comprbar que el formato del correo es valido
    private fun comprobarCorreo(correo: String): Boolean {

        val pattern = Pattern
            .compile(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            )
        val mather = pattern.matcher(correo)
        return if (mather.find() == true) {
                    true
                } else {
                    false
                 }
    }

}