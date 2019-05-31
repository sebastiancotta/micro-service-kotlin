package com.kazale.pontointeligente.services

import com.kazale.pontointeligente.documentos.Funcionario
import com.kazale.pontointeligente.enums.PerfilEnum
import com.kazale.pontointeligente.repositories.FuncionarioRepository
import com.kazale.pontointeligente.utils.SenhaUtils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class FuncionarioServiceImplTest {

    @MockBean
    private val funcionarioRepository: FuncionarioRepository? = null

    @Autowired
    val funcionarioService: FuncionarioService? = null

    private val cpf = "123456789"
    private val id = "1"
    private val email = "teste@teste.com"

    @Before
    @Throws(Exception::class)
    fun setUp() {
        BDDMockito.given(funcionarioRepository?.save(Mockito.any(Funcionario::class.java))).willReturn(funcionario())
        BDDMockito.given(funcionarioRepository?.findByCpf(Mockito.anyString())).willReturn(funcionario())
        BDDMockito.given(funcionarioRepository?.findByEmail(Mockito.anyString())).willReturn(funcionario())
        BDDMockito.given(funcionarioRepository?.findById(Mockito.anyString())).willReturn(Optional.of(funcionario()))
    }

    private fun funcionario(): Funcionario = Funcionario(nome = "rafael", email = email, senha = SenhaUtils().geraBcrypt("12345"), cpf = cpf, perfil = PerfilEnum.ROLE_USUARIO, id = id)

    @Test
    fun persistir() {
        val funcionario = funcionarioService?.persistir(funcionario())
        Assert.assertNotNull(funcionario)
    }

    @Test
    fun buscarPorCPF() {
        val funcionario = funcionarioService?.buscarPorCPF(cpf)
        Assert.assertNotNull(funcionario)
    }

    @Test
    fun buscarPorEmail() {
        val funcionario = funcionarioService?.buscarPorEmail(cpf)
        Assert.assertNotNull(funcionario)
    }

    @Test
    fun buscarPorId() {
        val funcionario = funcionarioService?.buscarPorId(cpf)
        Assert.assertNotNull(funcionario)
    }
}