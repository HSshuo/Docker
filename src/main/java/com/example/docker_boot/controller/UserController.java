package com.example.docker_boot.controller;

import cn.hutool.core.util.IdUtil;
import com.example.docker_boot.entity.DTO.UserDTO;
import com.example.docker_boot.entity.User;
import com.example.docker_boot.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @author SHshuo
 * @data 2022/11/25--17:12
 */
@Api(description = "用户User接口")
@RestController
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    @ApiOperation("数据库新增3条记录")
    @RequestMapping(value = "/user/add")
    public void addUser() {
        for (int i = 1; i <=3; i++) {
            User user = new User();
            user.setUsername("hshuo" + i);
            user.setPassword(IdUtil.simpleUUID().substring(0, 6));
            user.setSex((byte) new Random().nextInt(2));
            userService.addUser(user);
        }
    }

    @ApiOperation("删除1条记录")
    @RequestMapping(value = "/user/delete/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }

    @ApiOperation("修改1条记录")
    @RequestMapping(value = "/user/update")
    public void updateUser(@RequestBody UserDTO userDTO)
    {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        userService.updateUser(user);
    }

    @ApiOperation("查询1条记录")
    @RequestMapping(value = "/user/find/{id}",method = RequestMethod.GET)
    public User findUserById(@PathVariable Integer id)
    {
        return userService.findUserById(id);
    }
}

