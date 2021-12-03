package com.example.libros_fly.MisLibros

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.libros_fly.R
import com.example.libros_fly.clases.Reservas

//Adaptador que sacara los datos que se han recogido de las reservas en un RecycledView
class MisLibrosAdapter(
    private val valores : List<Reservas>, private val fragmentmanager : FragmentManager
) : RecyclerView.Adapter<MisLibrosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_mislibros_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = valores.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val titulo : TextView = view.findViewById(R.id.txtTitLibro)
        val fecha_fin : TextView = view.findViewById(R.id.txtFechaFin)
        val rlRelative : RelativeLayout = view.findViewById(R.id.rlReservasRelative)
    }

    override fun toString(): String {
        return super.toString() + " '"
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Cogera las reservas que tenga el usuario como item y sacara la informacion de esas reservas
        val item = valores[position]
        holder.titulo.text = item.titulo
        holder.fecha_fin.text = item.fecha_reserva_fin
        holder.rlRelative.setOnClickListener(View.OnClickListener{
            //Al pulsar la reserva, mandara al usuario a la informacion de la reserva
            val info : InfoFragment = InfoFragment.newInstance(item)
            val cambio : FragmentTransaction = fragmentmanager!!.beginTransaction()
            cambio.replace(R.id.nav_host_fragment, info)
            cambio.addToBackStack(null)
            cambio.commit()
        })
    }

}