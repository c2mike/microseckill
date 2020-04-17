package commons.bean;

public class RespBean {
    private int status;
    private String msg;
    private Object obj;

    public static RespBean build()
    {
        return new RespBean();
    }

    public static RespBean Ok(String msg)
    {
        RespBean respBean = new RespBean();
        respBean.status = 200;
        respBean.msg = msg;
        return respBean;
    }

    public static RespBean Ok(String msg,Object obj)
    {
        RespBean respBean = new RespBean();
        respBean.status = 200;
        respBean.msg = msg;
        respBean.obj = obj;
        return respBean;
    }

    public static RespBean Error(String msg)
    {
        RespBean respBean = new RespBean();
        respBean.status = 500;
        respBean.msg = msg;
        return respBean;
    }

    public static RespBean Error(String msg,Object obj)
    {
        RespBean respBean = new RespBean();
        respBean.status = 500;
        respBean.msg = msg;
        respBean.obj = obj;
        return respBean;
    }

    public int getStatus() {
        return status;
    }

    public RespBean setStatus(int status) {
        this.status = status;return this;
    }

    public String getMsg() {
        return msg;
    }

    public RespBean setMsg(String msg) {
        this.msg = msg;return this;
    }

    public Object getObj() {
        return obj;
    }

    public RespBean setObj(Object obj) {
        this.obj = obj;return this;
    }
}
