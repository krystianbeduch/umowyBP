package pl.bpwesley.TourOperator.email.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import pl.bpwesley.TourOperator.email.dto.EmailTemplateDto;
import pl.bpwesley.TourOperator.email.entity.EmailTemplate;

@Mapper(uses = {AttachmentMapper.class})
public interface EmailTemplateMapper {
    EmailTemplateMapper INSTANCE = Mappers.getMapper(EmailTemplateMapper.class);

    @Mapping(target = "attachmentDtos", source = "attachments")
    EmailTemplateDto emailTemplateToEmailTemplateDto(EmailTemplate emailTemplate);

    @Mapping(target = "attachments", source = "attachmentDtos")
    EmailTemplate emailTemplateDtoToEmailTemplate(EmailTemplateDto emailTemplateDto);
}
