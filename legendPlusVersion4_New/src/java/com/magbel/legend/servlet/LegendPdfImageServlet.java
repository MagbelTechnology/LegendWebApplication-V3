package com.magbel.legend.servlet;

import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.magbel.legend.bus.ApprovalRecords;

@MultipartConfig(fileSizeThreshold=1024*1024*10,
maxFileSize=1024*1024*50,
maxRequestSize=1024*1024*100)
public class LegendPdfImageServlet extends HttpServlet
{

    private static final long serialVersionUID = 1L;

    public LegendPdfImageServlet()
    {
    }


    private static final String PROPERTY_PATH = "C:\\Property\\LegendPlus.properties";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String assetCodeParam = request.getParameter("AssetCode");
        String type = request.getParameter("type");

        if (assetCodeParam == null || type == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
            return;
        }

        ApprovalRecords approve = new ApprovalRecords();
        
        

        String assetId = approve.getCodeName("SELECT asset_id FROM AM_ASSET_APPROVAL WHERE transaction_id = ?", assetCodeParam);
        System.out.println("assetId : " + assetId);
        if (assetId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid transaction ID");
            return;
        }

        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream(PROPERTY_PATH)) {
            prop.load(input);
        } catch (IOException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to load configuration");
            return;
        }

        String groupId;
        try {
            if ("U".equalsIgnoreCase(type)) {
                groupId = approve.getCodeName("SELECT GROUP_ID FROM AM_ASSET_UNCAPITALIZED WHERE Asset_id = ?", assetId);
            } else {
                groupId = approve.getCodeName("SELECT GROUP_ID FROM AM_ASSET WHERE Asset_id = ?", assetId);
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error during group ID lookup");
            return;
        }

        String assetCode = (groupId == null || groupId.equals("0") || groupId.trim().isEmpty()) ? assetCodeParam : groupId;
        System.out.println("assetCode : " + assetCode);
        String newCodeName = "W" + assetCode;
        System.out.println("newCodeName : " + newCodeName);

        String uploadFolder = prop.getProperty("imagesUrl");
        if (uploadFolder == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Upload folder not configured");
            return;
        }

        File folder = new File(uploadFolder);
        List<File> files = getMatchingFiles(newCodeName, folder);

        // Fallback if no files found
        if (files.isEmpty()) {
            try {
                assetCode = approve.getCodeName("SELECT ASSET_CODE FROM AM_ASSET WHERE Asset_id = ?", assetId);
                System.out.println("asset Code in empty file cap : " + assetCode);
                if (assetCode == null) {
                    assetCode = approve.getCodeName("SELECT ASSET_CODE FROM AM_ASSET_UNCAPITALIZED WHERE Asset_id = ?", assetId);
                    System.out.println("asset Code in empty file uncap : " + assetCode);
                }
                newCodeName = "W" + assetCode;
                System.out.println("newCodeName (2) : " + newCodeName);
                files = getMatchingFiles(newCodeName, folder);
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error during fallback asset code lookup");
                return;
            }
        }

        if (files.size() == 1) {
            File file = files.get(0);
            if (!file.exists() || file.length() <= 0) {
                alertClient(response, "Incorrect File Size!");
                return;
            }

            String filename = file.getName();
            String contentType = Files.probeContentType(file.toPath());
            if (contentType == null) {
                contentType = getMimeTypeByExtension(filename);
            }

            if (filename.toLowerCase().endsWith(".xls") || filename.toLowerCase().endsWith(".xlsx")) {
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment; filename=" + filename);

                try (FileInputStream fis = new FileInputStream(file);
                     ServletOutputStream out = response.getOutputStream();
                     Workbook workbook = filename.toLowerCase().endsWith(".xls")
                             ? new HSSFWorkbook(fis)
                             : new XSSFWorkbook(fis)) {

                    workbook.write(out);
                }
            } else {
                boolean isImage = contentType.startsWith("image/");
                String dispositionType = isImage ? "inline" : "attachment";
                streamFileToClient(file, response, contentType, filename, dispositionType);
            }

        } else {
            alertClient(response, "No File Available!");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private List<File> getMatchingFiles(String codeName, File folder) {
        List<File> matchedFiles = new ArrayList<>();
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().contains(codeName)) {
                    matchedFiles.add(file);
                }
            }
        }
        return matchedFiles;
    }

    private void alertClient(HttpServletResponse response, String message) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<script>alert('" + message + "');</script>");
        out.println("<script>history.go(-1);</script>");
    }

    private void streamFileToClient(File file, HttpServletResponse response, String mimeType, String filename, String dispositionType) throws IOException {
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", dispositionType + "; filename=\"" + filename + "\"");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "no-store, max-age=0");

        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ServletOutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = bis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    private String getMimeTypeByExtension(String filename) {
        String name = filename.toLowerCase();
        if (name.endsWith(".jpg") || name.endsWith(".jpeg")) return "image/jpeg";
        if (name.endsWith(".png")) return "image/png";
        if (name.endsWith(".doc")) return "application/msword";
        if (name.endsWith(".docx")) return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        if (name.endsWith(".pdf")) return "application/pdf";
        return "application/octet-stream";
    }
   	    
}
