package com.kazale.pontointeligente.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.kazale.pontointeligente.documentos.Funcionario
import com.kazale.pontointeligente.documentos.Lancamento
import com.kazale.pontointeligente.dtos.LancamentoDto
import com.kazale.pontointeligente.enums.PerfilEnum
import com.kazale.pontointeligente.enums.TipoEnum
import com.kazale.pontointeligente.services.FuncionarioService
import com.kazale.pontointeligente.services.LancamentoService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.text.SimpleDateFormat
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class LancamentoControllerTest {

    @Autowired
    private val mvc: MockMvc? = null

    @MockBean
    private val lancamentoService: LancamentoService? = null

    @MockBean
    private val funcionarioService: FuncionarioService? = null

    private val urlBase: String = "/api/lancamentos/"
    private val idFuncionario: String = "1"
    private val idLancamento: String = "1"
    private val tipoEnum: TipoEnum = TipoEnum.INICIO_TRABALHO
    private val data: Date = Date()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:SS")

    @Test
    fun testCadastrarLancamento() {
        val lancamento = obterDadosLancamento()

        BDDMockito.given<Funcionario>(funcionarioService?.buscarPorId(idFuncionario)).willReturn(funcionario())
        BDDMockito.given(lancamentoService?.persistir(lancamento)).willReturn(lancamento)

        mvc!!.perform(MockMvcRequestBuilders.post(urlBase)
                .content(obterJsonRequisicaoPost())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.data.tipo").value(tipoEnum.name))
                .andExpect(jsonPath("$.data.data").value(dateFormat.format(data)))
                .andExpect(jsonPath("$.data.funcionarioId").value(idFuncionario))
                .andExpect(jsonPath("$.erros").isEmpty)
    }

    @Test
    fun testCadastrarLancamentoFuncionarioInvalido() {
        BDDMockito.given<Funcionario>(funcionarioService?.buscarPorId(idFuncionario)).willReturn(null)
        mvc!!.perform(MockMvcRequestBuilders.post(urlBase)
                .content(obterJsonRequisicaoPost())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.erros").value("Funcionario não encontrado. ID inexistente."))
                .andExpect(jsonPath("$.data").isEmpty)
    }

    @Test
    @WithMockUser(username = "admin.admin@admin.com", roles = ["ADMIN"])
    fun testRemoverLancamento() {
        BDDMockito.given<Lancamento>(lancamentoService?.buscarPorId(idLancamento)).willReturn(obterDadosLancamento())

        mvc!!.perform(MockMvcRequestBuilders.delete(urlBase + idLancamento)
                .content(obterJsonRequisicaoPost())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
    }

    private fun obterJsonRequisicaoPost(): String {
        val lancamentoDto = LancamentoDto(dateFormat.format(data), tipoEnum.name, "Descrição", null, idFuncionario, idLancamento)
        val mapper = ObjectMapper()
        return mapper.writeValueAsString(lancamentoDto)
    }

    fun funcionario(): Funcionario = Funcionario("teste", "teste@email.com", "123456", "1234",
            PerfilEnum.ROLE_USUARIO, "1", 10.0, 5f, 5f, idFuncionario)
    fun obterDadosLancamento(): Lancamento = Lancamento(data, tipoEnum, idFuncionario,"Descrição", null, idLancamento)
}