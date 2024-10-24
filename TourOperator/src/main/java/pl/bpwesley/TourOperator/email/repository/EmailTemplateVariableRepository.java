package pl.bpwesley.TourOperator.email.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bpwesley.TourOperator.email.entity.EmailTemplateVariable;

import java.util.List;

@Repository
public interface EmailTemplateVariableRepository extends JpaRepository<EmailTemplateVariable, Long> {
//    List<EmailTemplateVariable> findByEmailTemplateId(Long emailTemplateId);
}
