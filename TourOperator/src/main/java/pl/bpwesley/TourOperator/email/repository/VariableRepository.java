package pl.bpwesley.TourOperator.email.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.bpwesley.TourOperator.email.entity.Variable;

import java.util.List;

@Repository
public interface VariableRepository extends JpaRepository<Variable, Long> {
    @Query("SELECT v.name FROM Variable v")
    List<String> findAllNames();

    @Query("SELECT v.value FROM Variable v WHERE v.name = :name")
    String findValueByName(@Param("name") String name);
}
