package com.davit.springblog.service.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.davit.springblog.dto.TokenRequestDto;
import com.davit.springblog.dto.UserRequestDto;
import com.davit.springblog.dto.UserResponseDto;
import com.davit.springblog.entity.Role;
import com.davit.springblog.entity.Users;
import com.davit.springblog.execption.ResourceNotFoundException;
import com.davit.springblog.jwt.JWTUtils;
import com.davit.springblog.repository.UserRepository;
import com.davit.springblog.service.FileUploadService;
import com.davit.springblog.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository usersRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private ModelMapper modelMapper;

    @Value("${project.upload}")
    private String path;
    // @Value("${base.url}")
    // private String baseUrl;

    
    @Override
    public UserResponseDto register(UserRequestDto userRequestDto, MultipartFile file) throws IOException {
        Users newUser = new Users();
        newUser.setName(userRequestDto.getName());
        newUser.setRole(Role.USER);
        newUser.setEmail(userRequestDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        if (file != null && !file.isEmpty()) {
            String savedFile = fileUploadService.saveFile(file, path);
            newUser.setImageProfile(savedFile);
        }
        newUser.setCreated(LocalDateTime.now());
        newUser.setUpdated(LocalDateTime.now());
        newUser.setAttempt(0);
        newUser.setStatus("ACTIVE");

        Users savedUser = usersRepository.save(newUser);

        var jwt = jwtUtils.generateToken(savedUser);
        var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), savedUser);

        UserResponseDto response = modelMapper.map(savedUser, UserResponseDto.class);
        response.setAccessToken(jwt);
        response.setRefreshToken(refreshToken);
        response.setOurUsersList(List.of(savedUser));
        response.setLogin(true);
        response.setStatusCode(200);

        return response;
    }

    
    
    @Override
    public UserResponseDto login(UserRequestDto userRequestDto) {
        UserResponseDto response = new UserResponseDto();
        try {
            var user = usersRepository.findByEmail(userRequestDto.getEmail());
            
            if (user.isEmpty()) {
            	
                user = usersRepository.findByName(userRequestDto.getEmail());
            }

            if (user.isPresent()) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        user.get().getEmail(),
                        userRequestDto.getPassword()));
                user.get().setAttempt(0);
                user.get().setStatus("ACTIVE");
                usersRepository.save(user.get());

                var jwt = jwtUtils.generateToken(user.get());
                var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user.get());
                response.setAccessToken(jwt);
                response.setOurUsersList(List.of(user.get()));
                response.setRefreshToken(refreshToken);
                response.setLogin(true);
                response.setStatusCode(200);
            } else {
                throw new UsernameNotFoundException("User not found with email or name : " + userRequestDto.getEmail());
            }
        } catch (Exception e) {
            var user = usersRepository.findByEmail(userRequestDto.getEmail());
            
            if (user.isEmpty()) {
            	
                user = usersRepository.findByName(userRequestDto.getEmail());
            }
            user.ifPresent(u -> {
                u.setAttempt(u.getAttempt() + 1);
                if (u.getAttempt() >= 3) {
                    u.setStatus("LOCKED");
                }
                usersRepository.save(u);
            });
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }


    @Override
    public UserResponseDto refreshToken(TokenRequestDto tokenRequestDto) {
        UserResponseDto response = new UserResponseDto();
        try {
            String ourEmail = jwtUtils.extractUsername(tokenRequestDto.getRefreshToken());
            Users users = usersRepository.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(tokenRequestDto.getRefreshToken(), users)) {
                var jwt = jwtUtils.generateToken(users);
                response.setAccessToken(jwt);
                response.setRefreshToken(tokenRequestDto.getRefreshToken());
                response.setOurUsersList(List.of(users));
                response.setLogin(true);
                response.setStatusCode(200);
            }
            response.setStatusCode(200);
            return response;

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    @Override
    public UserResponseDto getAllUsers(String keyword, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Users> userPages = keyword == null || keyword.trim().isEmpty() ? usersRepository.findAll(pageable)
                : usersRepository.findByNameContainingIgnoreCase(keyword, pageable);

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setOurUsersList(userPages.getContent());
        responseDto.setPageNumber(pageNumber);
        responseDto.setPageSize(pageSize);
        responseDto.setTotalElements(userPages.getTotalElements());
        responseDto.setTotalPages(userPages.getTotalPages());
        responseDto.setLast(userPages.isLast());

        return responseDto;
    }

    @Override
    public UserResponseDto getUsersById(Integer id) {
        UserResponseDto reqRes = new UserResponseDto();
        try {
            Users usersById = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            reqRes.setOurUsers(usersById);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Users with id '" + id + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public UserResponseDto getMyInfo(String email) {
        UserResponseDto reqRes = new UserResponseDto();
        try {
            Optional<Users> userOptional = usersRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setOurUsers(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }

        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;
    }

    @Override
    public UserResponseDto update(Integer userId, UserRequestDto userRequestDto, MultipartFile file)
            throws IOException {
        Users existingUser = usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        String newSaveFile = null;
        try {
            if (file != null && !file.isEmpty()) {
                if (existingUser.getImageProfile() != null && !existingUser.getImageProfile().isEmpty()) {
                    Files.deleteIfExists(Paths.get(path, existingUser.getImageProfile()));
                }
                newSaveFile = fileUploadService.saveFile(file, path);
            } else {
                newSaveFile = existingUser.getImageProfile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Error occurred while updating user profile image", e);
        }

        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());
            existingUser.setPassword(encodedPassword);
        }

        existingUser.setName(userRequestDto.getName());
        existingUser.setEmail(userRequestDto.getEmail());
        existingUser.setRole(Role.valueOf(userRequestDto.getRole()));
        existingUser.setCreated(LocalDateTime.now());
        existingUser.setUpdated(LocalDateTime.now());
        existingUser.setImageProfile(newSaveFile);
        if (existingUser.getAttempt() == 0) {
            existingUser.setStatus("ACTIVE");
        }

        Users savedUser = usersRepository.save(existingUser);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    @Override
    public void delete(Integer userId) throws IOException {
        Users existingUsers = usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + userId));

        String uploadDirectory = path;
        String photoFileName = existingUsers.getImageProfile();

        if (photoFileName != null && !photoFileName.isEmpty()) {
            try {
                Files.deleteIfExists(Paths.get(uploadDirectory, photoFileName));
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException("Failed to delete photo", e);
            }
        }
        usersRepository.deleteById(userId);
    }
}
