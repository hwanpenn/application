package com.shineyue.bpm.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shineyue.bpm.domain.ZipBean;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 解析zip文件
 *
 * @author liubo
 * @version  2017-11-28 9:57
 **/
public class ReadZipFIle {

    private static final Log LOGGER = LogFactory.getLog(ReadZipFIle.class);
    public  static ZipBean readZip(InputStream inputStream) {
        ZipBean zipBean = new ZipBean();
        ZipInputStream zipInputStream = null;
        try {
            Map<String,String> modelMap = new HashMap<String,String>();
            Charset charset = Charset.forName("UTF-8");
            zipInputStream = new ZipInputStream(inputStream, charset);
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry!=null){
                String zipEntryName = zipEntry.getName();
                if (zipEntryName.endsWith(".bpmn")||zipEntryName.endsWith(".bpmn20.xml")) {
                    String json = IOUtils.toString(zipInputStream,"UTF-8");
                    modelMap.put(zipEntryName,json);
                }
                zipEntry = zipInputStream.getNextEntry();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String modelsAsString = objectMapper.writeValueAsString(modelMap);
            zipBean.setModles(modelsAsString);
        } catch (Exception e) {
            e.printStackTrace();
            zipBean.setMsg(e.getMessage());
        }finally {
            if (zipInputStream != null) {
                try {
                    zipInputStream.closeEntry();
                } catch (Exception e) {
                    zipBean.setMsg(e.getMessage());
                }
                try {
                    zipInputStream.close();
                } catch (Exception e) {
                    zipBean.setMsg(e.getMessage());
                }
            }
        }
        return zipBean;
    }
}
