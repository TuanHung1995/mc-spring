package com.mc.application.mapper;

import com.mc.application.model.team.CreateApartmentResponse;
import com.mc.domain.model.entity.Apartment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ApartmentMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    CreateApartmentResponse toCreateApartmentResponse(Apartment apartment);

}
