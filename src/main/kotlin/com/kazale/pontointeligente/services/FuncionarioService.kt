package com.kazale.pontointeligente.services

import com.kazale.pontointeligente.documentos.Funcionario

interface FuncionarioService {

    fun persistir(funcionario: Funcionario): Funcionario

    fun buscarPorCPF(cpf: String): Funcionario?

    fun buscarPorEmail(email: String): Funcionario?

    fun buscarPorId(id: String): Funcionario?
}