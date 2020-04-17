package design;

import commons.bean.TbLimitPolicy;

public abstract class SeckillRedisKey {
    abstract String caculate(long origin);

    abstract String caculateSet(String origin);

    public final String getKey(long origin)
    {
        return caculate(origin);
    }

    abstract  String caculateMountKet();

    public final String getMountKey()
    {
        return caculateMountKet();
    }

    abstract  String caculateBeginDateKey();

    public final String getBeginDateKey()
    {
        return caculateBeginDateKey();
    }

    abstract  String caculateEndDateKey();

    public final String getEndDateKey()
    {
        return caculateEndDateKey();
    }

    abstract String caculateMd5Key(long origin);


    public final String getMd5Key(long origin)
    {
        return caculateMd5Key(origin);
    }

    public final String getSetKey(String origin){
        return "order_set";
    }

    public final String getSeckillSetKey(Long sid)
    {
        return "seckill_key_set"+sid;
    }


}
