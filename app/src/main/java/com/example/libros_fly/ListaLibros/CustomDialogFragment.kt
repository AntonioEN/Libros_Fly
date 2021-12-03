package com.example.libros_fly.ListaLibros

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.libros_fly.R
import com.example.libros_fly.clases.Sugerencias
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//DialogFragment para que cuando se pulse al boton 'Sugerencia' se habra una ventana emergente
class CustomDialogFragment : DialogFragment() {

    private lateinit var btnAtras: Button
    private lateinit var btnEnviar: Button
    private lateinit var titulo: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_custom_dialog, container, false)

        btnEnviar = root.findViewById(R.id.btnEnviar)
        btnAtras = root.findViewById(R.id.btnAtras)
        titulo = root.findViewById(R.id.txtLibro)

        //Cuando pulse el boton 'Atras' se salga de la ventana emergente
        btnAtras.setOnClickListener( View.OnClickListener {
            dismiss()
        })

        /*
        Cuando se introduzca el titulo del libro y el usuario pulse el boton 'Enviar'
        se comunicara con la base de datos para que cree un nuevo registro en Sugerencias
         */
        btnEnviar.setOnClickListener( View.OnClickListener {
            val idUsuario = FirebaseAuth.getInstance().uid.toString()
            val random = (0..100000).random()
            val child : String = idUsuario + random.toString()
            val sug = Sugerencias(idUsuario, titulo.text.toString())
            FirebaseDatabase.getInstance("https://librosfly-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("sugerencias").child(child)
            .setValue(sug).addOnCompleteListener {
                if(it.isSuccessful){
                    val t = Toast.makeText(context, "Sugerencia realizada", Toast.LENGTH_SHORT)
                    t.show()
                    dismiss()
                }else{
                    val t = Toast.makeText(context,it.result.toString(), Toast.LENGTH_SHORT)
                    t.show()
                }
            }
            Log.e("h", titulo.text.toString())
            Log.e("h", "libro")
        })


        return root
    }



}