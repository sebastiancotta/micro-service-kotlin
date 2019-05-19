package com.kazale.pontointeligente.repositories

import com.kazale.pontointeligente.documentos.Funcionario
import org.springframework.data.mongodb.repository.MongoRepository

interface FuncionarioRepository : MongoRepository<Funcionario, String> {
    fun findByEmail(email: String): Funcionario
    fun findByCpf(cpf: String): Funcionario
}