package br.rafael.api.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class LancamentoDTO {
	
	private Long id;
	private String descricao;
	private Integer mes;
	private Integer ano;
	private BigDecimal valor;
	private Long usuario;
	private String tipo;
	private String status;
	
	public LancamentoDTO() {
		// TODO Auto-generated constructor stub
	}

	public LancamentoDTO(Long id, String descricao, Integer mes, Integer ano, BigDecimal valor, Long usuario,
			String tipo, String status) {
		super();
		
		this.descricao = descricao;
		this.mes = mes;
		this.ano = ano;
		this.valor = valor;
		this.usuario = usuario;
		this.tipo = tipo;
		this.status=status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Long getUsuario() {
		return usuario;
	}

	public void setUsuario(Long usuario) {
		this.usuario = usuario;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ano, descricao, id, mes, status, tipo, usuario, valor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LancamentoDTO other = (LancamentoDTO) obj;
		return Objects.equals(ano, other.ano) && Objects.equals(descricao, other.descricao)
				&& Objects.equals(id, other.id) && Objects.equals(mes, other.mes)
				&& Objects.equals(status, other.status) && Objects.equals(tipo, other.tipo)
				&& Objects.equals(usuario, other.usuario) && Objects.equals(valor, other.valor);
	}
	
	
}
