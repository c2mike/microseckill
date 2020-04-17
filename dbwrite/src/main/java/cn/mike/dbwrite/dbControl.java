package cn.mike.dbwrite;

import commons.bean.MsgContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class dbControl {
    @Autowired
    WriteOrder writeOrder;
    @PostMapping("/dbwrite")
    public void dbwrite(MsgContent msgContent)
    {
        writeOrder.writeMsgLog(msgContent.getSkuId(),msgContent.getPhone(),msgContent.getMsgId());
    }
}
