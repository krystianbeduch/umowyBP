package pl.bpwesley.TourOperator.email.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "attachments")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attachmentId;
    private String filename;

    @Lob // Large Object
    private byte[] fileData; // Pole przechowujace zawartosc pliku

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "email_template_id")
//    private EmailTemplate emailTemplate;

    public Attachment(String filename, byte[] fileData) {
        this.filename = filename;
        this.fileData = fileData;
    }

//    public Attachment(String filename, byte[] fileData, EmailTemplate emailTemplate) {
//        this.filename = filename;
//        this.fileData = fileData;
//        this.emailTemplate = emailTemplate;
//    }
}
