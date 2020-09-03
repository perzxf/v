package com.ls.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class PdfUtil {


    public static void createPdf(String content, File file) throws IOException, DocumentException {
        // 创建PDF文档对象
        Document document = new Document();
        // 把文档对象写入目标地址
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        //
        document.open();
        // step 4
        XMLWorkerFontProvider fontImp = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
        fontImp.register("static/assets/fonts/FZXBSJW.TTF");
        fontImp.register("static/assets/fonts/simfang.ttf");
        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                new ByteArrayInputStream(content.getBytes("UTF-8")), null, Charset.forName("UTF-8"), fontImp);
        // step 5
        document.close();

    }
}
