package 并发.不可变类;

/**
 * @Author: znj
 * @Date: 2021/6/22 0022 23:20
 */
public class SmsInfo {
    /**
     * 地址
     */
    private String url;

    /**
     * 内容
     */
    private String context;

    public SmsInfo(String url,String context){
        this.url = url;
        this.context = context;
    }

    public SmsInfo(SmsInfo smsInfo){
        this.url = smsInfo.getUrl();
        this.context = smsInfo.getContext();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "SmsInfo{" +
                "url='" + url + '\'' +
                ", context='" + context + '\'' +
                '}';
    }
}
