package pl.bpwesley.TourOperator.email.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "email_templates")
public class EmailTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailTemplateId;
    private String templateName;
    private String templateSubject;

    @Lob // Large Object
    private String content; // Pole przechowujace kod HTML
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "emailTemplate")
    private List<EmailTemplateVariable> emailTemplateVariables;

    // CascadeType.ALL - wszystkie operacje na EmailTemplate będą propogawane na Attachment
    @OneToMany(mappedBy = "emailTemplate", cascade = CascadeType.ALL)
    private List<Attachment> attachments;

    public EmailTemplate(String templateName, String templateSubject, String content, LocalDateTime updateDate) {
        this.templateName = templateName;
        this.templateSubject = templateSubject;
        this.content = content;
        this.updateDate = updateDate;
    }
}


