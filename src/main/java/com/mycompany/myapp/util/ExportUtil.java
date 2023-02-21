package com.mycompany.myapp.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 导出工具
 *
 */
public class ExportUtil {

    public static void excel(Workbook workbook, String filename, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setHeader(
            "Content-Disposition",
            "attachment;filename=" + new String(filename.getBytes(), StandardCharsets.ISO_8859_1)
        );
        httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setHeader("Cache-Control", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        workbook.write(outputStream);
        outputStream.close();
    }
}
