package com.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.api.entity.BaseStat;
import com.api.service.BaseStatService;
import com.api.mapper.BaseStatMapper;
import org.springframework.stereotype.Service;

/**
* @author leishuiping
* @description 针对表【book】的数据库操作Service实现
* @createDate 2023-06-25
*/
@Service
public class BaseStatServiceImpl extends ServiceImpl<BaseStatMapper, BaseStat>
    implements BaseStatService{

    @Override
    public String getByDay(String stypeSql, String date, String countOrTimeSql) {

        return baseMapper.getByDay(stypeSql,date,countOrTimeSql);
    }

    @Override
    public String getByMonths(String stypeSql, String date, String countOrTimeSql) {
        return  baseMapper.getByMonths(stypeSql,date,countOrTimeSql);
    }

    @Override
    public int getPatientCountByTimeRange(String startDocTime, String endDocTime) {
        return baseMapper.getPatientCountByTimeRange(startDocTime,endDocTime);
    }

    @Override
    public int getTrianPatientCountByTimeRange(String startTrainLogTime, String endTrainLogTime) {
        return baseMapper.getTrianPatientCountByTimeRange(startTrainLogTime,endTrainLogTime);
    }

    @Override
    public int getTrianAllCountByTimeRange(String startTrainLogTime, String endTrainLogTime) {
        return baseMapper.getTrianAllCountByTimeRange(startTrainLogTime,endTrainLogTime);
    }

    @Override
    public int getTrianAllSecondByTimeRange(String startTrainLogTime, String endTrainLogTime) {
        return baseMapper.getTrianAllSecondByTimeRange(startTrainLogTime,endTrainLogTime);

    }
}




