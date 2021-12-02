package com.example.libros_fly.ListaLibros

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libros_fly.R
import com.example.libros_fly.clases.Libros
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction

//Adaptador que sacara los datos que se han recogido de los libros en un RecycledView
class ListaLibrosAdapter(
    private val valores : List<Libros>, private val fragmentmanager : FragmentManager
) : RecyclerView.Adapter<ListaLibrosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_listalibros_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = valores.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val titulo : TextView = view.findViewById(R.id.txtTitulo)
        val autor : TextView = view.findViewById(R.id.txtAutor)
        val genero : TextView = view.findViewById(R.id.txtGen)
        val rlRelative : RelativeLayout = view.findViewById(R.id.rlLibrosRelative)
    }

    override fun toString(): String {
        return super.toString() + " '"
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Cogera los libros que tenga como item y sacara la informacion de ellos
        val item = valores[position]
        holder.titulo.text = item.titulo
        holder.autor.text = item.autor
        holder.genero.text = item.genero
        //Al pulsar el libro, mandara al usuario a la descripcion del libro
        holder.rlRelative.setOnClickListener(View.OnClickListener{
            val descripcion : DescripcionFragment = DescripcionFragment.newInstance(item)
            val cambio : FragmentTransaction = fragmentmanager!!.beginTransaction()
            cambio.replace(R.id.nav_host_fragment, descripcion)
            cambio.addToBackStack(null)
            cambio.commit()
        })
    }

}