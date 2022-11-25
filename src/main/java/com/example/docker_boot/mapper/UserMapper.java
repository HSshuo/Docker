package com.example.docker_boot.mapper;

import com.example.docker_boot.entity.User;
import org.apache.ibatis.annotations.*;

/**
 * @author SHshuo
 * @data 2022/11/25--16:48
 */
@Mapper
public interface UserMapper {

    @Insert("insert into user(username, password, sex) values(#{user.username}, #{user.password}, #{user.sex})")
    Integer insertSelective(@Param("user")User user);

    @Select("select * from user where id = #{id} and deleted = 0")
    User selectByPrimaryKey(@Param("id")Integer id);

    @Select("select * from user where username = #{username} and deleted = 0")
    User selectByUsername(@Param("username")String username);

    @Update("update user set deleted = 1 where id = #{id}")
    void deleteByPrimaryKey(@Param("id")Integer id);

    @Update("update user set password = #{user.password} where username = #{user.username}")
    void updateByUsername(@Param("user")User user);
}
