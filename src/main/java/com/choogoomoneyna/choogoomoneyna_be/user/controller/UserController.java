package com.choogoomoneyna.choogoomoneyna_be.user.controller;

import com.choogoomoneyna.choogoomoneyna_be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

}
