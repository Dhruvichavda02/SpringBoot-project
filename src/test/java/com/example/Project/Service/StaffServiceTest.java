package com.example.Project.Service;
import com.example.Project.DTOs.StaffRequest;
import com.example.Project.DTOs.StaffResponseDTO;
import com.example.Project.enums.StaffRole;
import com.example.Project.model.UserMST;
import com.example.Project.model.staff.StaffModel;
import com.example.Project.repository.StaffAttachmentRepository;
import com.example.Project.repository.StaffRepository;
import com.example.Project.repository.UserMstRepository;
import com.example.Project.service.StaffServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StaffServiceTest {

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private UserMstRepository userMstRepository;

    @Mock
    private StaffAttachmentRepository staffAttachmentRepository;

    @InjectMocks
    private StaffServices staffServices;

    private StaffRequest staffRequest;
    private StaffModel staff;
    private UserMST user;

    @BeforeEach
    void setUp(){
        staffRequest = new StaffRequest();
        staffRequest.setUserId(1);
        staffRequest.setName("John");
        staffRequest.setDate_of_joining(LocalDate.now());

        user = new UserMST();
        user.setId(1);

        staff = new StaffModel();
        staff.setId(1);
        staff.setName("John");
        staff.setRole(StaffRole.ADMIN);
        staff.setDate_of_joining(LocalDate.now());
        staff.setActive(true);
    }

    @Test
    void createStaff_success(){
        when(userMstRepository.findById(1)).thenReturn(Optional.of(user));
        when(staffRepository.save(any(StaffModel.class))).thenAnswer(i ->i.getArgument(0));

        StaffModel res = staffServices.createStaff(staffRequest);
        assertEquals("John",res.getName());
        assertTrue(res.getActive());
        verify(staffRepository,times(1)).save(any(StaffModel.class));


    }

    @Test
    void createStaff_fail(){
        when(userMstRepository.findById(1)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                ()-> staffServices.createStaff(staffRequest)
        );
    }

    @Test
    void getAllStaff_success(){
        when(staffRepository.findByActiveTrue()).thenReturn(List.of(new StaffModel(),new StaffModel()));

        List<StaffModel> res = staffServices.getAllStaff();

        assertEquals(2,res.size());
        verify(staffRepository,times(1)).findByActiveTrue();
    }

    @Test
    void update_success(){
        StaffRequest updatedRequest = new StaffRequest();
        updatedRequest.setName("Chandler");
        updatedRequest.setDate_of_joining(LocalDate.now());

        when(staffRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(staff));
        when(staffRepository.save(any(StaffModel.class))).thenAnswer(i->i.getArgument(0));

        StaffModel res = staffServices.UpdateStaff(updatedRequest,1);
        assertEquals("Chandler",res.getName());

        verify(staffRepository,times(1)).save(staff);
    }

    @Test
    void update_fail(){
        when(staffRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                ()->staffServices.UpdateStaff(staffRequest,1)
        );
        assertEquals("Staff not found",ex.getMessage());
        verify(staffRepository,never()).save(any());
    }

    @Test
    void deactive_success(){
        when(staffRepository.findByIdAndActiveTrue(1))
                .thenReturn(Optional.of(staff));

        String result = staffServices.deactiveStaff(1);

        assertEquals("Staff deactivated successfully", result);
        assertFalse(staff.getActive());

        verify(staffRepository).save(staff);
    }

    @Test
    void deactive_notFound(){
        when(staffRepository.findByIdAndActiveTrue(1))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> staffServices.deactiveStaff(1)
        );

        assertEquals("User does not exist!", ex.getMessage());
        verify(staffRepository, never()).save(any());
    }

    @Test
    void FilterByRole_success(){
        when(staffRepository.findByRoleAndActiveTrue(StaffRole.ADMIN)).thenReturn(List.of(staff));

        List<StaffModel> res = staffServices.FilterByRole(StaffRole.ADMIN);

        assertEquals(1,res.size());
        verify(staffRepository).findByRoleAndActiveTrue(StaffRole.ADMIN);
    }

    @Test
    void getStaffById_success(){
        when(staffRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(staff));
        when(staffAttachmentRepository.findByStaff_IdAndActiveTrue(1)).thenReturn(List.of());

        StaffResponseDTO res = staffServices.getStaffById(1);

        assertNotNull(res);
        assertEquals("John",res.getName());
       assertTrue(res.getAttachmentDTOS().isEmpty());
    }

    @Test
    void getStaffById_notFound() {

        when(staffRepository.findByIdAndActiveTrue(1))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> staffServices.getStaffById(1)
        );

        assertEquals("Staff not registered", ex.getMessage());
    }
}
