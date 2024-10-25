package pl.bpwesley.TourOperator.email.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Lob // Large Object
    private String content; // Pole przechowujace kod HTML
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "emailTemplate")
    private List<EmailTemplateVariable> emailTemplateVariables;

    @OneToMany(mappedBy = "emailTemplate", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Attachment> attachments;


    public EmailTemplate(String templateName, String content, LocalDateTime updateDate) {
        this.templateName = templateName;
        this.content = content;
        this.updateDate = updateDate;
    }
}


