package com.kazale.pontointeligente.repositories

import com.kazale.pontointeligente.documentos.Lancamento
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface LancamentoRepository : MongoRepository<Lancamento, String> {
    fun findByFuncionarioId(funcionarioId: String, pageable: Pageable): Page<Lancamento>
}