package cn.mike.dbwrite.mapper;

import commons.bean.MsgLog;

import java.util.List;

public interface MsgLogMapper {
    int deleteByPrimaryKey(Integer msgId);

    int insert(MsgLog record);

    int insertSelective(MsgLog record);

    MsgLog selectByPrimaryKey(Integer msgId);

    int updateByPrimaryKeySelective(MsgLog record);

    int updateByPrimaryKey(MsgLog record);

    List<MsgLog> findLog();

    void sendSuccess(Long id);
}