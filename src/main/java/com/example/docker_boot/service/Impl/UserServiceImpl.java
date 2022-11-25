package com.example.docker_boot.service.Impl;

import com.example.docker_boot.entity.User;
import com.example.docker_boot.mapper.UserMapper;
import com.example.docker_boot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author SHshuo
 * @data 2022/11/25--16:50
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    public static final String CACHE_KEY_USER = "user:";

    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * addUser
     * @param user
     */
    public void addUser(User user)
    {
        //1 先插入mysql并成功
        Integer i = userMapper.insertSelective(user);

        if(i > 0)
        {
            //2 需要再次查询一下mysql将数据捞回来并ok
            user = userMapper.selectByUsername(user.getUsername());
            //3 将捞出来的user存进redis，完成新增功能的数据一致性。
            String key = CACHE_KEY_USER + user.getId();
            redisTemplate.opsForValue().set(key, user);
        }
    }

    /**
     * findUserById
     * @param id
     * @return
     */
    public User findUserById(Integer id)
    {
        User user = null;
        String key = CACHE_KEY_USER + id;

        //1 先从redis里面查询，如果有直接返回结果，如果没有再去查询mysql
        user = (User) redisTemplate.opsForValue().get(key);

        if(user == null)
        {
            //2 redis里面无，继续查询mysql
            user = userMapper.selectByPrimaryKey(id);
            if(user == null)
            {
                //3.1 redis+mysql 都无数据
                //你具体细化，防止多次穿透，我们规定，记录下导致穿透的这个key回写redis
                return user;
            }else{
                //3.2 mysql有，需要将数据写回redis，保证下一次的缓存命中率
                redisTemplate.opsForValue().set(key,user);
            }
        }
        return user;
    }


    @Override
    public void deleteUser(Integer id) {
        userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateByUsername(user);
    }

}
