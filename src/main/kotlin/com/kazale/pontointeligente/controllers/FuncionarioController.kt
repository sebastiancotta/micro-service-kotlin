package com.kazale.pontointeligente.controllers

import com.kazale.pontointeligente.documentos.Funcionario
import com.kazale.pontointeligente.dtos.FuncionarioDto
import com.kazale.pontointeligente.response.Response
import com.kazale.pontointeligente.services.FuncionarioService
import com.kazale.pontointeligente.utils.SenhaUtils
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/funcionarios")
class FuncionarioController(val funcionarioService: FuncionarioService) {

    @PutMapping(value = "/{id}")
    fun atualizar(@PathVariable("id") id: String, @Valid @RequestBody funcionarioDto: FuncionarioDto,
                  result: BindingResult): ResponseEntity<Response<FuncionarioDto>> {
        val response: Response<FuncionarioDto> = Response<FuncionarioDto>()
        var funcionario: Funcionario? = funcionarioService.buscarPorId(id)

        if (funcionario == null) {
            result.addError(ObjectError("funcionario", "Funcionário não encontrado."))
        }

        if (result.hasErrors()) {
            for( erro in result.allErrors) response.erros.add(erro.defaultMessage!!)
            return ResponseEntity.badRequest().body(response)
        }
        val atualizarFuncionario: Funcionario = atualizarDadosFuncionario(funcionario!!, funcionarioDto)

        funcionario = funcionarioService.persistir(atualizarFuncionario)

        response.data = converterFuncionarioDto(funcionario)

        return ResponseEntity.ok(response)
    }

    private fun converterFuncionarioDto(funcionario: Funcionario): FuncionarioDto =
            FuncionarioDto(funcionario.nome, funcionario.email, "", funcionario.valorHora.toString(),
                    funcionario.qtdHorasTrabalhoDia.toString(), funcionario.qtdHorasAlmoco.toString(), funcionario.id)

    private fun atualizarDadosFuncionario(funcionario: Funcionario, funcionarioDto: FuncionarioDto): Funcionario {

        val senha: String = if (funcionarioDto.senha == null) {
            funcionario.senha
        } else {
            SenhaUtils().geraBcrypt(funcionarioDto.senha)
        }

        return Funcionario(funcionarioDto.nome, funcionario.email, senha, funcionario.cpf, funcionario.perfil,
                funcionario.empresaId, funcionarioDto.valorHora?.toDouble(),
                funcionarioDto.qtdHorasTrabalhoDia?.toFloat(), funcionarioDto.qtdHorasAlmoco?.toFloat(), funcionario.id)

    }

}