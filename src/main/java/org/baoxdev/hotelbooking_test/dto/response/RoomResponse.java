package org.baoxdev.hotelbooking_test.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.baoxdev.hotelbooking_test.model.enums.RoomStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomResponse {
    String roomNumber;

    RoomStatus roomStatus;
}
