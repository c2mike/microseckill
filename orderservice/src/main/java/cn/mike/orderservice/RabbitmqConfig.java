package cn.mike.orderservice;

import cn.mike.orderservice.mapper.MsgLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
    public static final Logger logger = LoggerFactory.getLogger(RabbitmqConfig.class);
    @Bean
    Queue queue()
    {
        return new Queue("microseckill_queue");
    }

    @Bean
    DirectExchange directExchange()
    {
        return new DirectExchange("microseckill_exchange",true,false);
    }

    @Bean
    Binding bind()
    {
        return BindingBuilder.bind(queue()).to(directExchange()).with("microseckill_key");
    }

    @Autowired
    MsgLogMapper msgLogMapper;

    @Autowired
    CachingConnectionFactory cacheingConnectFactory;

    @Bean
    public RabbitTemplate rabbitTemplate()
    {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cacheingConnectFactory);
        rabbitTemplate.setConfirmCallback((data,ack,cause)->{
            Long id = Long.parseLong(data.getId());
            if(ack)
            {
                msgLogMapper.sendSuccess(id);
            }
            else{
                logger.error("message {} send error",id );
            }
        });
        rabbitTemplate.setReturnCallback((msg, repCode, repText, exchange, routingkey)->{
                logger.error("message to queue error {}",msg);
        });

        return rabbitTemplate;
    }
//    @Bean
//    Queue Queue()
//    {
//        return new Queue("direct_queue");
//    }
//
//    @Bean
//    Queue Queue2()
//    {
//        return new Queue("direct_queue2");
//    }
//
//    @Bean
//    DirectExchange DirectExchange()
//    {
//        return new DirectExchange("direct_exchange");
//    }
//
//    @Bean
//    Binding binding()
//    {
//        return BindingBuilder.bind(Queue()).to(DirectExchange()).with("direct_key");
//    }
//
//    @Bean
//    Binding binding2()
//    {
//        return BindingBuilder.bind(Queue()).to(DirectExchange()).with("direct_key");
//    }
//    @Bean
//    Queue fanoutQueue1()
//    {
//        return new Queue("fanout1");
//    }
//
//    @Bean
//    Queue fanoutQueue2()
//    {
//        return new Queue("fanout2");
//    }
//
//    @Bean
//    FanoutExchange fanoutExchange()
//    {
//        return new FanoutExchange("fanoutExchange",true,false);
//    }
//
//    @Bean
//    Binding bindingFanout()
//    {
//        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
//    }
//
//    @Bean
//    Binding bingdingFanout2()
//    {
//        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
//    }
//
//    @Bean
//    Queue huawei()
//    {
//        return new Queue("huawei");
//    }
//
//    @Bean
//    Queue phone()
//    {
//        return new Queue("phone");
//    }
//
//    @Bean
//    TopicExchange topicExchange()
//    {
//        return new TopicExchange("topicExchange");
//    }
//
//    @Bean
//    Binding BindPhone()
//    {
//        return BindingBuilder.bind(phone()).to(topicExchange()).with("#.phone");
//    }
//
//    @Bean
//    Binding BindHuawei()
//    {
//        return BindingBuilder.bind(huawei()).to(topicExchange()).with("huawei.#");
//    }
}
