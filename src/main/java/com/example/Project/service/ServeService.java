package com.example.Project.service;

import com.example.Project.enums.TaskType;
import com.example.Project.enums.StaffRole;
import com.example.Project.model.BookingModel;
import com.example.Project.model.ServeModel;
import com.example.Project.model.customer.Customer;
import com.example.Project.model.staff.StaffModel;
import com.example.Project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class ServeService {

    @Autowired
    private ServeRepository serveRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CustomerRepository customerRepository;


    public void assignTask(TaskType task, Integer BookingId, Integer managerId) {

        StaffModel manager = staffRepository.findByIdAndActiveTrue(managerId).orElseThrow(() -> new RuntimeException("Manager not found"));
        if (manager.getRole() != StaffRole.MANAGER) {
            throw new RuntimeException("Only Manager can assign the role !");
        }

        BookingModel booking = bookingRepository.findById(BookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        Customer customer = customerRepository.findByIdAndActiveTrue(booking.getCustId()).orElseThrow(() -> new RuntimeException("Customer not found"));

        List<TaskType> finalTask = resolveTask(task);

        for (TaskType tasks : finalTask) {
            StaffModel staff = getStaffForTask(tasks);

            ServeModel serve = new ServeModel();
            serve.setServeActionType(tasks);
            serve.setCustomer(customer);
            serve.setBooking(booking);
            serve.setStaff(staff);
            serve.setManager_id(managerId);
            serve.setServeAt(LocalDateTime.now());
            serveRepository.save(serve);
        }

    }


    private List<TaskType> resolveTask(TaskType userTask) {
        LinkedHashSet<TaskType> ordered = new LinkedHashSet<>();
        if (userTask == TaskType.CLEAN) {
            ordered.add(TaskType.COOK);
            ordered.add(TaskType.SERVE);
            ordered.add(TaskType.CLEAN);
        } else if (userTask == TaskType.SERVE) {
            ordered.add(TaskType.COOK);
            ordered.add(TaskType.SERVE);
        } else {
            ordered.add(TaskType.COOK);
        }
        return new ArrayList<>(ordered);

    }

    private StaffModel getStaffForTask(TaskType task) {
        return switch (task) {
            case COOK ->
                    staffRepository.findFirstByRoleAndActiveTrue(StaffRole.CHEF).orElseThrow(() -> new RuntimeException("Chef not available"));

            case SERVE ->
                    staffRepository.findFirstByRoleAndActiveTrue(StaffRole.WAITER).orElseThrow(() -> new RuntimeException("Waiter not available"));

            case CLEAN ->
                    staffRepository.findFirstByRoleAndActiveTrue(StaffRole.ROOM_CLEANER).orElseThrow(() -> new RuntimeException("Room_cleaner not available"));
        };
    }

}
