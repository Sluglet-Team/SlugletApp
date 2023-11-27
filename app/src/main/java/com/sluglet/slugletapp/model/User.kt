package com.sluglet.slugletapp.model

data class User(
    // default values
    val email: String = "",
    val name: String = "",
    val uid: String = "",
    val courses : MutableList<String> = ArrayList()
)