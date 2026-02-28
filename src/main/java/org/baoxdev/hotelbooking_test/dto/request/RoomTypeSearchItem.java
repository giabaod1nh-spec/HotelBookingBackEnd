package org.baoxdev.hotelbooking_test.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoomTypeSearchItem {
    String roomTypeId;
    String roomTypeName;
    String roomTypeDesc;
    Integer maxOccupy;
    BigDecimal totalPrice;
    String primaryImageUrl;
}
