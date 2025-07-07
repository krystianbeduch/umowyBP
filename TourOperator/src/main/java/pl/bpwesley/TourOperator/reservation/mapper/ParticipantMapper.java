package pl.bpwesley.TourOperator.reservation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.bpwesley.TourOperator.reservation.dto.ParticipantDto;
import pl.bpwesley.TourOperator.reservation.entity.Participant;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {
    @Mapping(target = "phone", expression = "java(dto.getPhonePrefix() + ' ' + dto.getPhone())")
    Participant dtoToParticipant(ParticipantDto dto);

    ParticipantDto participantToDto(Participant participant);
}