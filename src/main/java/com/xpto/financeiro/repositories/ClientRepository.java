package com.xpto.financeiro.repositories;

import com.xpto.financeiro.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findByCpf(String cpf);
    Optional<Client> findByCnpj(String cnpj);
    boolean existsByCpf(String cpf);
    boolean existsByCnpj(String cnpj);
}
