package pl.bpwesley.TourOperator.email.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bpwesley.TourOperator.email.entity.Attachment;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByEmailTemplate_EmailTemplateId(Long emailTemplateId);
}
