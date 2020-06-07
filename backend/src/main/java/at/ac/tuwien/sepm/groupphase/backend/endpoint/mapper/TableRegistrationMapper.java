package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TableRegistrationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.TableInfo;
import org.mapstruct.Mapper;

@Mapper
public class TableRegistrationMapper {


    public ApplicationUser tableRegistrationDtoToApplicationUser(TableRegistrationDto tableRegistrationDto) {

        ApplicationUser tableUser = new ApplicationUser();
        tableUser.setPassword(tableRegistrationDto.getPassword());
        tableUser.setName(tableRegistrationDto.getName());
        tableUser.setEmail("a");
        tableUser.setAdmintype("Table");

        TableInfo tableInfo = new TableInfo();
        tableInfo.setSeats(tableRegistrationDto.getSeats());
        tableInfo.setUsed(false);
        tableInfo.setApplicationUser(tableUser);
        tableUser.setTableInfo(tableInfo);

        return tableUser;
    }

    public TableRegistrationDto ApplicationUserToTableRegistrationDto(ApplicationUser applicationUser) {

        TableRegistrationDto tableRegistrationDto = new TableRegistrationDto();

        tableRegistrationDto.setEmail("xx");
        tableRegistrationDto.setPassword("xx");
        tableRegistrationDto.setSeats(applicationUser.getTableInfo().getSeats());
        tableRegistrationDto.setName(applicationUser.getName());

        return tableRegistrationDto;
    }
}