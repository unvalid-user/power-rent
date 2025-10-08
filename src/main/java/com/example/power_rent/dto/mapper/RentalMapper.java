package com.example.power_rent.dto.mapper;

import com.example.power_rent.dto.PagedResponse;
import com.example.power_rent.dto.RentalDto;
import com.example.power_rent.model.Rental;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(config = MapStructConfig.class, uses = EntityMapper.class)
public interface RentalMapper {
    RentalDto.Response.Basic toBasicResponse(Rental rental);
    List<RentalDto.Response.Basic> toBasicResponseList(List<Rental> rentals);
    // TODO
    default PagedResponse<RentalDto.Response.Basic> toBasicPagedResponse(Page<Rental> rentals) {
        return PagedResponse.<RentalDto.Response.Basic>builder()
                .content(toBasicResponseList(rentals.getContent()))
                .page(rentals.getNumber())
                .size(rentals.getSize())
                .totalPages(rentals.getTotalPages())
                .totalElements(rentals.getTotalElements())
                .build();
    }
}
