package org.baoxdev.hotelbooking_test.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomAssignmentResponse {
    String bookingRoomId ;
    String roomId;
}
