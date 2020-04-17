package cn.mike.orderservice.mapper;

import commons.bean.TbLimitPolicy;

public interface TbLimitPolicyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TbLimitPolicy record);

    int insertSelective(TbLimitPolicy record);

    TbLimitPolicy selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbLimitPolicy record);

    int updateByPrimaryKey(TbLimitPolicy record);

    TbLimitPolicy getPolice(Long sid);
}