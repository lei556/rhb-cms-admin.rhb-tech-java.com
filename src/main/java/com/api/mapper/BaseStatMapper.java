package com.api.mapper;

import com.api.entity.BaseStat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author leishuiping
* @description 针对表【book】的数据库操作Mapper
* @createDate 2023-06-21
* @Entity com.api.entity.BaseStat
*/
public interface BaseStatMapper extends BaseMapper<BaseStat> {

    String getByDay(@Param("stypeSql") String stypeSql, @Param("date") String date, @Param("countOrTimeSql") String countOrTimeSql);

    String getByMonths(@Param("stypeSql") String stypeSql, @Param("date") String date, @Param("countOrTimeSql") String countOrTimeSql);

    int getPatientCountByTimeRange(@Param("startDocTime") String startDocTime, @Param("endDocTime") String endDocTime);

    int getTrianPatientCountByTimeRange(@Param("startTrainLogTime") String startTrainLogTime, @Param("endTrainLogTime") String endTrainLogTime);

    int getTrianAllCountByTimeRange(@Param("startTrainLogTime") String startTrainLogTime, @Param("endTrainLogTime") String endTrainLogTime);

    int getTrianAllSecondByTimeRange(@Param("startTrainLogTime") String startTrainLogTime, @Param("endTrainLogTime") String endTrainLogTime);
}




