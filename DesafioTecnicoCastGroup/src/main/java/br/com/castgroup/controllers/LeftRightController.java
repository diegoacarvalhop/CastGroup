package br.com.castgroup.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.castgroup.models.Left;
import br.com.castgroup.models.Right;
import br.com.castgroup.services.LeftRightService;

@RestController
@RequestMapping(value = "/v1")
public class LeftRightController {

	@Autowired
	private LeftRightService service;

	/**
	 * Método que salva o JSON na tabela LEFT e atualiza caso já exista algum
	 * registro na base de dados.
	 * 
	 * @author diego
	 * @return Left
	 */
	@PostMapping("/diff/{id}/left")
	public Left setLeft(@PathVariable(value = "id") long id, @RequestBody Left left) {
		return service.setLeft(id, left);
	}

	/**
	 * Método que salva o JSON na tabela RIGHT e atualiza caso já exista algum
	 * registro na base de dados.
	 * 
	 * @author diego
	 * @return Right
	 */
	@PostMapping("/diff/{id}/right")
	public Right setRight(@PathVariable(value = "id") long id, @RequestBody Right right) {
		return service.setRight(id, right);
	}

	/**
	 * Método que realiza a comparação dos JSON e retornar as inconsistências.
	 * 
	 * @author diego
	 * @return ResponseEntity<Map<String, String>>
	 */
	@GetMapping("/diff")
	public ResponseEntity<Map<String, String>> getDiff() {
		return service.getDiff();
	}

}
