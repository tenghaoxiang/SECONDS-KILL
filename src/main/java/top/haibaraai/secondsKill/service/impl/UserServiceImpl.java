package top.haibaraai.secondsKill.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.haibaraai.secondsKill.domain.User;
import top.haibaraai.secondsKill.mapper.UserMapper;
import top.haibaraai.secondsKill.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findById(int id) {
        return userMapper.findById(id);
    }

    @Override
    public User findByWxId(int id) {
        return userMapper.findByWxId(id);
    }

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Override
    public int save(User user) {
        return userMapper.save(user);
    }
}
