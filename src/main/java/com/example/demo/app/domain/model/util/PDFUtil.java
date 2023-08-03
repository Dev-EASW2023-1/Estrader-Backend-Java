package com.example.demo.app.domain.model.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class PDFUtil {
    @Value("${file.claimdoc-dir}")
    private String fileClaimDocPath;
    @Value("${file.font-dir}")
    private String fileFontPath;
    @Value("${file.upload-dir}")
    private String fileUploadPath;

    private static final Float ONE_CM = 28.346f;

    public Resource createPdf(String userId) {
        // 파일명에 아이디와 생성일자를 조합하여 생성
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String creationDate = currentDateTime.format(formatter);
        String fileName = userId + "_" + creationDate + ".pdf";
        String docFileName = "bidSheet.pdf";
        String fontFileName = "nanumgothictext.ttf";

        File docFile = new File(fileClaimDocPath + docFileName);
        File fontFile = new File(fileFontPath + fontFileName);
        File modifiedPdf = new File(fileUploadPath + fileName);

        try (PDDocument doc = PDDocument.load(docFile))
        {
            PDType0Font font = PDType0Font.load(doc, fontFile);
            PDPage page = doc.getPage(0);
            writePdf(doc, font, page);
            doc.save(modifiedPdf);
        }
        catch (IOException e)
        {
            log.info("PDF 파일 생성 실패");
            return null;
        }
        return loadAsResource(fileName);
    }

    public void writePdf(
            PDDocument doc,
            PDType0Font font,
            PDPage page
    ){
        try (PDPageContentStream contentStream = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true))
        {
            generateBiddingPeriodAndItemNumber(font, contentStream);
            generateBuyerInformation(font, contentStream);
            generateRealtorInformation(font, contentStream);
            generateBidAndGuaranteeAmount(font, contentStream);
        }
        catch (IOException e)
        {
            log.info("PDF 파일 생성 실패");
        }
    }

    public void text(
            Float x,
            Float y,
            Float fontSize,
            String text,
            PDFont font,
            PDPageContentStream contentStream
    ) throws IOException {
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
    }

    public void generateBiddingPeriodAndItemNumber(
            PDType0Font font,
            PDPageContentStream contentStream
    ) throws IOException {
        contentStream.beginText();
        text(ONE_CM * 14f, ONE_CM * 23.4f, 9f, "2020", font, contentStream);
        text(ONE_CM * 1.2f, 0f, 9f, "04", font, contentStream);
        text(ONE_CM * 1.2f, 0f, 9f, "03", font, contentStream);
        text(-ONE_CM * 3f, -ONE_CM * 0.9f, 12f, "물건번호", font, contentStream);
        text(-ONE_CM * 8f, -2f, 12f, "2023", font, contentStream);
        text(ONE_CM * 3.5f, 0f, 12f, "123123", font, contentStream);
        contentStream.endText();
    }

    public void generateBuyerInformation(
            PDType0Font font,
            PDPageContentStream contentStream
    ) throws IOException {
        contentStream.beginText();
        text(ONE_CM * 7.762f, 603f, 20f, "구매인", font, contentStream);
        text(ONE_CM * 6.879f, 2f, 12f, "010-1234-5678", font, contentStream);
        text(-ONE_CM * 7f, -ONE_CM * 0.95f, 12f, "960806-123456", font, contentStream);
        text(ONE_CM * 6.2f, 0f, 12f, "110111-110111", font, contentStream);
        text(-ONE_CM * 6.2f, -ONE_CM * 0.8f, 10f, "충청남도 홍성군 머시기", font, contentStream);
        contentStream.endText();
    }

    public void generateRealtorInformation(
            PDType0Font font,
            PDPageContentStream contentStream
    ) throws IOException {
        contentStream.beginText();
        text(ONE_CM * 7.762f, ONE_CM * 18.8f, 20f, "대리인", font, contentStream);
        text(ONE_CM * 8f, 2f, 12f, "친척", font, contentStream);
        text(-ONE_CM * 8.121f, -ONE_CM * 0.88f, 12f, "960806-123456", font, contentStream);
        text(ONE_CM * 7.4f, 0f, 10f, "010-1234-4564", font, contentStream);
        text(-ONE_CM * 7.4f, -ONE_CM * 0.65f, 10f, "충청북도 청주 머시기", font, contentStream);
        contentStream.endText();
    }

    public void generateBidAndGuaranteeAmount(
            PDType0Font font,
            PDPageContentStream contentStream
    ) throws IOException {
        contentStream.beginText();
        //입찰 금액 및 보증 금액
        text(ONE_CM * 3.8f, ONE_CM * 15.1f, 20f, "1", font, contentStream);
        text(ONE_CM * 0.5f, 0f, 20f, "2", font, contentStream);
        text(ONE_CM * 0.55f, 0f, 20f, "3", font, contentStream); //십억
        text(ONE_CM * 0.5f, 0f, 20f, "4", font, contentStream);
        text(ONE_CM * 0.5f, 0f, 20f, "5", font, contentStream);
        text(ONE_CM * 0.5f, 0f, 20f, "6", font, contentStream); //백만
        text(ONE_CM * 0.5f, 0f, 20f, "7", font, contentStream);
        text(ONE_CM * 0.5f, 0f, 20f, "8", font, contentStream);
        text(ONE_CM * 0.55f, 0f, 20f, "9", font, contentStream); //천
        text(ONE_CM * 0.5f, 0f, 20f, "0", font, contentStream);
        text(ONE_CM * 0.5f, 0f, 20f, "1", font, contentStream);
        text(ONE_CM * 0.55f, 0f, 20f, "2", font, contentStream);
        // 보증 금액
        // => 입찰에 참여할 때는 통상 경매 물건의 최저매각가격의 10분의 1에 해당하는 금액을 매수신청의 보증을 제공(「민사집행법」 제113조, 「민사집행규칙」 제63조제1항 및 제71조)
        text(ONE_CM * 2.2f, 0f, 20f, "7", font, contentStream);
        text(ONE_CM * 0.45f, 0f, 20f, "1", font, contentStream);
        text(ONE_CM * 0.5f, 0f, 20f, "2", font, contentStream); //십억
        text(ONE_CM * 0.45f, 0f, 20f, "3", font, contentStream);
        text(ONE_CM * 0.45f, 0f, 20f, "4", font, contentStream);
        text(ONE_CM * 0.45f, 0f, 20f, "5", font, contentStream); //백만
        text(ONE_CM * 0.5f, 0f, 20f, "6", font, contentStream);
        text(ONE_CM * 0.45f, 0f, 20f, "7", font, contentStream);
        text(ONE_CM * 0.5f, 0f, 20f, "8", font, contentStream); //천
        text(ONE_CM * 0.55f, 0f, 20f, "9", font, contentStream);
        text(ONE_CM * 0.49f, 0f, 20f, "0", font, contentStream);
        contentStream.endText();
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = getPath().resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Path getPath() {
        return Paths.get(fileUploadPath).toAbsolutePath().normalize();
    }
}
