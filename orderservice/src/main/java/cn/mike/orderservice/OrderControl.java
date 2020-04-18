package cn.mike.orderservice;


import commons.bean.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class OrderControl {

    @Autowired
    OrderService orderService;

    @GetMapping("/hello")
    public String hello()
    {
        return "";
    }

    @GetMapping("/md5")
    public RespBean md5(String phone, Long sid)
    {
        return orderService.md5(phone,sid);
    }

    @PostMapping("/order")
    public RespBean order(String phone,Long sid,String md5)
    {
        return orderService.order(sid,phone,md5);
    }



}
