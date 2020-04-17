package design;

import commons.bean.TbLimitPolicy;

public class RedisKeyImpl extends SeckillRedisKey {
    String caculate(long origin) {
        return "seckill"+origin;
    }

    String caculateSet(String origin) {
        return origin;
    }

    String caculateMountKet() {
        return "mount";
    }

    String caculateBeginDateKey() {
        return "beginDate";
    }

    String caculateEndDateKey() {
        return "endDate";
    }

    String caculateMd5Key(long origin) {
        return "user"+origin;
    }
}
