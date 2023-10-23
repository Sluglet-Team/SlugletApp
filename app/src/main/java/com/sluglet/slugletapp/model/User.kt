package com.sluglet.slugletapp.model

data class User(
    // default values
    // id = empty string, will be user id when account created
    val id: String = "",
    // isAnonymous is when the user is not signed in, defaults to true
    val isAnonymous: Boolean = true
)
