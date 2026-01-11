package org.baoxdev.hotelbooking_test.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.dto.request.PermissionRequest;
import org.baoxdev.hotelbooking_test.dto.response.PermissionResponse;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.mapper.PermissionMapper;
import org.baoxdev.hotelbooking_test.model.entity.Permission;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.baoxdev.hotelbooking_test.repository.PermissionRepository;
import org.baoxdev.hotelbooking_test.service.interfaces.IPermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class PermissionServiceImpl implements IPermissionService {
    PermissionMapper permissionMapper;
    PermissionRepository permissionRepository;

    @Override
    public PermissionResponse createPermission(PermissionRequest request) {
        Permission permission = permissionMapper.createPermissionFromResponse(request);
        return permissionMapper.convertPermissionToResponse(permissionRepository.save(permission));
    }

    @Override
    public PermissionResponse getPermission(String permissionId) {
        return permissionMapper.convertPermissionToResponse(permissionRepository.findById(permissionId).orElseThrow(()-> new AppException(ErrorCode.USER_EXISTED)));
    }

    @Override
    public List<PermissionResponse> getAllPermission() {
        return permissionRepository.findAll().stream().map(permission -> permissionMapper.convertPermissionToResponse(permission)).toList();
    }


    @Override
    public void deletePermission(String permissionId) {
        permissionRepository.deleteById(permissionId);
    }

}
