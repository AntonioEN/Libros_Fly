package com.example.libros_fly.MisLibros

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libros_fly.ListaLibros.ListaLibrosAdapter
import com.example.libros_fly.R
import com.example.libros_fly.clases.Libros
import com.example.libros_fly.clases.Reservas
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

//Fragment para recoger los datos de las reservas de la base de datos y despues pasarselo a un Adapter
class MisLibrosFragment : Fragment() {

    private var columnCount = 1
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var listaReservas: ArrayList<Reservas>
    private lateinit var root : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root =  inflater.inflate(R.layout.fragment_mis_libros, container, false)
        vistaLista(root)
        return root
    }

    private fun vistaLista(root: View){
        //Se utiliza un arrayList para guardar dentro de él todos los libros
        listaReservas = ArrayList()
        database = FirebaseDatabase.getInstance("https://librosfly-default-rtdb.europe-west1.firebasedatabase.app/")
        databaseReference = database.reference.child("reservas")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            //Al cambiar de reserva, se recogera mediante la clase Reserva y se almacenara en una variable que al final se añadira al arraylist antes comentado
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val idUsuario = FirebaseAuth.getInstance().uid.toString()
                    val usu = it.child("id_usuario").value.toString()
                    if (usu.equals(idUsuario)) {
                        val reserva = Reservas(
                            it.child("isbn").value.toString(),
                            it.child("id_usuario").value.toString(),
                            it.child("fecha_reserva_fin").value.toString(),
                            it.child("titulo").value.toString()
                        )
                        listaReservas.add(reserva)
                    }
                    try{
                        with(root) {
                            (root as RecyclerView).layoutManager = when {
                                columnCount <= 1 -> LinearLayoutManager(context)
                                else -> GridLayoutManager(context, columnCount)
                            }
                            //Se pasara al adapter el arrayList que contiene las reservas que se han almacenado dentro de el
                            val fm = fragmentManager
                            (root as RecyclerView).adapter =
                                MisLibrosAdapter(listaReservas, fm!!)
                        }
                    }catch (e : Exception ){

                    }

                }
            }
        })
    }
}