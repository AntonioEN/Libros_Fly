package com.example.libros_fly.Inicio


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libros_fly.ListaLibros.ListaLibrosAdapter
import com.example.libros_fly.MainActivity
import com.example.libros_fly.R
import com.example.libros_fly.clases.Libros
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception

//Frangmen que sera llamado cuando el usuario inicie sesion
class InicioFragment : Fragment() {

    private lateinit var inicioVM : InicioVM


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inicioVM = ViewModelProvider(this).get(InicioVM::class.java)
        val root = inflater.inflate(R.layout.fragment_inicio, container, false)
        inicioVM.t.observe(viewLifecycleOwner, Observer {

        })

        return root
    }

}