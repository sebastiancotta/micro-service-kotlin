package com.kazale.pontointeligente.controllers

import com.kazale.pontointeligente.documentos.Lancamento
import com.kazale.pontointeligente.dtos.LancamentoDto
import com.kazale.pontointeligente.enums.TipoEnum
import com.kazale.pontointeligente.response.Response
import com.kazale.pontointeligente.services.FuncionarioService
import com.kazale.pontointeligente.services.LancamentoService
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.http.ResponseEntity.ok
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import javax.validation.Valid

@RestController
@RequestMapping("/api/lancamentos")
class LancamentoController(val lancamentoService: LancamentoService,
                           val funcionarioService: FuncionarioService) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:SS")

    @Value(value = "\${paginacao.qtd_por_pagina}")
    val qtdPorPagina: Int = 15

    @PostMapping
    fun adicionar(@Valid @RequestBody lancamentoDto: LancamentoDto, result: BindingResult):
            ResponseEntity<Response<LancamentoDto>> {
        val response: Response<LancamentoDto> = Response()
        validarFuncionario(lancamentoDto, result)

        if (result.hasErrors()) {
            for (error in result.allErrors) {
                response.erros?.add(error.defaultMessage!!)
                return badRequest().body(response)
            }
        }

        var lancamento = converterDtoParaLancamento(lancamentoDto, result)
        lancamento = lancamentoService.persistir(lancamento)
        response.data = convertLancamentoDto(lancamento)
        return ok(response)
    }

    @GetMapping("/{id}")
    fun listaPorId(@PathVariable("id") id: String): ResponseEntity<Response<LancamentoDto>> {
        val response: Response<LancamentoDto> = Response<LancamentoDto>()
        val lancamento: Lancamento? = lancamentoService.buscarPorId(id)

        if (lancamento == null) {
            response.erros.add("Lancamento não encontrado para o id: $id")
            ResponseEntity.badRequest().body(response)
        }

        response.data = convertLancamentoDto(lancamento)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/funcionario/{funcionarioId}")
    fun listaPorIdFuncionario(@PathVariable("funcionarioId") funcionarioId: String,
                              @RequestParam("pag", defaultValue = "0") pag: Int,
                              @RequestParam("ord", defaultValue = "id") ord: String,
                              @RequestParam("dir", defaultValue = "DESC") dir: String): ResponseEntity<Response<Page<LancamentoDto>>>{
        val response: Response<Page<LancamentoDto>> = Response<Page<LancamentoDto>>()
        val pageRequest: PageRequest = PageRequest(pag, qtdPorPagina, Sort.Direction.valueOf(dir), ord)

        val lancamentos: Page<Lancamento> = lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest)
        val lancamentoDtos: Page<LancamentoDto> = lancamentos.map { lancamento -> convertLancamentoDto(lancamento) }
        response.data = lancamentoDtos
        return ResponseEntity.ok(response)
    }

    private fun validarFuncionario(lancamentoDto: LancamentoDto, result: BindingResult) {
        if (lancamentoDto.funcionarioId == null) {
            result.addError(ObjectError("funcionario", "Funcionario não informado"))
            return
        }

        val funcionario = funcionarioService.buscarPorId(lancamentoDto.funcionarioId)
        if (funcionario == null) {
            result.addError(ObjectError("funcionario", "Funcionario não encontrado. ID inexistente"))
        }
    }

    private fun convertLancamentoDto(lancamento: Lancamento?): LancamentoDto =
        LancamentoDto(dateFormat.format(lancamento!!.data),
                        lancamento.tipo.name,
                        lancamento.descricao,
                        lancamento.localizacao,
                        lancamento.funcionarioId,
                        lancamento.id)


    private fun converterDtoParaLancamento(lancamentoDto: LancamentoDto, result: BindingResult): Lancamento {

        if (lancamentoDto.id != null) {
            val lancamento = lancamentoService.buscarPorId(lancamentoDto.id)
            if (lancamento == null){
                result.addError(ObjectError("lancamento", "Lançamento não encontrado"))
            }
        }

        return Lancamento(dateFormat.parse(lancamentoDto.data), TipoEnum.valueOf(lancamentoDto.tipo!!), lancamentoDto.funcionarioId!!,
                lancamentoDto.descricao, lancamentoDto.localizacao, lancamentoDto.id)

    }
}