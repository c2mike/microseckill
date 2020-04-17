package cn.mike.stockservice.control;

import cn.mike.stockservice.service.StockService;
import commons.bean.RespBean;
import commons.bean.TbLimitPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class StockControl {
    @Autowired
    StockService stockService;

    @GetMapping("/hello")
    public String hello()
    {
        return "hello";
    }

    @GetMapping("/politicals")
    public RespBean politicals()
    {
        return stockService.getPoliticals();
    }

    @PostMapping("/political")
    public RespBean political(TbLimitPolicy Political)
    {
        return stockService.political(Political);
    }


}
