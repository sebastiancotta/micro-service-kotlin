package com.kazale.pontointeligente.controllers

import com.kazale.pontointeligente.documentos.Empresa
import com.kazale.pontointeligente.documentos.Funcionario
import com.kazale.pontointeligente.dtos.CadastroPJDto
import com.kazale.pontointeligente.enums.PerfilEnum
import com.kazale.pontointeligente.response.Response
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

@RestController
@RequestMapping("/api/cadastrar-pj/")
class CadastroPJController(val empresaService: EmpresaService,
                           val funcionarioService: FuncionarioService) {

    @PostMapping
    fun cadastrar(@Valid @RequestBody cadastroPJDto: CadastroPJDto, result: BindingResult):
            ResponseEntity<Response<CadastroPJDto>> {
        val response: Response<CadastroPJDto> = Response<CadastroPJDto>()
        validarDadosExistente(cadastroPJDto, result)

        if (result.hasErrors()) {
            for( erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
            return ResponseEntity.badRequest().body(response)
        }

        var empresa = converterCadastroPJDto(cadastroPJDto)
        empresa = empresaService.persistir(empresa)

        val funcionario = converterFuncionarioDo(cadastroPJDto, empresa)
        funcionarioService.persistir(funcionario)

        response.data = converterCadastroPJDto(funcionario, empresa)

        return ResponseEntity.ok(response)
    }

    private fun validarDadosExistente(cadastroPJDto: CadastroPJDto, result: BindingResult) {
        val empresa: Empresa? = empresaService.buscarPorCnpj(cadastroPJDto.cnpj)

        if (empresa != null) {
            result.addError(ObjectError("empresa", "Empresa já existente."))
        }

        val funcionario: Funcionario? =  funcionarioService.buscarPorCPF(cadastroPJDto.cpf)
        if (funcionario != null) {
            result.addError(ObjectError("funcionario", "Funcionário já existente."))
        }

        val funcionarioEmail = funcionarioService.buscarPorEmail(cadastroPJDto.email)
        if (funcionarioEmail != null) {
            result.addError(ObjectError("funcionario", "Email já existente."))
        }

    }

    private fun converterCadastroPJDto(cadastroPJDto: CadastroPJDto): Empresa = Empresa(cadastroPJDto.razaoSocial, cadastroPJDto.cnpj)

    private fun converterFuncionarioDo(cadastroPJDto: CadastroPJDto, empresa: Empresa): Funcionario =
            Funcionario(cadastroPJDto.nome, cadastroPJDto.email, cadastroPJDto.senha, cadastroPJDto.cnpj
                    , PerfilEnum.ROLE_ADMIN, empresa.id)

    private fun converterCadastroPJDto(funcionario: Funcionario, empresa: Empresa): CadastroPJDto =
            CadastroPJDto(funcionario.nome, funcionario.email, "", funcionario.cpf, empresa.cnpj,
                    empresa.razaoSocial, funcionario.id)
}