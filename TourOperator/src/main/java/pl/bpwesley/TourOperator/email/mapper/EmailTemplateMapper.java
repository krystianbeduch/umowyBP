package pl.bpwesley.TourOperator.email.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.bpwesley.TourOperator.email.dto.EmailTemplateDTO;
import pl.bpwesley.TourOperator.email.entity.EmailTemplate;

@Mapper
public interface EmailTemplateMapper {
    EmailTemplateMapper INSTANCE = Mappers.getMapper(EmailTemplateMapper.class);

    EmailTemplateDTO emailTemplateToEmailTemplateDTO(EmailTemplate emailTemplate);
    EmailTemplate emailTemplateDTOToEmailTemplate(EmailTemplateDTO emailTemplateDTO);
}
