import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

public class Request {
    private String name;
    private String url;
    private String urlType;
    private HashMap<String, String> headers = new HashMap<>();
    private String body;
    private String statusCode="OK200";
    private double responseTime=2.69;

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

    public String getUrl() {
        return url;
    }

    public String getBody() {
        return body;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void addHeader(String header, String value) {
        headers.put(header, value);
    }

    public void setBody(String body) {
        this.body = body;
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

}
