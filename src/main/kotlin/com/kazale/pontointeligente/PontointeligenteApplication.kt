package com.kazale.pontointeligente

import com.kazale.pontointeligente.documentos.Empresa
import com.kazale.pontointeligente.documentos.Funcionario
import com.kazale.pontointeligente.enums.PerfilEnum
import com.kazale.pontointeligente.repositories.EmpresaRepository
import com.kazale.pontointeligente.repositories.FuncionarioRepository
import com.kazale.pontointeligente.repositories.LancamentoRepository
import com.kazale.pontointeligente.utils.SenhaUtils
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
//@EnableWebSecurity
class PontointeligenteApplication(val empresaRepository: EmpresaRepository,
								  val funcionarioRepository: FuncionarioRepository,
								  val lancamentoRepository: LancamentoRepository): CommandLineRunner {

	override fun run(vararg args: String?) {
		empresaRepository.deleteAll()
		funcionarioRepository.deleteAll()
		lancamentoRepository.deleteAll()

		var empresa = Empresa(razaoSocial = "Teste", cnpj = "1234566")
		empresa = empresaRepository.save(empresa)

		var admin = Funcionario(nome = "Admin", email = "admin@empresa.com", senha = SenhaUtils().geraBcrypt("1234"),
				cpf = "123456789",
				perfil = PerfilEnum.ROLE_ADMIN, empresaId = empresa.id!!)
		admin = funcionarioRepository.save(admin)

		var funcionario = Funcionario(nome = "Funcionario", email = "funcionario@empresa.com",
				senha = SenhaUtils().geraBcrypt("1234"),
				cpf = "123456789",
				perfil = PerfilEnum.ROLE_USUARIO, empresaId = empresa.id!!)

		funcionario = funcionarioRepository.save(funcionario)

		System.out.println("Empresa ID:" + empresa.id)
		System.out.println("Funcionario ID:" + funcionario.id)
		System.out.println("Admin ID:" + admin.id)
	}
}

fun main(args: Array<String>) {
	runApplication<PontointeligenteApplication>(*args)
}

