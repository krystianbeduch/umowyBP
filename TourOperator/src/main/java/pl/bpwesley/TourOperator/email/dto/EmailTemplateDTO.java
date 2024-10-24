package pl.bpwesley.TourOperator.email.dto;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.bpwesley.TourOperator.email.entity.EmailTemplateVariable;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplateDTO {
    private Long emailTemplateId;
    private String name;
    private String content;
    private LocalDateTime updateDate;
    private List<EmailTemplateVariable> emailTemplateVariables;
}
