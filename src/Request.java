import java.util.ArrayList;
import java.util.HashMap;

public class Request {
    private String name;
    private String url;
    private String urlType;
    private HashMap<String,String>headers=new HashMap<>();
    private String body;

    public Request(String name, String urlType) {
        this.name = name;
        this.urlType = urlType;
    }

    public String getName() {
        return name;
    }

    public String getUrlType() {
        return urlType;
    }
}
