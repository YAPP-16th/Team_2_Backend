package com.yapp.ios2.controller;

import com.yapp.ios2.dto.*;
import com.yapp.ios2.service.AwsSnsService;
import com.yapp.ios2.service.S3Service;
import com.yapp.ios2.service.SnsService;
import com.yapp.ios2.service.UserService;
import com.yapp.ios2.vo.User;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    SnsService snsService;



    @PostMapping(value = "/user/join")
    @ResponseBody
    public User join(@RequestBody JoinDto joinInfo) {

        User newUser = userService.join(joinInfo.getEmail(), joinInfo.getName(), joinInfo.getPassword(), joinInfo.getPhone());

        return newUser;
    }

    @PostMapping(value = "/user/login")
    @ResponseBody
    public User login(@RequestBody LoginDto loginInfo){

        User user = userService.login(loginInfo.getEmail(), loginInfo.getPassword());

        return user;
    }

    @PostMapping("/user/duplicatedEmail")
    @ResponseBody
    public BooleanResultDto duplicatedEmail(@RequestBody DuplicatedEmailDto duplicatedEmailDto){

        BooleanResultDto booleanResultDto = new BooleanResultDto();

        boolean duplicated = userService.duplicatedEmail(duplicatedEmailDto.getEmail());

        booleanResultDto.setResult(duplicated);

        return booleanResultDto;
    }

    @PostMapping("/user/sms")
    @ResponseBody
    public SmsResponseDto sendSms(@RequestBody SmsRequestDto smsRequestDto){

        String num = snsService.send(smsRequestDto.getPhoneNumber());

        SmsResponseDto smsResponseDto = new SmsResponseDto();

        smsResponseDto.setNum(num);

        return smsResponseDto;

    }
}
