package org.baoxdev.hotelbooking_test.dto.request;

import lombok.Data;

@Data
public class BookingRoomItemRequest {
    private String roomTypeId ;
    private Integer quantity;  //number of rooms of this roomType
}
