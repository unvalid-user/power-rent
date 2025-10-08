package com.example.power_rent.dto.mapper;

import com.example.power_rent.dto.UserDto;
import com.example.power_rent.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {
    UserDto.Response.Basic toBasicResponse(User user);
}
