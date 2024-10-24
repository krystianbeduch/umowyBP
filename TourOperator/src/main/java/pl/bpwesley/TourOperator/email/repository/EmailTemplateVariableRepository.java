package pl.bpwesley.TourOperator.email.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.bpwesley.TourOperator.email.entity.EmailTemplateVariable;

import java.util.List;

@Repository
public interface EmailTemplateVariableRepository extends JpaRepository<EmailTemplateVariable, Long> {
    @Query("SELECT e FROM EmailTemplateVariable e WHERE e.emailTemplate.emailTemplateId = :id")
    List<EmailTemplateVariable> findByEmailTemplate_Id(@Param("id") Long emailTemplateId);
}
