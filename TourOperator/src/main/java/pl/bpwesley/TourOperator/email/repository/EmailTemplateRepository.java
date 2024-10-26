package pl.bpwesley.TourOperator.email.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bpwesley.TourOperator.email.entity.EmailTemplate;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
    List<EmailTemplate> findAllByOrderByUpdateDateDesc();

//    @EntityGraph(attributePaths = {"attachments"})
//    Optional<EmailTemplate> findById(Long emailTemplateId);
}
