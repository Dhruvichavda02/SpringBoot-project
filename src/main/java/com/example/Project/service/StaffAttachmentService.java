package com.example.Project.service;

import com.example.Project.model.staff.StaffAttachmentModel;
import com.example.Project.model.staff.StaffModel;
import com.example.Project.repository.StaffAttachmentRepo;
import com.example.Project.repository.StaffRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class StaffAttachmentService {

    @Autowired
    StaffAttachmentRepo staffAttachmentrepo;
    @Autowired
    private StaffRepo staffRepo;


    private static final String uploadDir = System.getProperty("user.dir")+"/uploads/staff";


    public StaffAttachmentModel uploadFile(Integer userId, MultipartFile file) throws IOException {

        StaffModel staff = staffRepo.findByUser_IdAndActiveTrue(userId)
                .orElseThrow(() ->
                        new RuntimeException("Staff not registered for this user"));
        //create folder
        File dir = new File(uploadDir);
        if(!dir.exists()){
            dir.mkdirs();
        }

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filepath = uploadDir + File.separator + filename;

        file.transferTo(new File(filepath));
        //save path to db
        StaffAttachmentModel attachment = new StaffAttachmentModel();
        attachment.setActive(true);
       attachment.setStaff(staff);
        attachment.setPath(filepath);

        return staffAttachmentrepo.save(attachment);
    }
}
