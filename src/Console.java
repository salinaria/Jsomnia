import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;

public class Console {
    public static void bufferOutFormData(HashMap<String, String> form, String boundary, BufferedOutputStream bufferedOutputStream) throws IOException {
        for (String key : form.keySet()) {
            bufferedOutputStream.write(("--" + boundary + "\r\n").getBytes());
            if (key.contains("file")) {
                bufferedOutputStream.write(("Content-Disposition: form-data; filename=\"" + (new File(form.get(key))).getName() + "\"\r\nContent-Type: Auto\r\n\r\n").getBytes());
                try {
                    BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(new File(form.get(key))));
                    byte[] filesBytes = inputStream.readAllBytes();
                    bufferedOutputStream.write(filesBytes);
                    bufferedOutputStream.write("\r\n".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bufferedOutputStream.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes());
                bufferedOutputStream.write((form.get(key) + "\r\n").getBytes());
            }
        }
        bufferedOutputStream.write(("--" + boundary + "--\r\n").getBytes());
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }

    public static void uploadBinary(URL url, String fileName) {
        try {
            File file = new File(fileName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(connection.getOutputStream());
            BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(file));
            bufferedOutputStream.write(fileInputStream.readAllBytes());
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(connection.getInputStream());
            System.out.println(new String(bufferedInputStream.readAllBytes()));
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getHeaderFields());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Connection implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name="";
        private String url = "";
        private String reqMethod = "GET";
        private HashMap<String,String> Headers = new HashMap<>();
        private boolean fRedirect = false;
        private boolean showH = false;
        private HashMap<String, String> formData = new HashMap<>();

        public Connection(String url, String reqMethod, HashMap<String,String> headers, HashMap<String, String> formData, boolean fRedirect, boolean showH) {
            this.url = url;
            this.reqMethod = reqMethod;
            Headers = headers;
            this.fRedirect = fRedirect;
            this.showH = showH;
            this.formData = formData;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public HttpURLConnection getConnection() throws IOException {
            HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
            connection.setRequestMethod(reqMethod);
            connection.setInstanceFollowRedirects(fRedirect);
            for (String key : Headers.keySet())
                connection.setRequestProperty(key, String.valueOf(Headers.get(key)));
            connection.setDoOutput(true);
            connection.setDoInput(true);
            if (formData.size() > 0) {
                String boundary = System.currentTimeMillis() + "";
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                bufferOutFormData(formData, boundary, new BufferedOutputStream(connection.getOutputStream()));
            }
            return connection;
        }

        public boolean isShowH() {
            return showH;
        }
        public Request getRequest(){
            Request request=new Request(name,reqMethod);
            request.setHeaders(Headers);
            System.out.println(url);
            request.setUrl(url);
            request.setData(formData);
            request.setName(name);
            return request;
        }
    }

    public HttpURLConnection consoleWork(String[] args) throws IOException, ClassNotFoundException {
        HttpURLConnection connection = null;
        boolean showHeader = false;
        HashMap<String, String> formData = new HashMap<>();
        int i = 0;
        while (i < args.length && args[i] != null) {
            switch (args[i]) {
                case "-C": {
                    return connection;
                }
                case "list": {
                    File saves = new File("./Console/Saves/");
                    File[] files = saves.listFiles();
                    int j = 1;
                    assert files != null;
                    for (File file : files) {
                        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                        Connection connection1 = (Connection) in.readObject();
                        HttpURLConnection connection2 = connection1.getConnection();
                        connection2.disconnect();
                        System.out.println(j + ". URL: " + connection2.getURL().toString() + " | Method: " + connection2.getRequestMethod()
                                + " | Headers: " + connection2.getRequestProperties());
                        j++;
                    }
                    i++;
                    break;
                }
                case "fire": {
                    File saves = new File("./Console/Saves/");
                    File[] files = saves.listFiles();
                    while (i + 1 < args.length && new Scanner(args[i + 1].trim()).hasNextInt()) {
                        int j = Integer.parseInt(args[i + 1]);
                        assert files != null;
                        ObjectInputStream in = new ObjectInputStream(new FileInputStream(files[j - 1]));
                        Connection connection1 = (Connection) in.readObject();
                        HttpURLConnection connection2 = connection1.getConnection();
                        BufferedReader br;
                        if (connection2.getInstanceFollowRedirects()) {
                            while (connection2.getHeaderFields().containsKey("Location")) {
                                connection2 = (HttpURLConnection) new URL(connection2.getHeaderFields().get("Location").get(0)).openConnection();
                            }
                        }
                        if (connection2.getResponseCode() < 400) {
                            try {
                                br = new BufferedReader(new InputStreamReader((connection2.getInputStream())));
                                String output = "";
                                while ((output = br.readLine()) != null) {
                                    System.out.println(output);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (connection1.showH) {
                            System.out.println("\n\nResponse Message: " + connection2.getResponseMessage() + connection2.getResponseCode());
                            System.out.println("\n\nResponse Headers: " + connection2.getHeaderFields());
                        }
                        i++;
                    }
                    i++;
                    break;
                }
                case "-S":
                case "--save": {

                    File saves = new File("./Console/Saves/");
                    File[] files = saves.listFiles();
                    assert files != null;
                    int name = files.length + 1;
                    assert connection != null;
                    connection.disconnect();
                    FileOutputStream fOut = new FileOutputStream("./Console/Saves/Save" + name + ".txt");
                    ObjectOutputStream out = new ObjectOutputStream(fOut);
                    HashMap<String,String> map=new HashMap<>();
                    for(String string:connection.getRequestProperties().keySet()){
                        map.put(string,connection.getRequestProperty(string));
                    }
                    Connection connection1=new Connection(connection.getURL().toString()
                            , connection.getRequestMethod(), map, formData, connection.getInstanceFollowRedirects(), showHeader);
                    if(args[i+1]!=null && !args[i+1].startsWith("-")){
                        connection1.setName(args[i+1]);
                        i++;
                    }else{
                        connection1.setName(connection.getURL().toString());
                    }
                    out.writeObject(connection1);
                    out.flush();
                    out.close();
                    i++;
                    break;
                }
                case "-h":
                case "--help":

                    break;
                case "-i":
                    showHeader = true;
                    i++;
                    break;
                case "-M":
                case "--method":
                    try {
                        assert connection != null;
                        connection.setRequestMethod(args[i + 1]);
                        i = i + 2;
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }
                    break;
                case "-f":
                    assert connection != null;
                    connection.setInstanceFollowRedirects(true);
                    i++;
                    break;
                case "-O":
                case "--output":
                    assert connection != null;
                    if (connection.getHeaderFields().get("Content-Type").get(0).contains("html")) {
                        FileWriter writer = null;
                        BufferedReader br = null;
                        try {
                            br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (args.length != i + 1 && args[i + 1] != null && !args[i + 1].startsWith("-")) {
                            try {
                                writer = new FileWriter("./Console/Outputs/" + args[i + 1] + ".html");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            i = i + 2;
                        } else {
                            LocalDateTime time = LocalDateTime.now();
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                            String name = dtf.format(time);
                            name = name.replaceAll("/", "");
                            name = name.replaceAll("/", "");
                            name = name.replaceAll(":", "");
                            name = name.replaceAll(" ", "_");
                            try {
                                writer = new FileWriter("./Console/Outputs/" + name + ".html");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            i++;
                        }
                        String output;
                        try {
                            assert br != null;
                            while ((output = br.readLine()) != null) {
                                assert writer != null;
                                writer.write(output);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        br.close();
                        writer.close();
                    } else if (connection.getHeaderFields().get("Content-Type").get(0).contains("image")) {
                        InputStream is = connection.getURL().openStream();
                        if (args.length != i + 1 && !args[i + 1].startsWith("-")) {
                            OutputStream os = null;
                            try {
                                os = new FileOutputStream("./Console/Outputs/" + args[i + 1] + ".png");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            byte[] bytes = new byte[4096];
                            int j;
                            assert is != null;
                            while ((j = is.read(bytes)) != -1) {
                                assert os != null;
                                os.write(bytes, 0, j);
                            }
                            i = i + 2;
                        } else {
                            LocalDateTime time = LocalDateTime.now();
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                            String name = dtf.format(time);
                            name = name.replaceAll("/", "");
                            name = name.replaceAll("/", "");
                            name = name.replaceAll(":", "");
                            name = name.replaceAll(" ", "_");
                            OutputStream os = null;
                            try {
                                os = new FileOutputStream("./Console/Outputs/" + name + ".png");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            byte[] bytes = new byte[4096];
                            int j;
                            assert is != null;
                            while ((j = is.read(bytes)) != -1) {
                                assert os != null;
                                os.write(bytes, 0, j);
                            }
                            i++;
                        }
                    } else if (connection.getHeaderFields().get("Content-Type").get(0).contains("text")) {
                        FileWriter writer = null;
                        BufferedReader br = null;
                        try {
                            br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (args.length != i + 1 && !args[i + 1].startsWith("-")) {
                            try {
                                writer = new FileWriter("./Console/Outputs/" + args[i + 1] + ".txt");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            i = i + 2;
                        } else {
                            LocalDateTime time = LocalDateTime.now();
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                            String name = dtf.format(time);
                            name = name.replaceAll("/", "");
                            name = name.replaceAll("/", "");
                            name = name.replaceAll(":", "");
                            name = name.replaceAll(" ", "_");
                            try {
                                writer = new FileWriter("./Console/Outputs/" + name + ".txt");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            i++;
                        }
                        String output;
                        try {
                            assert br != null;
                            while ((output = br.readLine()) != null) {
                                assert writer != null;
                                writer.write(output);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        br.close();
                        writer.close();
                    }
                    break;
                case "-H":
                case "--Headers": {
                    StringBuilder input = new StringBuilder();
                    input.append(args[i + 1]);
                    while (input.length() > 0) {
                        StringBuilder key = new StringBuilder();
                        StringBuilder value = new StringBuilder();
                        while (!input.toString().startsWith(":") && input.length() != 0) {
                            key.append(input.charAt(0));
                            input.delete(0, 1);
                        }
                        input.delete(0, 1);
                        while (!input.toString().startsWith(";") && input.length() != 0) {
                            value.append(input.charAt(0));
                            input.delete(0, 1);
                        }
                        input.delete(0, 1);
                        if (key.length() > 0 && value.length() > 0) {
                            assert connection != null;
                            connection.setRequestProperty(key.toString(), value.toString());
                        } else
                            System.out.println("Wrong format");
                    }
                    i = i + 2;
                    break;
                }
                case "-D":
                case "--data": {
                    StringBuilder input = new StringBuilder();
                    input.append(args[i + 1]);
                    HashMap<String, String> fooBody = new HashMap<>();
                    assert connection != null;
                    connection.setDoOutput(true);
                    while (input.toString().length() > 0) {
                        StringBuilder key = new StringBuilder();
                        StringBuilder value = new StringBuilder();
                        while (!input.toString().startsWith(":") && input.length() != 0) {
                            key.append(input.charAt(0));
                            input.delete(0, 1);
                        }
                        input.delete(0, 1);
                        while (!input.toString().startsWith(";") && input.length() != 0) {
                            value.append(input.charAt(0));
                            input.delete(0, 1);
                        }
                        input.delete(0, 1);
                        if (key.length() > 0 && value.length() > 0) {
                            fooBody.put(key.toString(), value.toString());
                        } else
                            System.out.println("Wrong format");
                    }
                    String boundary = System.currentTimeMillis() + "";
                    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                    BufferedOutputStream request = null;
                    try {
                        request = new BufferedOutputStream(connection.getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bufferOutFormData(fooBody, boundary, request);
                    formData = fooBody;
                    try {
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(connection.getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    i = i + 2;
                    break;
                }
                case "-U":
                case "--upload":
                    assert connection != null;
                    uploadBinary(connection.getURL(), args[i + 1]);
                    i = i + 2;
                    break;
                default:
                    URL url;
                    if (!args[i].startsWith("http://") && !args[i].startsWith("https://")) {
                        url = new URL("http://" + args[i]);
                    } else {
                        url = new URL(args[i]);
                    }
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    i++;
                    break;
            }
        }
        return connection;
    }
}
