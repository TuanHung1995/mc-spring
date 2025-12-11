package com.mc.application.mapper;

import com.mc.application.model.team.CreateApartmentResponse;
import com.mc.domain.model.entity.Apartment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApartmentMapper {

    CreateApartmentResponse toCreateApartmentResponse(Apartment apartment);

}
