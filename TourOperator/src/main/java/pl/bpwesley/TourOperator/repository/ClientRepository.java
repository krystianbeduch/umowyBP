package pl.bpwesley.TourOperator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bpwesley.TourOperator.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
