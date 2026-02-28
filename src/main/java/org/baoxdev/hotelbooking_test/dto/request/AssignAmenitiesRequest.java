package org.baoxdev.hotelbooking_test.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignAmenitiesRequest {
    @NotNull
    List<String> amenitiesName;
}
