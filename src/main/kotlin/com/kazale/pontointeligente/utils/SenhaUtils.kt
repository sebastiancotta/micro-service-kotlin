package com.kazale.pontointeligente.utils

//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class SenhaUtils {
    fun geraBcrypt(senha: String): String = senha//BCryptPasswordEncoder().encode(senha)
}