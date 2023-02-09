package com.example.taxdeclarationservice.dto.mappers;

import com.example.taxdeclarationservice.model.User;
import com.example.taxdeclarationservice.payload.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, imports = {UUID.class, LocalDate.class})
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User mapToUser(RegisterRequestDTO registerRequestDTO);

    User mapLoginRequestToUser(LoginRequestDTO loginRequestDTO);


}
