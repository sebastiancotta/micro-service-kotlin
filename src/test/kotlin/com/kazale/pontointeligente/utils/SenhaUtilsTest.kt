package com.kazale.pontointeligente.utils

import org.junit.Assert
import org.junit.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class SenhaUtilsTest {

    private val senha = "123456"
    private val bCryptPasswordEncoder = BCryptPasswordEncoder()

    @Test
    fun testeGerarHashSenha() {
        val hash = SenhaUtils().geraBcrypt(senha)
        Assert.assertTrue(bCryptPasswordEncoder.matches(senha, hash))
    }
}