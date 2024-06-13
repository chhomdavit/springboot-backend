package com.davit.springblog.controller;

import com.davit.springblog.constant.AppConstants;
import com.davit.springblog.dto.TokenRequestDto;
import com.davit.springblog.dto.UserRequestDto;
import com.davit.springblog.dto.UserResponseDto;
import com.davit.springblog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

//    @PostMapping("/auth/register")
//    public ResponseEntity<UserResponseDto> register(
//            @RequestParam("email") String email,
//            @RequestParam("name") String name,
//            @RequestParam("password") String password,
//            @RequestParam(value = "file", required = false) MultipartFile file) {
//
//        UserRequestDto userRequestDto = new UserRequestDto();
//        userRequestDto.setEmail(email);
//        userRequestDto.setName(name);
//        userRequestDto.setPassword(password);
//
//        try {
//            UserResponseDto userResponseDto = userService.register(userRequestDto, file);
//            return ResponseEntity.ok().body(userResponseDto);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    
    @PostMapping("/auth/register")
    public ResponseEntity<UserResponseDto> register(
            @RequestParam("email") String email,
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail(email);
        userRequestDto.setName(name);
        userRequestDto.setPassword(password);

        try {
            UserResponseDto userResponseDto = userService.register(userRequestDto, file);
            return ResponseEntity.ok().body(userResponseDto);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.login(userRequestDto));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<UserResponseDto> refreshToken(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(userService.refreshToken(tokenRequestDto));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<UserResponseDto> getAllUsers(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize,
            @RequestParam(required = false) String keyword) {
        UserResponseDto userRequestDto = userService.getAllUsers(keyword, pageNumber, pageSize);
        return ResponseEntity.ok().body(userRequestDto);
    }

    @GetMapping("/admin/get-users/{userId}")
    public ResponseEntity<UserResponseDto> getUSerByID(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getUsersById(userId));

    }

    @PutMapping("/auth/update/{userId}")
    public ResponseEntity<UserResponseDto> update(
            @PathVariable Integer userId,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("role") String role,
            @RequestParam("password") String password,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setName(name);
        userRequestDto.setEmail(email);
        userRequestDto.setRole(role);
        userRequestDto.setPassword(password);

        try {
            UserResponseDto updatedUserDto = userService.update(userId, userRequestDto, file);
            return ResponseEntity.ok().body(updatedUserDto);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/auth/get-profile")
    public ResponseEntity<UserResponseDto> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserResponseDto response = userService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<String> delete(@PathVariable(value = "userId") Integer userId) {
        try {
            userService.delete(userId);
            return ResponseEntity.ok("Post deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete Post: " + e.getMessage());
        }
    }

}
