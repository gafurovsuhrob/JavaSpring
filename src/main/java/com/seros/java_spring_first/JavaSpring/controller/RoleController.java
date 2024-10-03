package com.seros.java_spring_first.JavaSpring.controller;


import com.seros.java_spring_first.JavaSpring.model.Role;
import com.seros.java_spring_first.JavaSpring.service.RoleService;
import com.seros.java_spring_first.JavaSpring.utils.Constants;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.BASE_URL + Constants.ROLES)
@AllArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping(Constants.ROLE_ALL)
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(roleService.readAllRoles(), HttpStatus.OK);
    }

    @GetMapping(Constants.ROLE_ID)
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return new ResponseEntity<>(roleService.readById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        return new ResponseEntity<>(roleService.createRole(role), HttpStatus.CREATED);
    }

    @PutMapping(Constants.ROLE_ID)
    public Role updateUser(@PathVariable Long id, @RequestParam String roleName) {
        return roleService.updateRole(id, roleName);
    }

    @DeleteMapping(Constants.ROLE_ID)
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
    }
}