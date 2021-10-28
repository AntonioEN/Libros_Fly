package com.example.libros_fly.Login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.libros_fly.R
import java.util.regex.Pattern

class RegistrarFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_registrar, container, false)

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val btnRegistrarse = root.findViewById<Button>(R.id.btnRegistrarse)
        val txtNombre = root.findViewById<EditText>(R.id.etxtNombre)
        val txtPasswd = root.findViewById<EditText>(R.id.etxtPWD)
        val txtCorreo = root.findViewById<EditText>(R.id.etxtEmail)

        btnRegistrarse.setOnClickListener(View.OnClickListener {

            var nombre = txtNombre.text.toString()
            var contrasenna = txtPasswd.text.toString()
            var correo = txtCorreo.text.toString()

            if(!correo.isEmpty() && !contrasenna.isEmpty() && !nombre.isEmpty()){

                if(comprobarCorreo(correo.trim())){
                    Toast.makeText(context,"Correo valido",Toast.LENGTH_SHORT).show()
                }else{

                }
                registrarUsuario(nombre, correo, contrasenna)
            }else{
                Toast.makeText(context,"Rellena todos los datos",Toast.LENGTH_SHORT).show()
            }

        })

        //falta boton para atras

        return root
    }

    private fun registrarUsuario(nombre: String, correo: String, contrasenna: String){

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