package top.haibaraai.secondsKill.service;

public interface FlagService {

    int selectByQueue(String queue);

    int save(String queue, int flag);

    int update(String queue, int flag);

}
