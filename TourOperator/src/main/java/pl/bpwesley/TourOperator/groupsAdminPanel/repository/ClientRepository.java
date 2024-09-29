package pl.bpwesley.TourOperator.groupsAdminPanel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.bpwesley.TourOperator.groupsAdminPanel.entity.Client;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findAllByOrderByClientNumberAsc();

    // metoda zwracana Optional<Client> - zwrocony moze zostac klient lub empty jesli klient nie zostanie znaleziony

    // w PostgreSQL wykonac polecenie CREATE EXTENSION IF NOT EXISTS unaccent
    @Query("SELECT c FROM Client c WHERE unaccent(LOWER(c.name)) = unaccent(LOWER(:name))")
    Optional<Client> findByNameIgnoreCaseAndAccent(@Param("name") String name);
    // JPA automatycznie wygeneruje implementacje na podstawie definicji

    @Query("SELECT COALESCE(MAX(c.clientNumber), 0) FROM Client c")
    Optional<Long> findMaxClientNumber();
}
