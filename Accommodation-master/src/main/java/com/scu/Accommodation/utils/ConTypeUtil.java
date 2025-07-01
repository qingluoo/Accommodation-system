package com.scu.Accommodation.utils;

public class ConTypeUtil {

    // 根据文件名判断 Content-Type
    public static String determineContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "pdf":
                return "application/pdf";
            case "txt":
                return "text/plain";
            case "mp4":
                return "video/mp4";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "xls":
                return "application/vnd.ms-excel";
            default:
                return "application/octet-stream"; // 默认二进制流（会触发下载）
        }
    }
}
