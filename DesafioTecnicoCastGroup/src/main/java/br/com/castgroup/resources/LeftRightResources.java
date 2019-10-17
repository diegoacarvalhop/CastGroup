package br.com.castgroup.resources;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.castgroup.models.Left;
import br.com.castgroup.models.Right;
import br.com.castgroup.repository.LeftRepository;
import br.com.castgroup.repository.RightRepository;

@RestController
@RequestMapping(value = "/v1")
public class LeftRightResources {

	@Autowired
	private LeftRepository leftRepository;

	@Autowired
	private RightRepository rightRepository;

	/**
	 * Método que salva o JSON na tabela LEFT e atualiza caso já exista algum
	 * registro na base de dados.
	 * 
	 * @author diego
	 * @return Left
	 */
	@PostMapping("/diff/{id}/left")
	public Left setLeft(@PathVariable(value = "id") long id, @RequestBody Left left) {
		List<Left> leftsBase = leftRepository.findAll();
		if (leftsBase.isEmpty()) {
			left.setId_request(id);
			return leftRepository.save(left);
		} else {
			Left leftBase = leftsBase.get(0);
			leftBase.setId_request(id);
			leftBase.setBase64(left.getBase64());
			return leftRepository.save(leftBase);
		}
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
		List<Right> rightsBase = rightRepository.findAll();
		if (rightsBase.isEmpty()) {
			right.setId_request(id);
			return rightRepository.save(right);
		} else {
			Right rightBase = rightsBase.get(0);
			rightBase.setId_request(id);
			rightBase.setBase64(right.getBase64());
			return rightRepository.save(rightBase);
		}
	}

	/**
	 * Método que realiza a comparação dos JSON e retornar as inconsistências.
	 * 
	 * @author diego
	 * @return ResponseEntity<Map<String, String>>
	 */
	@GetMapping("/diff")
	public ResponseEntity<Map<String, String>> getDiff() {
		Map<String, String> res = new HashMap<>();
		List<Left> leftBase64 = leftRepository.findAll();
		List<Right> rightBase64 = rightRepository.findAll();
		if (!(leftBase64.size() > 1 && rightBase64.size() > 1)) {
			String base64Left = new String(Base64.getDecoder().decode(leftBase64.get(0).getBase64()));
			String base64Right = new String(Base64.getDecoder().decode(rightBase64.get(0).getBase64()));
			if (base64Left.length() != base64Right.length()) {
				res.put("Error", "Documentos <" + leftBase64.get(0).getId_request() + "> e <"
						+ rightBase64.get(0).getId_request() + "> com tamanhos diferentes");
			} else {
				String[] stringUm = base64Left.split(" ");
				String[] stringDois = base64Right.split(" ");
				for (int xL = 0; xL < stringUm.length; xL++) {
					for (int xR = xL; xR < stringDois.length; xR++) {
						if (stringUm[xL].equals(stringDois[xR])) {
							xR = stringDois.length;
						} else {
							stringDois[xR] = "'" + stringDois[xR] + "'";
							xR = stringDois.length;
						}
					}
				}
				StringBuilder left = new StringBuilder();
				StringBuilder right = new StringBuilder();
				for (String item : stringUm) {
					left.append(item);
					left.append(" ");
				}
				left.replace(left.length() - 1, left.length(), "");
				for (String item : stringDois) {
					right.append(item);
					right.append(" ");
				}
				right.replace(right.length() - 1, right.length(), "");
				if (left.toString().equals(right.toString())) {
					res.put("Success", "Documentos <" + leftBase64.get(0).getId_request() + "> e <"
							+ rightBase64.get(0).getId_request() + "> idênticos");
				} else {
					res.put("" + leftBase64.get(0).getId_request() + "", left.toString());
					res.put("" + rightBase64.get(0).getId_request() + "", right.toString());
				}
			}
		}
		return new ResponseEntity<Map<String, String>>(res, HttpStatus.OK);
	}

}
