package br.rafael.api.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.rafael.api.dto.AtualizarStatusDTO;
import br.rafael.api.dto.LancamentoDTO;
import br.rafael.exceptions.RegraNegocioException;
import br.rafael.model.entity.Lancamento;
import br.rafael.model.entity.Usuario;
import br.rafael.model.enums.StatusLancamento;
import br.rafael.model.enums.TipoLancamento;
import br.rafael.service.LancamentoService;
import br.rafael.service.UsuarioService;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {

	private LancamentoService lancamentoService;
	private UsuarioService usuarioService;
	
	public LancamentoController(LancamentoService service, UsuarioService usuarioService) {
		this.lancamentoService=service;
		this.usuarioService=usuarioService;
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping
	public ResponseEntity buscar(
			@RequestParam(value = "descricao", required = false) String descricao,
			@RequestParam(value = "mes", required = false) Integer mes,
			@RequestParam(value = "ano", required = false) Integer ano,
			@RequestParam("usuario") Long idUsuario) {
		
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		
		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		if (!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para o id informado");
		}else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		
		List<Lancamento> lancamentos = lancamentoService.buscar(lancamentoFiltro);
		return ResponseEntity.ok(lancamentos);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("{id}")
	public ResponseEntity obterLancamento(@PathVariable("id") Long id) {
		return lancamentoService.obterPorId(id)
					.map(lancamento -> new ResponseEntity(converter(lancamento), HttpStatus.OK))
					.orElseGet(() -> new ResponseEntity(HttpStatus.NOT_FOUND));

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes"})
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		try {
			Lancamento entidade = converter(dto);
			entidade = lancamentoService.salvar(entidade);
			return new ResponseEntity(entidade, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	@GetMapping("{id}/saldo")
	public ResponseEntity obterSaldo(@PathVariable("id") Long id) {
		Optional<Usuario> usuario = usuarioService.obterPorId(id);
		
		if(!usuario.isPresent()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		BigDecimal saldo = lancamentoService.obterSaldoPorUsuario(id);
		return ResponseEntity.ok(saldo);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PutMapping("/{id}")
	public ResponseEntity atualizar(@PathVariable Long id, @RequestBody LancamentoDTO dto) {
		
		return lancamentoService.obterPorId(id).map( entity -> {
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(entity.getId());
				lancamentoService.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		})
		.orElseGet( 
				() -> new ResponseEntity("Lancamento não encontrado na base de dados"
					,HttpStatus.BAD_REQUEST));
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizarStatusDTO dto) {
		return lancamentoService.obterPorId(id).map( entity -> {
			StatusLancamento statusLancamento = StatusLancamento.valueOf(dto.getStatus());
			if(statusLancamento == null) {
				return ResponseEntity.badRequest()
						.body("Não foi possível atualizar o status do lançamento, envie um status válido.");
				
			}
			
			try {
				entity.setStatus(statusLancamento);
				lancamentoService.atualizar(entity);
				return ResponseEntity.ok(entity);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet( () -> 
				new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable Long id) {
		return lancamentoService.obterPorId(id).map(entity ->{
			lancamentoService.deletar(entity);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		})
		.orElseGet( 
				() -> new ResponseEntity("Lancamento não encontrado na base de dados"
					,HttpStatus.BAD_REQUEST));
	}
	
	private LancamentoDTO converter(Lancamento lancamento) {
		return new LancamentoDTO(lancamento.getId(), 
				                 lancamento.getDescricao(), 
				                 lancamento.getMes(), 
				                 lancamento.getAno(), 
				                 lancamento.getValor(), 
				                 lancamento.getUsuario().getId(), 
				                 lancamento.getTipo().name(), 
				                 lancamento.getStatus().name());
	}
	
	private Lancamento converter(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());
		
		Usuario usuario = usuarioService.obterPorId(dto.getUsuario())
				.orElseThrow(
						() -> new RegraNegocioException("Usuário não encontrado para o id informado"));
		
		lancamento.setUsuario(usuario);
		
		if(dto.getTipo() != null) {
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		}
		
		if(dto.getStatus()!=null) {
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		}
		
		return lancamento;
		
	}
}
