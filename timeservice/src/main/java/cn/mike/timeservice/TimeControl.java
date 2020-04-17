package cn.mike.timeservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class TimeControl {
    @Value("${server.port}")
    private int port;
    @GetMapping("/time")
    public long getTime()
    {
        System.out.println(port);
        return System.currentTimeMillis();
    }
}
