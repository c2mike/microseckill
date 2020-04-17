package cn.mike.orderservice.mapper;

import commons.bean.MsgLog;

public interface MsgLogMapper {
    int deleteByPrimaryKey(Integer msgId);

    int insert(MsgLog record);

    int insertSelective(MsgLog record);

    MsgLog selectByPrimaryKey(Integer msgId);

    int updateByPrimaryKeySelective(MsgLog record);

    int updateByPrimaryKey(MsgLog record);

    void sendSuccess(Long id);
}