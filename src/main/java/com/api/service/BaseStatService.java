package com.api.service;

import com.api.entity.BaseStat;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author leishuiping
* @description 针对表【book】的数据库操作Service
* @createDate 2023-06-25
*/
public interface BaseStatService extends IService<BaseStat> {

    String getByDay(String stypeSql, String date, String countOrTimeSql);

    String getByMonths(String stypeSql, String date, String countOrTimeSql);

    int getPatientCountByTimeRange(String startDocTime, String endDocTime);

    int getTrianPatientCountByTimeRange(String startTrainLogTime, String endTrainLogTime);

    int getTrianAllCountByTimeRange(String startTrainLogTime, String endTrainLogTime);

    int getTrianAllSecondByTimeRange(String startTrainLogTime, String endTrainLogTime);
}
