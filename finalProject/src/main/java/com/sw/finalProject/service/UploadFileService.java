package com.sw.finalProject.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
public class UploadFileService {
    public String upload(MultipartFile file) {
        boolean result = false;

        //File 저장
        String fileOriName = file.getOriginalFilename();
        String fileExtension = fileOriName.substring(fileOriName.lastIndexOf("."), fileOriName.length()); //확장자
        String uploadDir = "C:\\SpringMVC_2024\\Workspace\\imageUpload\\";

        UUID uuid = UUID.randomUUID(); //랜덤으로 이름 하나 만듦
        String uniqueName = uuid.toString().replaceAll("-", "");

        File saveFile = new File(uploadDir + "\\" + uniqueName + fileExtension);

        if (!saveFile.exists()) //파일 존재하지 않을 경우 저장X
            saveFile.mkdirs();

        try {
            file.transferTo(saveFile); //파일 존재하면 저장
            result = true;

        } catch (Exception e) {
            e.printStackTrace();

        }

        if (result) { //result = true일 경우
            System.out.println("[UploadFileService] FILE UPLOAD SUCCESS!!");
            return uniqueName + fileExtension; //이름 + 확장자

        } else {
            System.out.println("[UploadFileService] FILE UPLOAD FAIL!!");
            return null;

        }
    }
}
