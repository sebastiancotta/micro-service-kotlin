package com.kazale.pontointeligente.services

import com.kazale.pontointeligente.documentos.Lancamento
import com.kazale.pontointeligente.enums.TipoEnum
import com.kazale.pontointeligente.repositories.LancamentoRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.mockito.verification.VerificationMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import kotlin.collections.ArrayList

@SpringBootTest
@RunWith(SpringRunner::class)
class LancamentoServiceTest {

    @MockBean
    val lancamentoRepository: LancamentoRepository? = null

    @Autowired
    val lancamentoService: LancamentoService? = null

    private val id = "1"

    @Before
    @Throws(Exception::class)
    fun setUp() {
        BDDMockito.given<Page<Lancamento>>(lancamentoRepository?.findByFuncionarioId(id, PageRequest(0, 10))).willReturn(PageImpl(ArrayList<Lancamento>()))
        BDDMockito.given(lancamentoRepository?.findById(Mockito.anyString())).willReturn(Optional.of(lancamento()))
        BDDMockito.given(lancamentoRepository?.save(Mockito.any(Lancamento::class.java))).willReturn(lancamento())
    }

    private fun lancamento(): Lancamento = Lancamento(Date(), TipoEnum.INICIO_ALMOCO, funcionarioId = id)

    @Test
    fun buscarPorFuncionarioId() {
        val lancamento: Page<Lancamento>? = lancamentoService?.buscarPorFuncionarioId(id, PageRequest.of(0, 10))
        Assert.assertNotNull(lancamento)
    }

    @Test
    fun buscarPorId() {
        val lancamento: Lancamento? = lancamentoService?.buscarPorId(id)
        Assert.assertNotNull(lancamento)
    }

    @Test
    fun persistir() {
        val lancamento: Lancamento? = lancamentoService?.persistir(lancamento())
        Assert.assertNotNull(lancamento)
    }

    @Test
    fun remover() {
        lancamentoService?.remover(id)
        BDDMockito.verify(lancamentoRepository, BDDMockito.times(1))?.deleteById(id)
    }
}