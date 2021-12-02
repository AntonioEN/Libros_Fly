package com.example.libros_fly.ListaLibros

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.os.Debug
import android.os.SystemClock
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.libros_fly.Inicio.InicioFragment
import com.example.libros_fly.Login.LoginActivity
import com.example.libros_fly.R
import com.example.libros_fly.clases.Libros
import com.example.libros_fly.clases.Prestamo
import com.example.libros_fly.clases.Reservas
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.type.DateTime
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

//Fragment para sacar los datos del libro seleccionado por el usuario
class DescripcionFragment : Fragment(){

    private var libro : Libros? = null
    private lateinit var titulo: TextView
    private lateinit var autor: TextView
    private lateinit var isbn: TextView
    private lateinit var sinopsis: TextView
    private lateinit var genero : TextView
    private lateinit var ejemplares : TextView
    private lateinit var database: FirebaseDatabase
    private lateinit var dbReference: DatabaseReference
    private lateinit var dbReferencer: DatabaseReference
    private lateinit var dbReferencerp: DatabaseReference
    private lateinit var dbReferencerpr: DatabaseReference
    private lateinit var fecha : String
    private lateinit var txtFecha : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Aqui se recibira y se cargara el libro que el usuario ha seleccionado
        arguments?.let {
            libro = it.getSerializable("libro") as Libros?
        }
    }




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =  inflater.inflate(R.layout.fragment_descripcion_lista_libros, container, false)

        titulo = root.findViewById(R.id.txtTit)
        autor = root.findViewById(R.id.txtAut)
        isbn = root.findViewById(R.id.txtISBN)
        sinopsis = root.findViewById(R.id.txtSin)
        genero = root.findViewById(R.id.txtGenero)
        ejemplares = root.findViewById(R.id.txtStock)


        database = FirebaseDatabase.getInstance("https://librosfly-default-rtdb.europe-west1.firebasedatabase.app/")
        dbReference = database.reference.child("ListaLibros")
        dbReferencer = database.reference.child("reservas")
        dbReferencerp = database.reference.child("penalizacion")
        dbReferencerpr = database.reference.child("prestamos")


        autor.text = libro?.autor
        genero.text = libro?.genero
        isbn.text = libro?.isbn
        sinopsis.text = libro?.sinopsis
        ejemplares.text = libro?.stock
        titulo.text = libro?.titulo

        var n_ejemplares = Integer.parseInt(ejemplares.text.toString())
        val idUsuario = FirebaseAuth.getInstance().uid.toString()
        val btnReserva = root.findViewById<Button>(R.id.btnReservar)
        val btnPrestamo = root.findViewById<Button>(R.id.btnInformar)
        val btnFecha = root.findViewById<Button>(R.id.btnFecha)
        txtFecha = root.findViewById<TextView>(R.id.txtFecha)


        //Cuando se pulse el boton 'Reservar Fecha' llame al metodo showDatePickerDialog()
        btnFecha.setOnClickListener( View.OnClickListener {
            showDatePickerDialog()
        })



        val child : String = isbn.text as String ? + idUsuario

        dbReferencerp.addListenerForSingleValueEvent(object : ValueEventListener {
            //Se comprobara si el usuario esta penalizado
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.hasChild(idUsuario)){
                    btnPrestamo.isVisible = false
                    btnReserva.isVisible = false
                    val t = Toast.makeText(context, "Estas penalizado por devolver tarde una reserva", Toast.LENGTH_SHORT)
                    t.show()
                }else{
                    //Si no lo esta pasara ha comprobar si el usuario tiene ya reserva de ese libro
                    dbReferencer.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.hasChild(child)){
                                btnPrestamo.isVisible = false
                                btnReserva.isVisible = false
                            }else{
                                //Si no tiene reserva, se comprobara si el libro tiene ejemplares
                                if(n_ejemplares == 0){
                                    //Si no tiene ejemplares, el usuario enviara un aviso a la biblioteca que sera guardado en la base de datos
                                    btnPrestamo.isVisible = true
                                    btnReserva.isVisible = false
                                    val hijo : String = isbn.text as String ? + idUsuario
                                    val pre = Prestamo(isbn.text as String?, idUsuario)
                                    btnPrestamo.setOnClickListener(  View.OnClickListener {
                                        FirebaseDatabase.getInstance("https://librosfly-default-rtdb.europe-west1.firebasedatabase.app/")
                                            .getReference("prestamo").child(hijo)
                                            .setValue(pre).addOnCompleteListener {
                                                if(it.isSuccessful){
                                                    val t = Toast.makeText(context, "Pedida de prestamos realizada, aviso enviado", Toast.LENGTH_SHORT)
                                                    t.show()
                                                }else{
                                                    val t = Toast.makeText(context,it.result.toString(), Toast.LENGTH_SHORT)
                                                    t.show()
                                                }
                                            }
                                    })
                                }else{
                                    //Si el libro tiene ejemplares, el usuario podra realizar la reserva
                                    btnReserva.isVisible = true
                                    btnPrestamo.isVisible = false
                                    //Al pulsar el boton 'Realizar reserva', se comprobara si el usuario ha seleccionado una fecha
                                    btnReserva.setOnClickListener( View.OnClickListener {
                                        if(txtFecha.text.toString() != ""){
                                            //Si tiene fecha seleccionada, se realizara la reserva perfectamente
                                            val date = fecha
                                            val res = Reservas(isbn.text as String?, idUsuario, date, titulo.text as String?)
                                            val hijo : String = isbn.text as String ? + idUsuario
                                            FirebaseDatabase.getInstance("https://librosfly-default-rtdb.europe-west1.firebasedatabase.app/")
                                                .getReference("reservas").child(hijo)
                                                .setValue(res).addOnCompleteListener {
                                                    if(it.isSuccessful){
                                                        cambiarFragment()
                                                        val t = Toast.makeText(context, "Reserva realizada", Toast.LENGTH_SHORT)
                                                        t.show()
                                                        n_ejemplares = n_ejemplares - 1
                                                        dbReference.child(libro?.isbn.toString()).child("Stock").setValue(n_ejemplares.toString())
                                                    }else{
                                                        val t = Toast.makeText(context,it.result.toString(), Toast.LENGTH_SHORT)
                                                        t.show()
                                                    }
                                                }
                                        }else{
                                            val t = Toast.makeText(context, "Porfavor elija fecha de tope", Toast.LENGTH_SHORT)
                                            t.show()
                                        }

                                    })

                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            val t = Toast.makeText(
                                context,
                                "Reserva mal creada", Toast.LENGTH_SHORT
                            )
                            t.show()
                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                val t = Toast.makeText(
                    context,
                    "Penalizacion no existente", Toast.LENGTH_SHORT
                )
                t.show()
            }

        })


        return root
    }

    //Metodo para que salga un DatePicker cuando el usuario pulse el boton 'Reservar Fecha'
    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(requireFragmentManager(), "datePicker")

    }

    //Cuando se haya seleccionado una fecha, se guardara en una variable para
    fun onDateSelected(day: Int, month : Int, year: Int) {
        var dia = day
        val mes = month + 1
        val anno = year
        if(dia < 10){
            fecha = anno.toString()+"-"+mes.toString()+"-"+"0"+day.toString()
        }else{
            fecha = anno.toString()+"-"+mes.toString()+"-"+day.toString()
        }


        /*val t = Toast.makeText(context, ""  + dia + "/" + mes + "/" +anno +"" , Toast.LENGTH_SHORT)
        t.show()*/
        txtFecha.text = "Fecha tope de reserva es:"  + dia + "-" + mes + "-" +anno
    }

    //Metodo para cuando el usuario haga la reserva y este bien completamente, lo envie al Inicio
    private fun cambiarFragment(){
        val inicio = InicioFragment()
        val fm = fragmentManager
        val transaction = fm!!.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, inicio)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(libro: Libros) =
            DescripcionFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("libro", libro)
                }
            }
    }

}