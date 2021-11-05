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
import android.widget.Toast
import com.example.libros_fly.R
import com.example.libros_fly.clases.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.regex.Pattern

class RegistrarFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var usuariosCreados: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_registrar, container, false)

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val btnRegistrarse = root.findViewById<Button>(R.id.btnRegistrarse)
        val txtNombre = root.findViewById<EditText>(R.id.etxtNombre)
        val txtPasswd = root.findViewById<EditText>(R.id.etxtPassword)
        val txtCorreo = root.findViewById<EditText>(R.id.editCorreo)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://librosfly-default-rtdb.europe-west1.firebasedatabase.app/")

        databaseReference = database.reference.child("usuarios")
        usuariosCreados = database.reference.child("UsuariosCreados")

        btnRegistrarse.setOnClickListener(View.OnClickListener {

            var nombre = txtNombre.text.toString()
            var contrasenna = txtPasswd.text.toString()
            var correo = txtCorreo.text.toString()

            if(!correo.isEmpty() && !contrasenna.isEmpty() && !nombre.isEmpty()){

                if(comprobarCorreo(correo.trim())){
                    Toast.makeText(context,"Correo valido",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,"Correo no valido",Toast.LENGTH_SHORT).show()
                }
                //if(comprobarCorreo(correo.trim())){
                //    Toast.makeText(context,"Correo valido",Toast.LENGTH_SHORT).show()
                //}
                registrarUsuario(nombre, correo, contrasenna)
            }else{
                Toast.makeText(context,"Rellena todos los datos",Toast.LENGTH_SHORT).show()
            }

        })

        //falta boton para atras
        //ivRegistroAtras.setOnClickListener(View.OnClickListener
        //{
        //    val intent = Intent(activity, LoginActivity::class.java)
         //   activity?.startActivity(intent)
        //})

        return root
    }

    private fun registrarUsuario(nombre: String, correo: String, contrasenna: String){
        usuariosCreados.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("nombre ya registrado","" + snapshot.child(nombre).key)
                if(!snapshot.hasChild(nombre)){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        correo, contrasenna
                    ).addOnCompleteListener{
                        if(it.isSuccessful){
                            val id = auth.uid.toString()
                            val foto = ""
                            val usu = Usuario(nombre, id)

                            FirebaseAuth.getInstance().currentUser?.let { it1 ->
                                FirebaseDatabase.getInstance("https://librosfly-default-rtdb.europe-west1.firebasedatabase.app/")
                                    .getReference("usuarios").child(it1.uid)
                                    .setValue(usu).addOnCompleteListener {
                                        if(it.isSuccessful){
                                            usuariosCreados.child(nombre).setValue(id)
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
                            val t = Toast.makeText(
                                context,
                                "Usuario mal creado", Toast.LENGTH_SHORT
                            )
                            t.show()
                        }
                    }
                }else{
                    val t = Toast.makeText(
                        context,
                        "Usuario ya creado", Toast.LENGTH_SHORT
                    )
                    t.show()
                    Log.e("repetido", snapshot.child(nombre).key.toString())
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

    private fun comprobarCorreo(correo: String): Boolean {

        // Patrón para validar el email
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