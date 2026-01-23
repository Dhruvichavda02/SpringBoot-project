package com.example.Project.Service;

import com.example.Project.model.MenuModel;
import com.example.Project.repository.MenuRepository;
import com.example.Project.service.MenuService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    private MenuModel menu;

    @BeforeEach
    void setUp(){
         menu = new MenuModel();
        menu.setName("Pizza");
        menu.setDescription("Chessy blast");
        menu.setActive(true);
        menu.setId(1);
        menu.setPrice(400.00);
    }

    @Test
    void createMenu_success(){
       when(menuRepository.save(any(MenuModel.class))).thenAnswer(i->i.getArgument(0));

       MenuModel result = menuService.createMenu(menu);
       assertEquals("Pizza",result.getName());
       assertEquals("Chessy blast",result.getDescription());
       assertTrue(result.getActive());
       verify(menuRepository,times(1)).save(menu);
    }

    @Test
    void createMenu_alreadyExist(){
        when(menuRepository.existsByName("Pizza")).thenReturn(true);
        RuntimeException ex = assertThrows(RuntimeException.class,()->menuService.createMenu(menu));

        assertEquals("Menu item already exist with the same name!",ex.getMessage());
        verify(menuRepository,times(1)).existsByName("Pizza");
        verify(menuRepository,times(0)).save(any());
    }

    @Test
    void getById_success(){
        when(menuRepository.findByIdAndActive(1)).thenReturn(Optional.of(menu));
        MenuModel result = menuService.getById(1);

        assertEquals(menu,result);
        verify(menuRepository,times(1)).findByIdAndActive(1);
    }

    @Test
    void getById_menuNotFound(){
        when(menuRepository.findByIdAndActive(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,()->menuService.getById(1));
        assertEquals("No such menu found!",ex.getMessage());
        verify(menuRepository,times(1)).findByIdAndActive(1);
    }

    @Test
    void getAllActive_success(){
        when(menuRepository.findAllActive()).thenReturn(List.of(menu));

        assertEquals(1,menuService.getAllActive().size());
        verify(menuRepository,times(1)).findAllActive();
    }

    @Test
    void deactivated_success(){
        when(menuRepository.findByIdAndActive(1)).thenReturn(Optional.of(menu));
        when(menuRepository.save(any(MenuModel.class))).thenReturn(menu);
        menuService.deactivated(1);

        assertFalse(menu.getActive());
        verify(menuRepository,times(1)).save(menu);
    }

    @Test
    void deactivated_menuNotFound(){
        when(menuRepository.findByIdAndActive(1)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,()->menuService.deactivated(1));

        assertEquals("No such menu found!",ex.getMessage());
        verify(menuRepository,times(1)).findByIdAndActive(1);
    }

    @Test
    void updateMenu_success(){
        MenuModel updatedMenu = new MenuModel();
        updatedMenu.setId(1);
         updatedMenu.setName("Pasta");
         updatedMenu.setPrice(200.00);

         when(menuRepository.findByIdAndActive(1)).thenReturn(Optional.of(menu));
         when(menuRepository.save(any(MenuModel.class))).thenAnswer(i->i.getArgument(0));
         MenuModel result = menuService.updateMenu(updatedMenu);

         assertEquals("Pasta",result.getName());
         assertEquals(200.00,result.getPrice());
         verify(menuRepository,times(1)).save(menu);
    }

    @Test
    void updateMenu_menuNotFound(){
        MenuModel updatedMenu = new MenuModel();
        updatedMenu.setId(1);
        updatedMenu.setName("Pasta");
        updatedMenu.setPrice(200.00);

        when(menuRepository.findByIdAndActive(1)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,()->menuService.updateMenu(updatedMenu));

        assertEquals("No such menu found!",ex.getMessage());
        verify(menuRepository,never()).save(any());

    }
}
