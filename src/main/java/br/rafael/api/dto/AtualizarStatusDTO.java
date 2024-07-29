package br.rafael.api.dto;

import java.util.Objects;

public class AtualizarStatusDTO {

	private String status;
	
	public AtualizarStatusDTO() {
		// TODO Auto-generated constructor stub
	}

	public AtualizarStatusDTO(String status) {
		super();
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(status);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AtualizarStatusDTO other = (AtualizarStatusDTO) obj;
		return Objects.equals(status, other.status);
	}
	
}
