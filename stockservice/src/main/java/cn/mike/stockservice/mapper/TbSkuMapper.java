package cn.mike.stockservice.mapper;

import commons.bean.TbSku;

import java.util.List;

public interface TbSkuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TbSku record);

    int insertSelective(TbSku record);

    TbSku selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbSku record);

    int updateByPrimaryKey(TbSku record);


}