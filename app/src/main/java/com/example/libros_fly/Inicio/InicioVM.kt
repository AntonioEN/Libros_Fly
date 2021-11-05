package com.example.libros_fly.Inicio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InicioVM : ViewModel() {

    private val te = MutableLiveData<String>().apply {
        value = ""
    }

    val t : LiveData<String> = te
}