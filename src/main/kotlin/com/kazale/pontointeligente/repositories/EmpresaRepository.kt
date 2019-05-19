package com.kazale.pontointeligente.repositories

import com.kazale.pontointeligente.documentos.Empresa
import org.springframework.data.mongodb.repository.MongoRepository

interface EmpresaRepository: MongoRepository<Empresa, String> {
    fun findByCnpj(cnpj: String): Empresa
}