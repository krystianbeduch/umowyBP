package pl.bpwesley.TourOperator.groupsAdminPanel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.bpwesley.TourOperator.groupsAdminPanel.dto.ClientDTO;
import pl.bpwesley.TourOperator.groupsAdminPanel.entity.Client;

@Mapper
public interface ClientMapper {
    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    ClientDTO clientToClientDTO(Client client);
    Client clientDTOToClient(ClientDTO clientDTO);
}
