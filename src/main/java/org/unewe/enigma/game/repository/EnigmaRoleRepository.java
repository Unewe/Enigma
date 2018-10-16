package org.unewe.enigma.game.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unewe.enigma.game.entity.EnigmaRole;

@Repository
public interface EnigmaRoleRepository extends JpaRepository<EnigmaRole, Long>{
    EnigmaRole findByUserId(int i);
}
