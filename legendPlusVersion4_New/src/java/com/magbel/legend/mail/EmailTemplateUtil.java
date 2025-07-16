package com.magbel.legend.mail;

import java.util.Map;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EmailTemplateUtil{
	public static String loadTemplate(String filePath, Map<String, String> values) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        for (Map.Entry<String, String> entry : values.entrySet()) {
            content = content.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return content;
    }

}
