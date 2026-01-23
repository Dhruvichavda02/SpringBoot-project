package com.example.Project.Service;

import com.example.Project.enums.BookingCategory;
import com.example.Project.model.ResourceMstModel;
import com.example.Project.repository.ResourceMstRepository;
import com.example.Project.service.ResourceMstService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResourceMstServiceTest {

    @Mock
    private ResourceMstRepository resourceMstRepository;

    @InjectMocks
    private ResourceMstService resourceMstService;
    private ResourceMstModel resource;

    @BeforeEach
    void setUp(){
        resource = new ResourceMstModel();
        resource.setActive(true);
        resource.setId(1);
    }

    @Test
    void create_success(){
        when(resourceMstRepository.save(resource)).thenAnswer(i->i.getArgument(0));

        ResourceMstModel result = resourceMstService.create(resource);

        assertNotNull(result);
        assertEquals(1,result.getId());

    }

    @Test
    void getAll_success(){
        when(resourceMstRepository.findAll()).thenReturn(List.of(resource));

        List<ResourceMstModel> result = resourceMstService.getAll();

        assertNotNull(result);
        assertEquals(1,result.size());
        verify(resourceMstRepository,times(1)).findAll();

    }

    @Test
    void getById_success(){
        when(resourceMstRepository.findById(1)).thenReturn(Optional.of(resource));

        ResourceMstModel result = resourceMstService.getById(1);

        assertNotNull(result);
        assertEquals(1,result.getId());
        verify(resourceMstRepository,times(1)).findById(1);
    }

    @Test
    void getById_resourceNotFound(){
        when(resourceMstRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,()->resourceMstService.getById(1));

        assertEquals("Not found!",ex.getMessage());
        verify(resourceMstRepository,times(1)).findById(1);
    }

    @Test
    void updateById_success(){
        ResourceMstModel updated  = new ResourceMstModel();
        updated.setCategory(BookingCategory.TABLE);

        when(resourceMstRepository.findById(1)).thenReturn(Optional.of(resource));
        when(resourceMstRepository.save(resource)).thenAnswer(i->i.getArgument(0));

        ResourceMstModel res = resourceMstService.updateById(1,updated);

        assertEquals(BookingCategory.TABLE,res.getCategory());
        verify(resourceMstRepository,times(1)).findById(1);
        verify(resourceMstRepository,times(1)).save(any(ResourceMstModel.class));

    }

    @Test
    void updateById_ResourceNotFound(){
        ResourceMstModel updated  = new ResourceMstModel();
        updated.setCategory(BookingCategory.TABLE);

        when(resourceMstRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,()->resourceMstService.updateById(1,updated));

        assertEquals("Not found!",ex.getMessage());
        verify(resourceMstRepository,times(1)).findById(1);
        verify(resourceMstRepository,never()).save(any(ResourceMstModel.class));

    }

    @Test
    void deleteById_success(){
        resource.setActive(true);
        when(resourceMstRepository.findByIdAndActiveTrue(1)).thenReturn(Optional.of(resource));
        when(resourceMstRepository.save(resource)).thenAnswer(i->i.getArgument(0));

        assertEquals("Resource deactivated successfully",resourceMstService.deleteById(1));
        verify(resourceMstRepository,times(1)).findByIdAndActiveTrue(1);
        verify(resourceMstRepository,times(1)).save(any(ResourceMstModel.class));
    }


}
