package com.kazale.pontointeligente.controllers

import com.kazale.pontointeligente.dtos.CadastroPJDto
import com.kazale.pontointeligente.services.EmpresaService
import com.kazale.pontointeligente.services.FuncionarioService
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.xml.ws.Response

@RestController
@RequestMapping("/api/cadastrar-pj/")
class CadastroPJController(val empresaService: EmpresaService,
                           val funcionarioService: FuncionarioService) {

    @PostMapping
    fun cadastra(@Valid @RequestBody cadastroPJDto: CadastroPJDto, result: BindingResult):
            ResponseEntity<Response<CadastroPJDto>> {
        val response: Response<CadastroPJDto> = Response<CadastroPJDto>()


        return ResponseEntity.ok(response)
    }

    private fun validarDadosExistente(cadastroPJDto: CadastroPJDto, result: BindingResult) {
        val empresa = empresaService.buscarPorCnpj(cadastroPJDto.cnpj)

        if (empresa != null) {
            result.addError(ObjectError("empresa", "Empresa já existente."))
        }


    }
}