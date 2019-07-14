package top.haibaraai.secondsKill.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.haibaraai.secondsKill.mapper.FlagMapper;
import top.haibaraai.secondsKill.service.FlagService;

@Service
public class FlagServiceImpl implements FlagService {

    @Autowired
    private FlagMapper flagMapper;

    @Override
    public int selectByQueue(String queue) {
        return flagMapper.selectByQueue(queue);
    }

    @Override
    public int save(String queue, int flag) {
        return flagMapper.save(queue, flag);
    }

    @Override
    public int update(String queue, int flag) {
        return flagMapper.update(queue, flag);
    }
}
