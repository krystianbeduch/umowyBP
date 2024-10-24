package pl.bpwesley.TourOperator.email.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bpwesley.TourOperator.email.entity.EmailTemplate;

import java.util.List;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
    List<EmailTemplate> findAllByOrderByUpdateDateAsc();
}
