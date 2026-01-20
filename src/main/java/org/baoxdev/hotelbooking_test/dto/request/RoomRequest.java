package org.baoxdev.hotelbooking_test.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.baoxdev.hotelbooking_test.model.entity.RoomType;
import org.baoxdev.hotelbooking_test.model.enums.RoomStatus;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomRequest {

    String roomNumber;

    RoomStatus roomStatus;

}
