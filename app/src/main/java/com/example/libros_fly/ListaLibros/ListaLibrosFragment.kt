package com.example.libros_fly.ListaLibros

import android.Manifest
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ActionProvider
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.ColorUtils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.example.libros_fly.R
import com.example.libros_fly.clases.Libros
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.NonCancellable.cancel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//Fragment para recoger los datos de los libros de la base de datos y despues pasarselo a un Adapter
class ListaLibrosFragment : Fragment() {

    private var columnCount = 1
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var listaLibros: ArrayList<Libros>
    private lateinit var root : View
    private lateinit var btnBuscar: Button
    private lateinit var dbReferencerp: DatabaseReference
    private lateinit var btnSugerencia: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var buscarTitulo: EditText
    private var buscar: String? = null

    //Esta linea sirve para poder operar con la opcion LocalDate, ya que esta para versiones anteriores
    // y para esta no esta disponible, a menos que pongas esta linea
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_lista_libros, container, false)
       elegirLista(root)
        btnSugerencia = root.findViewById(R.id.btnSugerencia)

        //Al pulsar el boton de 'Sugerencia' se abrira una ventana emergente
        btnSugerencia.setOnClickListener(  View.OnClickListener {
            val dialog = CustomDialogFragment()
            activity?.supportFragmentManager?.let { it1 -> dialog.show(it1, "customDialog") }

        })


        dbReferencerp = database.reference.child("penalizacion")
        var fechaPenalizacion : String
        val idUsuario = FirebaseAuth.getInstance().uid.toString()
        val simpleFormat = DateTimeFormatter.ISO_DATE

        //Se comprobara si el usuario esta penalizado
        dbReferencerp.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.hasChild(idUsuario)){
                    //Se creara como un automatizador, para que si la fecha de penalizacion llega a su fin el usuario deje de estar penalizado
                    fechaPenalizacion = snapshot.child(idUsuario).child("fechaTopePenalizacion").value.toString()
                    val convetirFechaPenalizacion = LocalDate.parse(fechaPenalizacion, simpleFormat)
                    val calendario = java.util.Calendar.getInstance()
                    val hoy = SimpleDateFormat("yyyy-MM-dd")
                    val fechaHoy = hoy.format(calendario.getTime())
                    val convetirFechahoy = LocalDate.parse(fechaHoy, simpleFormat)
                    if(convetirFechahoy.dayOfMonth >= convetirFechaPenalizacion.dayOfMonth && convetirFechahoy.month == convetirFechaPenalizacion.month
                        && convetirFechahoy.year == convetirFechaPenalizacion.year){
                        dbReferencerp.child(idUsuario).removeValue()
                        val t = Toast.makeText(context, "Ya no esta penalizado", Toast.LENGTH_SHORT)
                        t.show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        return root

    }

    private fun elegirLista(root: View){

        buscarTitulo = root.findViewById(R.id.txtBuscarLibro)
        btnBuscar = root.findViewById(R.id.btnBuscar)
        recyclerView = root.findViewById(R.id.listView)

        database = FirebaseDatabase.getInstance("https://librosfly-default-rtdb.europe-west1.firebasedatabase.app/")
        databaseReference = database.reference.child("ListaLibros")
        //Se cargara de primeras este metodo, para toda la lista de libros
        vistaLista()

        //Si se filta por el titulo, al pulsar el boton con una lupa, se abriran dos opciones
        // si el EditText no esta vacio filtrara por el titulo buscado y si no seguira con la misma vista
        btnBuscar.setOnClickListener{
            buscar = buscarTitulo.text.toString()
            if(!buscar.equals("")){
                buscarLibroPorTitulo()
            }else{
                vistaLista()
            }
        }


    }


    private fun vistaLista(){
        //Se utiliza un arrayList para guardar dentro de él todos los libros
        listaLibros = ArrayList()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            //Al cambiar de libro, se recogera mediante la clase Libro y se almacenara en una variable que al final se añadira al arraylist antes comentado
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach() {
                    val libro = Libros(
                        it.child("Título").value.toString(),
                        it.child("Autor").value.toString(),
                        it.child("Sinopsis").value.toString(),
                        it.child("ISBN").value.toString(),
                        it.child("Genero").value.toString(),
                        it.child("Stock").value.toString()
                    )
                    listaLibros.add(libro)

                    try{
                        with(recyclerView) {
                            (recyclerView as RecyclerView).layoutManager = when {
                                columnCount <= 1 -> LinearLayoutManager(context)
                                else -> GridLayoutManager(context, columnCount)
                            }
                            //Se pasara al adapter el arrayList que contiene los libros que se almacenado dentro de el
                            val fragmentmanage = fragmentManager
                            (recyclerView as RecyclerView).adapter =
                                ListaLibrosAdapter(listaLibros, fragmentmanage!!)
                        }
                    }catch (e: Exception){

                    }

                }

            }

        })


    }


    private fun buscarLibroPorTitulo(){

            //Se utiliza un arrayList para guardar dentro de él todos los libros
            listaLibros = ArrayList()
            databaseReference.addValueEventListener(object : ValueEventListener{
                //Al cambiar de libro, se recogera mediante la clase Libro y se almacenara en una variable que al final se añadira al arraylist antes comentado
                //la excepción aqui, es que se recogera el o los libros por los que el usuario haya filtrado mediante el titulo
                override fun onDataChange(snapshot: DataSnapshot) {
                    try{
                    snapshot.children.forEach {
                        val titulo = it.child("Título").value.toString()
                        if(titulo.toLowerCase().startsWith(buscar!!.toLowerCase())){
                            val libro = Libros(
                                it.child("Título").value.toString(),
                                it.child("Autor").value.toString(),
                                it.child("Sinopsis").value.toString(),
                                it.child("ISBN").value.toString(),
                                it.child("Genero").value.toString(),
                                it.child("Stock").value.toString()
                            )
                            listaLibros.add(libro)
                        }
                        with(recyclerView) {
                            (recyclerView as RecyclerView).layoutManager = when {
                                columnCount <= 1 -> LinearLayoutManager(context)
                                else -> GridLayoutManager(context, columnCount)
                            }
                            //Se pasara al adapter el arrayList que contiene los libros que se han almacenado dentro de el
                            val fragmentmanage = fragmentManager
                            (recyclerView as RecyclerView).adapter =
                                ListaLibrosAdapter(listaLibros, fragmentmanage!!)
                        }

                    }
                    }catch(e: Exception){

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })


    }

}