package cn.mike.orderservice.mapper;

import commons.bean.TbOrder;
import org.apache.ibatis.annotations.Param;

public interface TbOrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(TbOrder record);

    int insertSelective(TbOrder record);

    TbOrder selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(TbOrder record);

    int updateByPrimaryKey(TbOrder record);

    TbOrder selectByPhoneAndSid(@Param("phone") String phone,@Param("sid") long sid);
}