package pl.bpwesley.TourOperator.email.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.bpwesley.TourOperator.email.entity.EmailTemplate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDto {
    private Long attachmentId;
    private String filename;
    private byte[] fileData;
//    private LocalDateTime updateDate;
    private EmailTemplate emailTemplate;
}
