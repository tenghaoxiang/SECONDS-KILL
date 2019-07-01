package top.haibaraai.secondsKill.service;

import top.haibaraai.secondsKill.domain.User;

public interface UserService {

    User save(String code);

    User findById(int id);

}
