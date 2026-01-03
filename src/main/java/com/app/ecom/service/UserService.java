package com.app.ecom.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.app.ecom.dto.AddressDTO;
import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.model.Address;
import com.app.ecom.repository.UserRepository;
import com.app.ecom.model.User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;


    public List<UserResponse> fetchAllUsers(){

        return repository.findAll().stream()
                .map(this::mapTOUserResponse)
                .collect(Collectors.toList());
    }

    public void addUser(UserRequest userRequest) {
        User user = new User();
        updateUserFromRequest(user, userRequest);
        repository.save(user);
    }



    public Optional<UserResponse> fetchUser(Long id) {
        return repository.findById(id)
                .map(this::mapTOUserResponse);
    }

    public boolean updateUser(Long id, UserRequest updatedUserRequest){
        return repository.findById(id)
            .map(existingUser -> {
                updateUserFromRequest(existingUser, updatedUserRequest);
                repository.save(existingUser);
                return true;
            }).orElse(false);
        
    }

    private UserResponse mapTOUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setRole(user.getRole());

        if (user.getAddress() !=null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipcode(user.getAddress().getZipcode());
            userResponse.setAddress(addressDTO);
        }
        return userResponse;
    }

    private void updateUserFromRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        if (userRequest.getAddress()!= null) {
            Address address = new Address();
            address.setStreet(userRequest.getAddress().getStreet());
            address.setCity(userRequest.getAddress().getCity());
            address.setState(userRequest.getAddress().getState());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setZipcode(userRequest.getAddress().getZipcode());
            user.setAddress(address);
        }
    }

}
