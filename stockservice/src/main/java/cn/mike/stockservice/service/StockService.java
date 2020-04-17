package cn.mike.stockservice.service;

import cn.mike.stockservice.mapper.TbLimitPolicyMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.bean.RespBean;
import commons.bean.TbLimitPolicy;
import design.RedisKeyImpl;
import design.SeckillRedisKey;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class StockService {
    @Autowired
    TbLimitPolicyMapper tbLimitPolicyMapper;
    @Autowired
    StringRedisTemplate redisStringTemplate;
    @Transactional
    public RespBean political(TbLimitPolicy seckillPolitical) {
        tbLimitPolicyMapper.deleteBySkuId(seckillPolitical.getSkuId());
        int result = tbLimitPolicyMapper.insertSelective(seckillPolitical);
        if(result!=1)
        {
            return RespBean.Error("更新失败");
        }
        SeckillRedisKey seckillRedisKey = new RedisKeyImpl();
        String key = seckillRedisKey.getKey(seckillPolitical.getSkuId());
        redisStringTemplate.delete(key);
        HashOperations hashOperations =redisStringTemplate.opsForHash();
        long diff = seckillPolitical.getEndTime().getTime()-System.currentTimeMillis();
        hashOperations.put(key,seckillRedisKey.getMountKey(), ""+seckillPolitical.getQuanty());
        hashOperations.put(key,seckillRedisKey.getBeginDateKey(),""+seckillPolitical.getBeginTime().getTime());
        hashOperations.put(key,seckillRedisKey.getEndDateKey(),""+seckillPolitical.getEndTime().getTime());

        return RespBean.Ok("添加成功");

    }

    public RespBean getPoliticals() {
        List<TbLimitPolicy> data = tbLimitPolicyMapper.getPolitical();
        for(TbLimitPolicy tbLimitPolicy:data)
        {
            SeckillRedisKey seckillRedisKey = new RedisKeyImpl();
            String key = seckillRedisKey.getKey(tbLimitPolicy.getSkuId());
            HashOperations hashOperations = redisStringTemplate.opsForHash();
            tbLimitPolicy.setQuanty(Long.parseLong((String) hashOperations.get(key,seckillRedisKey.getMountKey())));
        }
        RespBean respBean = RespBean.Ok("获取秒杀列表成功",data);
        return  respBean;
    }
}
