package cn.mike.timeservice;

import commons.bean.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.Date;

@RestController
public class TimeControl {
    @Value("${server.port}")
    private int port;
    @Autowired
    RestTemplate restTemplate;
    @GetMapping("/time")
    public long getTime()
    {
        System.out.println(port);
        return System.currentTimeMillis();
    }

    @GetMapping("/invoke")
    public void invoke()
    {
        File file = new File("D:/test.txt");
        File file2 = new File("D:/order.txt");
        if(!file2.exists())
        {
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileReader reader = new FileReader(file);
            FileWriter writer = new FileWriter(file2);
            BufferedReader bufferedReader = new BufferedReader(reader);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            String line;
            while((line=bufferedReader.readLine())!=null)
            {
                String[] strings = line.split(",");
                String phone = strings[0];
                Long sid = Long.valueOf(strings[1]);
                RespBean respBean =
                restTemplate.getForObject("http://orderServer/md5?phone={1}&sid={2}",RespBean.class,phone,sid);
                String md5 = (String) respBean.getObj();
                String line2 = phone+","+sid+","+md5;
                bufferedWriter.write(line2);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
