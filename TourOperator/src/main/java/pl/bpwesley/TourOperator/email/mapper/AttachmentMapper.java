package pl.bpwesley.TourOperator.email.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.bpwesley.TourOperator.email.dto.AttachmentDto;
import pl.bpwesley.TourOperator.email.entity.Attachment;

@Mapper
public interface AttachmentMapper {
    AttachmentMapper INSTANCE = Mappers.getMapper(AttachmentMapper.class);

    AttachmentDto attachmentToAttachmentDto(Attachment attachment);
    Attachment attachmentDtoToAttachment(AttachmentDto attachmentDto);
}
