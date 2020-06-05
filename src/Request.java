import java.util.HashMap;

public class Request {
    private String name;
    private String url;
    private String urlType;
    private HashMap<String, String> headers = new HashMap<>();
    private HashMap<String, String> data = new HashMap<>();
    private String json;
    private String bodyType;
    private String statusCode="OK200";
    private double responseTime=2.69;

    public Request(String name, String urlType) {
        this.name = name;
        this.urlType = urlType;
        this.bodyType="Form Data";
    }

    public String getName() {
        return name;
    }

    public String getUrlType() {
        return urlType;
    }

    public String getUrl() {
        return url;
    }

    public String getJson() {
        return json;
    }


    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void addHeader(String header, String value) {
        headers.put(header, value);
    }

    public void removeHeader(String header){
        for(String key:headers.keySet()){
            if(key.equals(header)){
                headers.remove(key);
                break;
            }
        }
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void addData(String header, String value) {
        data.put(header, value);
    }

    public void removeData(String header){
        for(String key:data.keySet()){
            if(key.equals(header)){
                data.remove(key);
                break;
            }
        }
    }

    public void setJson(String json) {
        this.json = json;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    public double getResponseTime() {

        return responseTime;
    }

    public void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getBodyType() {
        return bodyType;
    }
}
