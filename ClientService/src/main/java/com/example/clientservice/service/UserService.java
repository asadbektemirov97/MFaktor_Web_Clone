package com.example.clientservice.service;

import com.example.clientservice.entity.AdsSource;
import com.example.clientservice.entity.Payment;
import com.example.clientservice.entity.PaymentType;
import com.example.clientservice.entity.User;
import com.example.clientservice.feignClient.CatalogFeignClient;
import com.example.clientservice.payload.ApiResponse;
import com.example.clientservice.payload.PaymentDto;
import com.example.clientservice.payload.UserDto;
import com.example.clientservice.repository.PaymentRepository;
import com.example.clientservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CatalogFeignClient catalogFeignClient;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    public ApiResponse addUser(UserDto user) {
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            return new ApiResponse("User with this phoneNumber already exists", false);
        }
        User addUser = new User();
        addUser.setFirstName(user.getFirstName());
        addUser.setLastName(user.getLastName());
        addUser.setGender(user.getGender());
        addUser.setOrganization(user.getOrganization());
        addUser.setPassword(user.getPassword());
        addUser.setPhoneNumber(user.getPhoneNumber());
        ApiResponse<AdsSource> response = catalogFeignClient.getAdsSource(user.getAdsSourceId());
        AdsSource adsSource = response.getObject();

        addUser.setAdsSource(adsSource);
        User save = userRepository.save(addUser);
        return new ApiResponse("Registered", true);
    }

    public ApiResponse paymentUser(UserDto user, PaymentDto paymentDto) {
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            return new ApiResponse("User with this phoneNumber already exists", false);
        }
        User addUser = new User();
        addUser.setFirstName(user.getFirstName());
        addUser.setLastName(user.getLastName());
        addUser.setGender(user.getGender());
        addUser.setOrganization(user.getOrganization());
        addUser.setPassword(user.getPassword());
        addUser.setPhoneNumber(user.getPhoneNumber());
        ApiResponse one = catalogFeignClient.getAdsSource(user.getAdsSourceId());
        AdsSource object = (AdsSource) one.getObject();
        addUser.setAdsSource(object);
        User save = userRepository.save(addUser);

        Payment payment = new Payment();
        payment.setAmount(paymentDto.getAmount());

        ApiResponse apiResponse = catalogFeignClient.getPaymentType(paymentDto.getPaymentTypeId());
        payment.setPaymentType((PaymentType) apiResponse.getObject());
        payment.setUser(addUser);
        payment.setDate(paymentDto.getDate());
        Payment save1 = paymentRepository.save(payment);
        return new ApiResponse("Payment successfully fullfilled", true, save1);
    }

    public ApiResponse editAppliedUser(Long id, UserDto userDto) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty()) {
            return new ApiResponse("No user found", false);
        }
        User user = byId.get();

        ApiResponse apiResponse = catalogFeignClient.getAdsSource(userDto.getAdsSourceId());
        user.setAdsSource((AdsSource) apiResponse.getObject());
        user.setGender(userDto.getGender());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setOrganization(userDto.getOrganization());
        user.setPhoneNumber(userDto.getPhoneNumber());
        User save = userRepository.save(user);
        return new ApiResponse("User successfully edited", true);

    }

    public ApiResponse getAll() {
        List<User> all = userRepository.findAll();
        return new ApiResponse("All users", true, all);
    }

    public ApiResponse getAllByChatId() {
        List<User> userList = userRepository.findAllByChatIdNotNull();
        return new ApiResponse("All users By ChatId", true, userList);
    }
}
