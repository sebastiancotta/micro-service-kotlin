package com.kazale.pontointeligente.controllers

import com.kazale.pontointeligente.documentos.Empresa
import com.kazale.pontointeligente.documentos.Funcionario
import com.kazale.pontointeligente.dtos.CadastroPFDto
import com.kazale.pontointeligente.dtos.CadastroPJDto
import com.kazale.pontointeligente.enums.PerfilEnum
import com.kazale.pontointeligente.response.Response
import com.kazale.pontointeligente.services.EmpresaService
import com.kazale.pontointeligente.services.FuncionarioService
import com.kazale.pontointeligente.utils.SenhaUtils
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/cadastrar-pf/")
class CadastroPFController(val empresaService: EmpresaService,
                           val funcionarioService: FuncionarioService) {
    @PostMapping
    fun cadastrar(@Valid @RequestBody cadastroPFDto: CadastroPFDto, result: BindingResult):
            ResponseEntity<Response<CadastroPFDto>> {
        val response: Response<CadastroPFDto> = Response<CadastroPFDto>()

        val empresa: Empresa? = empresaService.buscarPorCnpj(cadastroPFDto.cnpj)
        validarDadosExistente(cadastroPFDto, empresa, result)
        if (result.hasErrors()) {
            for( erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
            return ResponseEntity.badRequest().body(response)
        }

        val funcionario: Funcionario = converterDtoParaFuncionario(cadastroPFDto, empresa!!)
        funcionarioService.persistir(funcionario)

        response.data = converterCadastroPFDto(funcionario, empresa)

        return ResponseEntity.ok(response)
    }

    private fun converterCadastroPFDto(funcionario: Funcionario, empresa: Empresa): CadastroPFDto
            = CadastroPFDto(funcionario.nome, funcionario.email, "", funcionario.cpf, empresa.cnpj,
            empresa.id.toString(), funcionario.valorHora?.toString(), funcionario.qtdHorasTrabalhoDia?.toString(),
            funcionario.qtdHorasAlmoco?.toString(), funcionario.id)

    private fun converterDtoParaFuncionario(cadastroPFDto: CadastroPFDto, empresa: Empresa): Funcionario
            = Funcionario(cadastroPFDto.nome, cadastroPFDto.email,
            SenhaUtils().geraBcrypt(cadastroPFDto.senha),
            cadastroPFDto.cpf, PerfilEnum.ROLE_USUARIO,
            empresa.id, cadastroPFDto.valorHora?.toDouble(),
            cadastroPFDto.qtdHorasTrabalhoDia?.toFloat(), cadastroPFDto.qtdHorasAlmoco?.toFloat(), cadastroPFDto.id)

    private fun validarDadosExistente(cadastroPFDto: CadastroPFDto, empresa: Empresa?, result: BindingResult) {
        if (empresa == null) {
            result.addError(ObjectError("empresa", "Empresa não cadastrada."))
        }

        val funcionario: Funcionario? =  funcionarioService.buscarPorCPF(cadastroPFDto.cpf)
        if (funcionario != null) {
            result.addError(ObjectError("funcionario", "Funcionário já existente."))
        }

        val funcionarioEmail = funcionarioService.buscarPorEmail(cadastroPFDto.email)
        if (funcionarioEmail != null) {
            result.addError(ObjectError("funcionario", "Email já existente."))
        }

    }
}