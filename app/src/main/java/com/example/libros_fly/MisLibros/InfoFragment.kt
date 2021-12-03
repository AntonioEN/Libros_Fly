package com.example.libros_fly.MisLibros

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.*
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.libros_fly.Inicio.InicioFragment
import com.example.libros_fly.Login.RegistrarFragment
import com.example.libros_fly.MainActivity
import com.example.libros_fly.R
import com.example.libros_fly.clases.Penalizacion
import com.example.libros_fly.clases.Reservas
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Date.parse
import javax.xml.datatype.DatatypeConstants
import kotlin.time.days
import java.time.temporal.ChronoUnit.DAYS


class InfoFragment : Fragment() {

    private var reserva : Reservas? = null
    private lateinit var titulo: TextView
    private lateinit var isbn: TextView
    private lateinit var n_ejemplares: TextView
    private lateinit var database: FirebaseDatabase
    private lateinit var dbReference: DatabaseReference
    private lateinit var dbReferenced: DatabaseReference
    private lateinit var fecha : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Aqui se recibira y se cargara la reserva del libro que el usuario ha seleccionado
        arguments?.let {
            reserva = it.getSerializable("reserva") as Reservas?
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =  inflater.inflate(R.layout.fragment_info_libros, container, false)

        titulo = root.findViewById(R.id.txtTituloInfo)
        isbn = root.findViewById(R.id.txtISBNInfo)
        fecha  = root.findViewById(R.id.txtFechaDevolucionInfo)

        database = FirebaseDatabase.getInstance("https://librosfly-default-rtdb.europe-west1.firebasedatabase.app/")
        dbReference = database.reference.child("reservas")
        dbReferenced = database.reference.child("ListaLibros")

        titulo.text = reserva?.titulo
        isbn.text = reserva?.isbn
        fecha.text = reserva?.fecha_reserva_fin


        val idUsuario = FirebaseAuth.getInstance().uid.toString()
        val btnDevolucion = root.findViewById<Button>(R.id.btnDevolver)
        val id_reserva = isbn.text.toString() + idUsuario
        val isbn_libro = isbn.text

        //Se realizaran unos cambios, ya que se almacena la fecha como String para cambiarla a date
        // y despues enviarla otra vez como String a la base de datos
        val simpleFormat = DateTimeFormatter.ISO_DATE
        val convetirFecha = LocalDate.parse(fecha.text.toString() , simpleFormat)

        val calendario = Calendar.getInstance()
        val hoy = SimpleDateFormat("yyyy-MM-dd")
        val fechaHoy = hoy.format(calendario.getTime())
        val convetirFechahoy = LocalDate.parse(fechaHoy, simpleFormat)

        val fechaMenos = convetirFecha.minusDays(3)

        val fechaSumadaPenalizacion = convetirFecha.plusDays(10)
        val fechaPenalizacon = fechaSumadaPenalizacion.toString()

        Log.e("h","" + fechaPenalizacon)

        //Mensaje que se mostrara cuando el usuario haya sobrepasado la fecha tope de la reserva
        if(convetirFechahoy.dayOfMonth > convetirFecha.dayOfMonth && convetirFechahoy.month == convetirFecha.month
            && convetirFechahoy.year == convetirFecha.year){
            val t = makeText(context, "Te has pasado de fecha de devolucion", LENGTH_SHORT)
            t.show()
        }

        //Mensaje que se mostrara cuando la fecha tope este 3 dias de pasarse
        if(convetirFechahoy.dayOfMonth == fechaMenos.dayOfMonth && convetirFechahoy.month == convetirFechahoy.month
            && convetirFechahoy.year == convetirFecha.year ){
            val t = Toast.makeText(context, "Te quedan 3 dias para llegar a la fecha tope", Toast.LENGTH_SHORT)
            t.show()
        }

        //Cuando se pulse el boton 'Devolver libro', se haran una comprobaciones
        btnDevolucion.setOnClickListener(View.OnClickListener {
            dbReferenced.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.hasChild(isbn_libro.toString())){
                        //Si el usuario se ha sobrepasado de la fecha tope de reserva sera penalizado y si no lo devolvera sin ningun problema
                        val pen = Penalizacion(idUsuario , fechaPenalizacon)
                        if(convetirFechahoy.dayOfMonth > convetirFecha.dayOfMonth && convetirFechahoy.month == convetirFecha.month
                            && convetirFechahoy.year == convetirFecha.year){
                            FirebaseDatabase.getInstance("https://librosfly-default-rtdb.europe-west1.firebasedatabase.app/").getReference("penalizacion")
                                .child(idUsuario).setValue(pen).addOnCompleteListener {
                                    if(it.isSuccessful){
                                        val t = Toast.makeText(context, "Usuario Penalizado", Toast.LENGTH_SHORT)
                                        t.show()
                                    }
                                }
                        }
                        var numero = snapshot.child(isbn_libro.toString()).child("Stock").value.toString()
                        var ejemplares = Integer.parseInt(numero)
                        dbReference.child(id_reserva).removeValue()
                        val t = makeText(context, "Reserva devuelta", LENGTH_SHORT)
                        t.show()
                        ejemplares = ejemplares + 1
                        dbReferenced.child(reserva?.isbn.toString()).child("Stock").setValue(ejemplares.toString())
                        cambiarFragment()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


        })


        return root
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
        fun newInstance(reserva: Reservas) =
            InfoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("reserva", reserva)
                }
            }
    }

}