package cn.mike.dbwrite;

import cn.mike.dbwrite.mapper.MsgLogMapper;
import cn.mike.dbwrite.mapper.TbOrderMapper;
import cn.mike.dbwrite.mapper.TbSkuMapper;
import com.rabbitmq.client.Channel;
import commons.bean.MsgContent;
import commons.bean.MsgLog;
import commons.bean.TbOrder;
import design.RedisKeyImpl;
import design.SeckillRedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class WriteOrder {
    final static Logger logger = LoggerFactory.getLogger(WriteOrder.class);
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    TbOrderMapper tbOrderMapper;
    @Autowired
    TbSkuMapper tbSkuMapper;
    @Autowired
    MsgLogMapper msgLogMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;
    @RabbitListener(queues="microseckill_queue")
    public void hander(Message message, Channel channel) throws Exception {
        MsgContent content = (MsgContent) message.getPayload();
        MessageHeaders messageHeaders = message.getHeaders();
        Long o = (Long) messageHeaders.get(AmqpHeaders.DELIVERY_TAG);
        SetOperations setOperations = redisTemplate.opsForSet();
        String key = null;
        try{

        SeckillRedisKey seckillRedisKey = new RedisKeyImpl();
        key = seckillRedisKey.getSetKey("1");

        if(setOperations.isMember(key,""+content.getMsgId()))
        {
            logger.error("重复消费");
            channel.basicAck(o,false);
            return;
        }

        updateDb(content);


        }
        catch (Exception e)
        {
            logger.error("订单消费失败");
        }
        setOperations.add(key,""+content.getMsgId());
        channel.basicAck(o,false);
    }
    @Transactional
    public void updateDb(MsgContent msgContent) throws Exception {
        int result = tbSkuMapper.decrement(msgContent.getSkuId());
        TbOrder tbOrder = new TbOrder();
        tbOrder.setSkuId(msgContent.getSkuId());
        tbOrder.setUserPhone(msgContent.getPhone());
        tbOrderMapper.insertSelective(tbOrder);
    }

    @Scheduled(cron = "0/10 * * * * ? ")
    public void task()
    {
        List<MsgLog> data = msgLogMapper.findLog();
        for(MsgLog msgLog:data)
        {
            msgLog.setCount(msgLog.getCount()+1);
            msgLog.setTryTime(new Date(System.currentTimeMillis()+60*1000));
            msgLogMapper.updateByPrimaryKeySelective(msgLog);
            MsgContent msgContent = new MsgContent();
            msgContent.setMsgId(msgLog.getMsgId());
            msgContent.setPhone(msgLog.getPhone());
            msgContent.setSkuId(msgLog.getSkuId());
            rabbitTemplate.convertAndSend("microseckill_exchange","microseckill_key",msgContent,new CorrelationData(""+msgLog.getMsgId()));

        }
    }

    public void writeMsgLog(long sid, String phone, int msgId)
    {
        MsgLog msgLog = new MsgLog();
        msgLog.setMsgId(msgId);
        msgLog.setSkuId(sid);
        msgLog.setPhone(phone);
        msgLog.setCount(msgLog.getCount()+1);
        msgLog.setCreateTime(new Date());
        msgLog.setExchange("microseckill_exchange");
        msgLog.setRouteKey("microseckill_key");
        msgLog.setStatus(0);
        msgLog.setTryTime(new Date(System.currentTimeMillis()+60*1000));
        MsgContent msgContent = new MsgContent();
        msgLogMapper.insertSelective(msgLog);
    }

}
