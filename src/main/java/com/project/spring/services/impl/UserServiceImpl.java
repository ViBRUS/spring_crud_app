package com.project.spring.services.impl;

import com.project.spring.entities.User;
import com.project.spring.exceptions.ResourceNotFoundException;
import com.project.spring.payloads.UserDto;
import com.project.spring.repositories.UserRepo;
import com.project.spring.services.UserService;
import com.project.spring.transformers.UserDtoTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;


    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserDtoTransformer.dtoToUserEntity(userDto);
        User savedUser = this.userRepo.save(user);
        return UserDtoTransformer.UserEntityToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " id ", userId));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());

        User updatedUser = this.userRepo.save(user);
        return UserDtoTransformer.UserEntityToDto(updatedUser);
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " id ", userId));
        return UserDtoTransformer.UserEntityToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> usersList = this.userRepo.findAll();
        return usersList.stream().map(UserDtoTransformer::UserEntityToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " id ", userId));
        this.userRepo.delete(user);
    }
}
