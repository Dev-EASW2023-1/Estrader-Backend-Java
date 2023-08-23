package com.example.demo.app.domain.model.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Copyright [2023] [Nam Jae Gyeong]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

@Slf4j
@Component
public class PDFUtil {
    @Value("${file.claimdoc-dir}")
    private String fileClaimDocPath;
    @Value("${file.font-dir}")
    private String fileFontPath;
    @Value("${file.upload-dir-windows}")
    private String windowsFileUploadPath;
    @Value("${file.upload-dir-docker}")
    private String dockerFileUploadPath;

    private static final Float ONE_CM = 28.346f;

    /**
     * PDF 생성 코드
     *
     * 생성한 PDF 파일을 저장하지 않고 바로 Resource 로 반환하는 방식 적용
     * 추후 요구 사항에 따라 S3에 ttl 걸고 저장 혹은 Cloudfront + Signed URLs 으로 url 인증 절차 적용 예정
     *
     */
    public Resource createPdf() {
        String docFileName = "bidSheet.pdf";
        String fontFileName = "nanumgothictext.ttf";

        File docFile = fileCall(fileClaimDocPath, docFileName,"pdf");
        File fontFile = fileCall(fileFontPath, fontFileName,"ttf");

        byte[] bytes;

        try (PDDocument doc = PDDocument.load(docFile);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
        {
            PDType0Font font = PDType0Font.load(doc, fontFile);
            PDPage page = doc.getPage(0);
            writePdf(doc, font, page);
            doc.save(byteArrayOutputStream);
            bytes = byteArrayOutputStream.toByteArray();
        }
        catch (IOException e)
        {
            log.error("PDF 파일 생성 실패");
            return null;
        }
        // 파일을 Resource 타입으로 변환
        return new ByteArrayResource(bytes);
    }

    // 파일명에 아이디와 생성일자를 조합하여 생성
    public String generateFileName(String userId) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String creationDate = currentDateTime.format(formatter);
        return userId + "_" + creationDate + ".pdf";
    }

    // 운영 체제(windows, ubuntu docker) 에 따른 파일 절대 경로 설정
    public String getFilePath() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            return windowsFileUploadPath;
        } else {
            return dockerFileUploadPath;
        }
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
            log.error("PDF 파일 생성 실패");
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

    // jar 파일 배포 시 프로토콜이 다르기 때문에 ClassPathResource 통한 패키지 내부 리소스 파일 가져오기
    public Resource fileCall(
            String filePath,
            String title
    ) {
        ClassPathResource resource = new ClassPathResource(filePath + title);
        if (resource.exists()) {
            return resource;
        } else {
            return null;
        }
    }

    // jar 파일 배포 시 프로토콜이 다르기 때문에 InputStream 통해 파일 처리
    // inputStream 객체에서 File 임시 객체 만들기
    public File fileCall(
            String filePath,
            String title,
            String fileType
    ) {
        try (InputStream inputStream = new ClassPathResource(filePath + title).getInputStream()){
            File tempFile = File.createTempFile(String.valueOf(inputStream.hashCode()), "." + fileType);
            tempFile.deleteOnExit();
            copyInputStreamToFile(inputStream, tempFile);
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.toString());
        }
        return null;
    }

    private void copyInputStreamToFile(InputStream inputStream, File file) {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.toString());
        }
    }

    // 지정된 파일명의 리소스를 로드하여 Resource 객체로 반환
    public Resource loadAsResource(
            String filePath,
            String filename
    ) {
        try {
            Path file = getPath(filePath).resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            log.error(e.toString());
        }
        return null;
    }

    private Path getPath(String filePath) {
        return Paths.get(filePath).toAbsolutePath().normalize();
    }
}
