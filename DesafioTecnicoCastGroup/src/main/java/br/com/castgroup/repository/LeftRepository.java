package br.com.castgroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.castgroup.models.Left;

public interface LeftRepository extends JpaRepository<Left, Long> {

	Left findById(long id);
	
}
