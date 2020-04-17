package cn.mike.stockservice.mapper;

import commons.bean.TbLimitPolicy;

import java.util.List;

public interface TbLimitPolicyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TbLimitPolicy record);

    int insertSelective(TbLimitPolicy record);

    TbLimitPolicy selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbLimitPolicy record);

    int updateByPrimaryKey(TbLimitPolicy record);

    int deleteBySkuId(Long id);

    List<TbLimitPolicy> getPolitical();
}