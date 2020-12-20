package devec.pdfmicroservice.controller;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.lowagie.text.html.HtmlParser;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.StringCharBuffer;
import java.nio.charset.StandardCharsets;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;

@RestController
public class PdfController {

    @GetMapping("/pdf/rendering/stream")
    public String stream(@RequestBody String html) {
        try {
            Document document = new Document();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, outputStream);

            document.open();

            HtmlParser.parse(document, new ByteArrayInputStream(html.getBytes()));

            document.close();

            return outputStream.toString(StandardCharsets.UTF_8);
        } catch (DocumentException | IOException de) {
            System.err.println(de.getMessage());
        }

        return "";
    }

    @GetMapping("/pdf/rendering/filecached")
    public String filecached(@RequestBody String html) {
        try {
            Document document = new Document();

            String sha256hex = Hashing.sha256()
                    .hashString(html, StandardCharsets.UTF_8)
                    .toString();

            File HtmlFile = new File("/tmp/" + sha256hex + ".html");
            File pdfFile  = new File("/tmp/" + sha256hex + ".pdf");

            BufferedWriter htmlWriter = Files.newWriter(HtmlFile, StandardCharsets.UTF_8);

            htmlWriter.write(html);
            htmlWriter.close();

            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

            document.open();

            HtmlParser.parse(document, new FileInputStream(HtmlFile));

            document.close();

            String output = Files.toString(pdfFile, StandardCharsets.UTF_8);

            if (!HtmlFile.delete()) {
                System.err.println("coun't delete file#" + HtmlFile.getName());
            }

            if (!pdfFile.delete()) {
                System.err.println("coun't delete file#" + pdfFile.getName());
            }

            return output;
        } catch (DocumentException | IOException de) {
            System.err.println(de.getMessage());
        }

        return "";
    }
}
