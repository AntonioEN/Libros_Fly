package com.example.libros_fly.Inicio


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.libros_fly.MainActivity
import com.example.libros_fly.R



class InicioFragment : Fragment() {

    private lateinit var inicioVM : InicioVM;

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