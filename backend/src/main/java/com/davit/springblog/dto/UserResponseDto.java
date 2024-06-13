package com.davit.springblog.dto;

import com.davit.springblog.entity.Users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponseDto {

  private int statusCode;
  private String message;
  private String accessToken;
  private String refreshToken;

  private Users ourUsers;
  private List<Users> ourUsersList;

  // Pagination fields
  private Integer pageNumber;
  private Integer pageSize;
  private Long totalElements;
  private int totalPages;
  private  boolean isLast;

  // Search field
  private String searchKeyword;
  
  // is login true or false
  private boolean Login;
}
