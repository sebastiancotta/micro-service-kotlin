package com.kazale.pontointeligente.controllers

import com.kazale.pontointeligente.documentos.Empresa
import com.kazale.pontointeligente.dtos.EmpresaDto
import com.kazale.pontointeligente.response.Response
import com.kazale.pontointeligente.services.EmpresaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/empresas")
class EmpresaController(val empresaService: EmpresaService) {

    @GetMapping(value = "/cnpj/{cnpj}")
    fun buscaPorCnpj(@PathVariable("cnpj") cnpj: String): ResponseEntity<Response<EmpresaDto>> {
        val response: Response<EmpresaDto> = Response<EmpresaDto>()

        val empresa: Empresa? = empresaService.buscarPorCnpj(cnpj)

        if (empresa == null) {
            response.erros.add("Empresa não encontrada para o cnpj $cnpj.")
            return ResponseEntity.badRequest().body(response)
        }

        response.data = converterEmrpesaDto(empresa)
        return ResponseEntity.ok(response)
    }

    private fun converterEmrpesaDto(empresa: Empresa): EmpresaDto =
            EmpresaDto(empresa.razaoSocial, empresa.cnpj, empresa.id)
}