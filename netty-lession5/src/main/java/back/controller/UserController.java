package back.controller;

import back.annotation.Controller;
import back.annotation.RequestMapping;
import io.netty.handler.codec.http.HttpMethod;

@Controller("/user")
public class UserController {

    @RequestMapping(method = "GET", value = "/getUser")
    public User getUser(){
        return User.defaultUser();
    }
}
