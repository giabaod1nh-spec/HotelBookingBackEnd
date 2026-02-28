package org.baoxdev.hotelbooking_test.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IpnResponse {
    private String RspCode;
    private String message;
}
