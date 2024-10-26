package pl.bpwesley.TourOperator.email.dto;

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
public class EmailTemplateDto {
    private Long emailTemplateId;
    private String templateName;
    private String content;
    private LocalDateTime updateDate;
    private List<EmailTemplateVariable> emailTemplateVariables;
    private List<AttachmentDto> attachmentDtos;
}
