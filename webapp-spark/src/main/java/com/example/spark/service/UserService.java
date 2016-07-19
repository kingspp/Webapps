package com.example.spark.service;

import com.example.spark.pojo.User;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by prathyushsp on 18/07/16.
 */
public class UserService {

    public static User getUserByID(Integer id){
        List<User> list = getUserList();
        for(User u:list){
            if(u.getId().equals(id)) return u;
        }
        return null;
    }

    public static List<User> getUserList(){
        List<User> list = new ArrayList<>();
        list.add(new User(1,"admin","admin@123"));
        return list;
    }
}
