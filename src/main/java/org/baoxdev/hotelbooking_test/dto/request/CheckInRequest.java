package org.baoxdev.hotelbooking_test.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CheckInRequest {
    private List<String> roomId;  //List roomId to assign to booking
}
