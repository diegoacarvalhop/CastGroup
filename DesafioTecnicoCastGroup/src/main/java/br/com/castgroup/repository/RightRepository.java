package br.com.castgroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.castgroup.models.Right;

public interface RightRepository extends JpaRepository<Right, Long> {

	Right findById(long id);

}
