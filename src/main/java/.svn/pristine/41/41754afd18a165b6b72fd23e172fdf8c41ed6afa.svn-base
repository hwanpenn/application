package com.shineyue.bpm.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shineyue.bpm.http.HttpResult;
import com.shineyue.bpm.http.HttpService;
import org.springframework.web.util.UriUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;

/**
 * 数据转zip文件
 *
 * @author liubo
 * @version 2017-11-29 11:17
 **/
public class CreateModelDefinitionZip {
    public static void createModelDefinitnionZip(HttpServletResponse response,String modleMap) {
        try {
            String fileName = "bpm_"
                    + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".zip\"; filename*=utf-8''" + UriUtils.encode(fileName+ ".zip", "utf-8"));
            ServletOutputStream servletOutputStream = response.getOutputStream();
            response.setContentType("application/zip");
            ZipOutputStream zipOutputStream = new ZipOutputStream(servletOutputStream);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String,Object> map = objectMapper.readValue(modleMap,Map.class);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                byte[] xmlData = value.getBytes("UTF8");
                createZipEntry(zipOutputStream,key,xmlData);
            }
            zipOutputStream.close();
            servletOutputStream.flush();
            servletOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected static void createZipEntry(ZipOutputStream zipOutputStream, String filename, byte[] content) throws Exception {
        ZipEntry entry = new ZipEntry(filename);
        zipOutputStream.putNextEntry(entry);
        zipOutputStream.write(content);
        zipOutputStream.closeEntry();
    }
}
