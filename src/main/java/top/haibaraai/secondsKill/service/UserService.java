package top.haibaraai.secondsKill.service;

import top.haibaraai.secondsKill.domain.User;

import java.util.List;

public interface UserService {

    User findById(int id);

    User findByWxId(int id);

    List<User> findAll();

    int save(User user);

}
