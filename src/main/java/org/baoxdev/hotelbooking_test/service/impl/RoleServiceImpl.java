package org.baoxdev.hotelbooking_test.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.request.RoleRequest;
import org.baoxdev.hotelbooking_test.dto.response.RoleResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.mapper.RoleMapper;
import org.baoxdev.hotelbooking_test.mapper.UserMapper;
import org.baoxdev.hotelbooking_test.model.entity.Role;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.repository.RoleRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    private final UserMapper userMapper;

    @Override
    public RoleResponse createRole(RoleRequest request) {
        Role role = roleMapper.convertRoleFromRequest(request);
        return roleMapper.convertResponseFromRole(roleRepository.save(role));
    }

    @Override
    public RoleResponse getRole(String roleId) {
        return roleMapper.convertResponseFromRole(roleRepository.findById(roleId).orElseThrow(() ->new AppException(ErrorCode.USER_EXISTED)));
    }

    @Override
    public List<RoleResponse> getAllRole() {

        return roleRepository.findAll().stream().map(role -> roleMapper.convertResponseFromRole(role)).toList();
    }

    @Override
    public void deleteRole(String roleId) {
        roleRepository.deleteById(roleId);
    }


}
