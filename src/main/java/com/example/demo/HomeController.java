package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Lucia on 2018/10/6.
 */
@Controller
public class HomeController {
    @RequestMapping("/home")
    public String home(){
        return "index";
    }

    @RequestMapping("/csvUpload")
    public void csvUpload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Part> fileParts = request.getParts().stream().filter(part -> "file".equals(part.getName())).collect(Collectors.toList());
        String baseUrl = "http://api.timezonedb.com/v2.1/get-time-zone?key=SUEGC6TS8KZE&format=json&by=position";

        for (Part filePart : fileParts) {
            InputStream fileContent = filePart.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(fileContent);
            char []cha = new char[1024];
            int len = inputStreamReader.read(cha);
            String dataChar = new String(cha,0,len);
            System.out.println(dataChar);
            inputStreamReader.close();

            String[] dataLine = dataChar.split("\r\n");
            StringBuilder sb = new StringBuilder();

            for (String dataline : dataLine) {
                sb.append(dataline);
                String[] datalineList = dataline.split(",");
                String time = datalineList[0];
                String latitude = datalineList[1];
                String longitude = datalineList[2];
                String urlString = baseUrl + "&lat=" + latitude + "&lng=" + longitude;
                try {
                    URL url = new URL(urlString);
                    java.net.HttpURLConnection conn = (java.net.HttpURLConnection)url.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("GET");
                    java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(),"UTF-8"));
                    String line;
                    while ((line = in.readLine()) != null) {
                        String[] list = line.split(",");
                        Long timediff = 0L;
                        int dst = 0;
                        for (String l: list) {
                            if(l.contains("zoneName")){
                                String tmp = l.split(":")[1];
                                sb.append("," + tmp.replace("\\", "").replace("\"", ""));
                            }
                            if(l.contains("gmtOffset")) {
                                timediff = Long.parseLong(l.split(":")[1]) * 1000;
                            }
                            if(l.contains("dst")) {
                                String tmp = l.split(":")[1];
                                dst = Integer.parseInt(tmp.replace("\"", "")) * 3600 * 1000;
                            }
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date utcTime = sdf.parse(time);
                        Date local = new Date();
                        local.setTime(utcTime.getTime() + timediff - dst);
                        sb.append("," + sdf.format(local));
                    }
                    sb.append("\r\n");
                    in.close();

                } catch (Exception e) {
                    System.out.println("error in action,and e is " + e.getMessage());
                }
            }
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=\"output.csv\"");
            try
            {
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(sb.toString().getBytes());
                outputStream.flush();
                outputStream.close();
            }
            catch(Exception e)
            {
                System.out.println(e.toString());
            }
        }
    }

}
