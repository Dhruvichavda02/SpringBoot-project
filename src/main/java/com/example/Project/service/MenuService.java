package com.example.Project.service;

import com.example.Project.model.MenuModel;
import com.example.Project.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    //create
    public MenuModel createMenu(MenuModel menu){
        if(menuRepository.existsByName(menu.getName())){
            throw new RuntimeException("Menu item already exist with the same name!");
        }
        if (menu.getActive() == null) {
            menu.setActive(true);
        }

        return menuRepository.save(menu);
    }

    //get by id
    public MenuModel getById(Integer id){
        return menuRepository.findByIdAndActive(id).orElseThrow(()-> new RuntimeException("No such menu found!"));

    }

    // get all active
    public List<MenuModel> getAllActive(){
        return menuRepository.findAllActive();
    }

    //Update
    public MenuModel updateMenu(MenuModel menu){
        MenuModel existing = getById(menu.getId());

        existing.setName(menu.getName());
        existing.setDescription(menu.getDescription());
        existing.setPrice(menu.getPrice());
        if(menu.getActive() == null){
            existing.setActive(true);
        }
        existing.setActive(menu.getActive());
        return menuRepository.save(existing);
    }

    public void deactivated(Integer id){
        MenuModel menu = getById(id);
        menu.setActive(false);
        menuRepository.save(menu);
    }
}
