package by.slemnev.Clinic.service.impl;

import by.slemnev.Clinic.model.Role;
import by.slemnev.Clinic.repository.RoleRepository;
import by.slemnev.Clinic.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByName(String name) {
      Role tl=roleRepository.findRolesById(2L);
//        System.out.println(tl.getId()+'|');
//
//        System.out.println(tl.toString()+'|');
        Role roleUser = roleRepository.findByName("ROLE_USER");
//        System.out.println(roleUser.getName());
//        System.out.println(roleUser.getUsers());
//        System.out.println(roleUser.getId());
//        System.out.println(roleUser.toString());
        return null;
    }
}
