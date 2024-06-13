package com.davit.springblog.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.davit.springblog.dto.TokenRequestDto;
import com.davit.springblog.dto.UserRequestDto;
import com.davit.springblog.dto.UserResponseDto;

public interface UserService {

   
    UserResponseDto register(UserRequestDto userRequestDto ,MultipartFile file) throws IOException;

    UserResponseDto login(UserRequestDto userRequstDto);

    UserResponseDto refreshToken(TokenRequestDto tokenRequestDto);

    UserResponseDto getAllUsers(String keyword, int pageNumber, int pageSize);

    UserResponseDto getUsersById(Integer id);

    void delete(Integer userId) throws IOException;

    UserResponseDto update(Integer userId,UserRequestDto userRequestDto, MultipartFile file) throws IOException;

    UserResponseDto getMyInfo(String email);
}
