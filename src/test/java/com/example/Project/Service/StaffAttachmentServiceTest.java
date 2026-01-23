package com.example.Project.Service;

import com.example.Project.model.staff.StaffAttachmentModel;
import com.example.Project.model.staff.StaffModel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.*;

import com.example.Project.repository.StaffAttachmentRepository;
import com.example.Project.repository.StaffRepository;
import com.example.Project.service.StaffAttachmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
;

@ExtendWith(MockitoExtension.class)
public class StaffAttachmentServiceTest {

    @Mock
    private StaffAttachmentRepository staffAttachmentRepository;

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private StaffAttachmentService staffAttachmentService;

    private StaffModel staff;

    @BeforeEach
    void setUp(){
        staff = new StaffModel();
        staff.setId(1);
        staff.setActive(true);
    }

    @Test
    void uploadFile_success() throws IOException {

        when(staffRepository.findByUser_IdAndActiveTrue(1)).thenReturn(Optional.of(staff));
        when(multipartFile.getOriginalFilename()).thenReturn("test.pdf");
        doNothing().when(multipartFile).transferTo(any(File.class));
        when(staffAttachmentRepository.save(any(StaffAttachmentModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StaffAttachmentModel result = staffAttachmentService.uploadFile(1, multipartFile);

        assertNotNull(result);
        assertEquals(staff, result.getStaff());
        assertTrue(result.getPath().contains("test.pdf"));
        assertTrue(result.getActive());

        verify(staffRepository, times(1)).findByUser_IdAndActiveTrue(1);
        verify(staffAttachmentRepository, times(1)).save(any(StaffAttachmentModel.class));
    }

    @Test
    void uploadFile_staffNotFound(){
        when(staffRepository.findByUser_IdAndActiveTrue(1)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> staffAttachmentService.uploadFile(1, multipartFile));

        assertEquals("Staff not registered for this user",ex.getMessage());
        verify(staffRepository,times(1)).findByUser_IdAndActiveTrue(1);
        verify(staffAttachmentRepository,never()).save(any());
    }

}
