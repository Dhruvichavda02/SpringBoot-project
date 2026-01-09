package com.example.Project.service;

import com.example.Project.model.ResourceMstModel;
import com.example.Project.repository.ResourceMstRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceMstService {

    @Autowired
    private ResourceMstRepository resourceMstRepository;

    //create
    public ResourceMstModel create(ResourceMstModel resource) {
        return resourceMstRepository.save(resource);
    }

    //getAll
    public List<ResourceMstModel> getAll() {
        return resourceMstRepository.findAll();
    }

    // getById
    public ResourceMstModel getById(Integer id) {
        return resourceMstRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found!"));
    }

    //update
    public ResourceMstModel updateById(Integer id,ResourceMstModel resource) {
        ResourceMstModel r = getById(id);
        r.setCategory(resource.getCategory());
        r.setActive(resource.getActive());
        r.setCapacity(resource.getCapacity());
        r.setCode(resource.getCode());
        return resourceMstRepository.save(r);
    }

    //deactived
    public String deleteById(Integer id) {
        ResourceMstModel resource = resourceMstRepository.findByIdAndActiveTrue(id).orElseThrow(()-> new RuntimeException("User does not exist!"));
        resource.setActive(false);
        resourceMstRepository.save(resource);
        return "Customer deactivated successfully";

    }
}