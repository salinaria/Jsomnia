import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        private String url = "";
        private String reqMethod = "GET";
        private Map<String, List<String>> Headers = new HashMap<>();
        private boolean fRedirect = false;
        private boolean showH = false;

        public Connection(String url, String reqMethod, Map<String, List<String>> headers, boolean fRedirect, boolean showH) {
            this.url = url;
            this.reqMethod = reqMethod;
            Headers = headers;
            this.fRedirect = fRedirect;
            this.showH = showH;
        }

        public HttpURLConnection getConnection() throws IOException {
            HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
            connection.setRequestMethod(reqMethod);
            connection.setInstanceFollowRedirects(fRedirect);
            for (String key : Headers.keySet())
                connection.setRequestProperty(key, String.valueOf(Headers.get(key)));
            return connection;
        }

        public boolean isShowH() {
            return showH;
        }
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        HttpURLConnection connection = null;
        boolean showHeader = false;
        int i = 0;
        while (i < args.length) {
            switch (args[i]) {
                case "list": {
                    File saves = new File("./Saves/");
                    File[] files = saves.listFiles();
                    int j = 1;
                    assert files != null;
                    for (File file : files) {
                        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                        Connection connection1 = (Connection) in.readObject();
                        HttpURLConnection connection2 = connection1.getConnection();
                        System.out.println(j + ". URL: " + connection2.getURL().toString() + " | Method: " + connection2.getRequestMethod()
                                + " | Headers: " + connection2.getRequestProperties());
                        j++;
                    }
                    i++;
                    break;
                }
                case "fire":{
                    File saves = new File("./Saves/");
                    File[] files = saves.listFiles();
                    System.out.println("hi");
                    break;
                }
                case "-S":
                case "--save": {
                    File saves = new File("./Saves/");
                    File[] files = saves.listFiles();
                    assert files != null;
                    int name = files.length + 1;
                    FileOutputStream fout = new FileOutputStream("./Saves/Save" + name + ".txt");
                    System.out.println("hi");
                    ObjectOutputStream out = new ObjectOutputStream(fout);
                    assert connection != null;
                    out.writeObject(new Connection(connection.getURL().toString()
                            , connection.getRequestMethod(), connection.getRequestProperties(), true, true));
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
                        if (args.length != i + 1 && !args[i + 1].startsWith("-")) {
                            try {
                                writer = new FileWriter("./Outputs/" + args[i + 1] + ".html");
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
                                writer = new FileWriter("./Outputs/" + name + ".html");
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
                                os = new FileOutputStream("./Outputs/" + args[i + 1] + ".png");
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
                                os = new FileOutputStream("./Outputs/" + name + ".png");
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
                        System.out.println("ok");
                        FileWriter writer = null;
                        BufferedReader br = null;
                        try {
                            br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (args.length != i + 1 && !args[i + 1].startsWith("-")) {
                            try {
                                writer = new FileWriter("./Outputs/" + args[i + 1] + ".txt");
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
                                writer = new FileWriter("./Outputs/" + name + ".txt");
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
                                System.out.println("okkkkk");
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
                    if (!args[i].startsWith("http://")) {
                        url = new URL("http://" + args[i]);
                    } else url = new URL(args[i]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    i++;
                    break;
            }
        }
    }
}
