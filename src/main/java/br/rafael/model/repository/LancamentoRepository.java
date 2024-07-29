package br.rafael.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.rafael.model.entity.Lancamento;
import br.rafael.model.enums.StatusLancamento;
import br.rafael.model.enums.TipoLancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

	@Query("SELECT SUM(l.valor) FROM Lancamento l JOIN l.usuario u WHERE u.id = :idUsuario AND l.tipo = :tipo AND l.status = :status")
	BigDecimal obterSaldoPorTipoLancEUsuario(
			@Param("idUsuario") Long idUsuario, 
			@Param("tipo") TipoLancamento tipo,
			@Param("status") StatusLancamento status);

}
