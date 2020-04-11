package back.controller;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private Integer age;

    public static User defaultUser(){
        User user = new User();
        user.id = 100L;
        user.name = "back";
        user.age = 27;
        return user;
    }
}
