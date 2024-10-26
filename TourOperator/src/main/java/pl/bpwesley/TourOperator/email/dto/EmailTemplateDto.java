package pl.bpwesley.TourOperator.email.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.bpwesley.TourOperator.email.entity.EmailTemplateVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplateDto {
    private Long emailTemplateId;
    private String templateName;
    private String templateSubject;
    private String content;
    private LocalDateTime updateDate;
    private List<EmailTemplateVariable> emailTemplateVariables;
    private List<AttachmentDto> attachmentDtos;

    public Map<String, String> getEmailTemplateVariablesAsMap() {
        return emailTemplateVariables.stream()
                .collect(Collectors.toMap(
                        var -> var.getVariable().getName(),
                        var -> var.getVariable().getValue()
                ));
    }
}
