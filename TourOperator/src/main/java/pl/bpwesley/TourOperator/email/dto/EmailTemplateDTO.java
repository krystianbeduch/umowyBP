package pl.bpwesley.TourOperator.email.dto;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplateDTO {
    private Long emailTemplateId;
    private String name;
    private String content;
    private LocalDateTime updateDate;
}
