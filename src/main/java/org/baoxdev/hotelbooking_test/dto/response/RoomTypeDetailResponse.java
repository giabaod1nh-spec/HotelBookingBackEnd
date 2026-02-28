package org.baoxdev.hotelbooking_test.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomTypeDetailResponse {
    private String roomTypeId;

    private String roomTypeName;

    private String roomTypeDesc;

    private String bedSummary;

    private BigDecimal basePrice;

    private Integer maxOccupy;

    private Integer totalRooms;

    private Integer availableCount;

    private BigDecimal totalPriceForStay;
}
