package com.mc.application.mapper;

import com.mc.application.model.team.CreateApartmentResponse;
import com.mc.application.model.team.GetApartmentResponse;
import com.mc.domain.model.entity.Apartment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApartmentMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    CreateApartmentResponse toCreateApartmentResponse(Apartment apartment);

    @Mapping(source = "owner.id", target = "ownerId")
    List<CreateApartmentResponse> toCreateApartmentResponseList(List<Apartment> apartments);

    @Mapping(source = "id", target = "apartmentId")
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "members", target = "members")
    GetApartmentResponse toGetApartmentResponse(Apartment apartment);

}
