package cn.mike.orderservice;

import cn.mike.orderservice.mapper.MsgLogMapper;
import cn.mike.orderservice.mapper.TbLimitPolicyMapper;
import cn.mike.orderservice.mapper.TbOrderMapper;
import com.netflix.discovery.converters.Auto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import commons.bean.MsgContent;
import commons.bean.MsgLog;
import commons.bean.RespBean;
import commons.bean.TbLimitPolicy;
import design.RedisKeyImpl;
import design.SeckillRedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService {
    final static Logger logger = LoggerFactory.getLogger(OrderService.class);
    String salt = "abc";
    @Autowired
    TbLimitPolicyMapper tbLimitPolicyMapper;
    @Autowired
    TbOrderMapper tbOrderMapper;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    RabbitTemplate amqpTemplate;
    @Autowired
    AtomicInteger atomicInteger;
    @Autowired
    MsgLogMapper msgLogMapper;

    Map<String,Boolean> end = new ConcurrentHashMap<>();
    public RespBean md5(String phone, Long sid) {
       TbLimitPolicy tblLimitPolicy =  tbLimitPolicyMapper.getPolice(sid);
       if(tblLimitPolicy==null)
       {
           return RespBean.Error("秒杀未开始");
       }
       RespBean respBean = RespBean.Ok("获取接口成功");
       Random random = new Random();
       String origin = phone+salt+""+random.nextInt(1000);
       String md5 = DigestUtils.md5DigestAsHex((phone+salt).getBytes());
       respBean.setObj(md5);
       SeckillRedisKey seckillRedisKey = new RedisKeyImpl();
       ValueOperations valueOperations = redisTemplate.opsForValue();
       valueOperations.set(seckillRedisKey.getMd5Key(Long.parseLong(phone)),md5,30*60, TimeUnit.SECONDS);
       return respBean;
    }

    @HystrixCommand(fallbackMethod = "writeMsgError")
    public RespBean order(long sid, String phone, String md5)
    {
        SeckillRedisKey seckillRedisKey = new RedisKeyImpl();
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object t = valueOperations.get(seckillRedisKey.getMd5Key(Long.parseLong(phone)));
        if(t==null||!((String)t).equals(md5))
        {
            return RespBean.Error("校验失败");
        }
        redisTemplate.delete(seckillRedisKey.getMd5Key(Long.parseLong(phone)));
        SeckillRedisKey redisKey = new RedisKeyImpl();
        HashOperations hashOperations = redisTemplate.opsForHash();
        SetOperations setOperations = redisTemplate.opsForSet();
        String key1 = redisKey.getBeginDateKey();
        String key2 = redisKey.getEndDateKey();
        String key3 = redisKey.getKey(sid);
        String key4 = redisKey.getMountKey();
        long d1 = Long.parseLong((String) hashOperations.get(key3,key1));
        long d2 = Long.parseLong((String) hashOperations.get(key3,key2));
        long time = System.currentTimeMillis();
        if(d1>time||d2<time)
        {
            return RespBean.Error("秒杀未开启或者已结束");
        }
        if(end.containsKey(key3)&&end.get(key3))
        {
            return RespBean.Error("卖完了");
        }
        if(setOperations.isMember(redisKey.getSeckillSetKey(sid),phone))
        {
            return RespBean.Error("重复秒杀");
        }
        Long aLong = hashOperations.increment(key3, redisKey.getMountKey(), -1);
        if(aLong==0)
        {
            end.put(redisKey.getKey(sid),true);
        }
        MsgContent msgContent = new MsgContent();
        msgContent.setMsgId(atomicInteger.incrementAndGet());
        msgContent.setPhone(phone);
        msgContent.setSkuId(sid);
        int i = 1/0;
        amqpTemplate.convertAndSend("microseckill_exchange","microseckill_key",msgContent,new CorrelationData(""+msgContent.getMsgId()));
        restTemplate.postForObject("http://dbServer/dbwrite",msgContent,Void.class);
        setOperations.add(redisKey.getSeckillSetKey(sid),phone);
        return RespBean.Ok("下单成功");
    }

    public RespBean writeMsgError(long sid, String phone, String md5,Throwable t)
    {
        logger.error("sid:{},phone:{},md5:{},message:{}",sid,phone,md5,t.getMessage());
        return RespBean.Error("调用出错");
    }




}
