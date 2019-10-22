package br.com.castgroup.services;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.castgroup.models.Left;
import br.com.castgroup.models.Right;
import br.com.castgroup.repository.LeftRepository;
import br.com.castgroup.repository.RightRepository;

@Service
public class LeftRightService {

	private LeftRepository leftRepository;
	private RightRepository rightRepository;

	@Autowired
	public LeftRightService(LeftRepository leftRepository, RightRepository rightRepository) {
		super();
		this.leftRepository = leftRepository;
		this.rightRepository = rightRepository;
	}

	public Left setLeft(long id, Left left) {
		left.setId_request(id);
		return leftRepository.save(left);
	}

	public Right setRight(long id, Right right) {
		right.setId_request(id);
		return rightRepository.save(right);
	}

	public ResponseEntity<Map<String, String>> getDiff() {
		Map<String, String> res = new HashMap<>();
		List<Left> leftBase = leftRepository.findAll();
		List<Right> rightBase = rightRepository.findAll();
		if (!leftBase.isEmpty() && !rightBase.isEmpty()) {
			String stringBase64Left = new String(Base64.getDecoder().decode(leftBase.get(0).getBase64()));
			String stringBase64Right = new String(Base64.getDecoder().decode(rightBase.get(0).getBase64()));
			if (stringBase64Left.length() != stringBase64Right.length()) {
				res.put("Error", "Documentos " + leftBase.get(0).getId_request() + " e "
						+ rightBase.get(0).getId_request() + " com tamanhos diferentes");
				leftRepository.deleteAll();
				rightRepository.deleteAll();
			} else {
				char[] stringUm = stringBase64Left.toCharArray();
				char[] stringDois = stringBase64Right.toCharArray();
				List<Integer> listDiff = new ArrayList<Integer>();
				for (int xL = 0; xL < stringUm.length; xL++) {
					for (int xR = xL; xR < stringDois.length; xR++) {
						if (stringUm[xL] == (stringDois[xR])) {
							xR = stringDois.length;
						} else {
							listDiff.add(xR + 1);
							xR = stringDois.length;
						}
					}
				}
				if (listDiff.isEmpty()) {
					res.put("Success", "Documentos " + leftBase.get(0).getId_request() + " e "
							+ rightBase.get(0).getId_request() + " idênticos");
					leftRepository.deleteAll();
					rightRepository.deleteAll();
				} else {
					res.put("Diff",
							"Documento ID(Left) " + leftBase.get(0).getId_request()
									+ " diferente do Documento ID(Right) " + leftBase.get(0).getId_request()
									+ " na(s) posição(es) " + listDiff.toString());
					leftRepository.deleteAll();
					rightRepository.deleteAll();
				}
			}
		} else if (!leftBase.isEmpty() && rightBase.isEmpty()) {
			res.put("Error", "Nenhum documento right encontrado");
			leftRepository.deleteAll();
			rightRepository.deleteAll();
		} else if (leftBase.isEmpty() && !rightBase.isEmpty()) {
			res.put("Error", "Nenhum documento left encontrado");
			leftRepository.deleteAll();
			rightRepository.deleteAll();
		} else if (leftBase.isEmpty() && rightBase.isEmpty()) {
			res.put("Error", "Nenhum documento left e right encontrado");
			leftRepository.deleteAll();
			rightRepository.deleteAll();
		}
		return new ResponseEntity<Map<String, String>>(res, HttpStatus.OK);
	}

}
