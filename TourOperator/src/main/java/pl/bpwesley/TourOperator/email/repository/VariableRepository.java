package pl.bpwesley.TourOperator.email.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bpwesley.TourOperator.email.entity.Variable;

@Repository
public interface VariableRepository extends JpaRepository<Variable, Long> {
}
