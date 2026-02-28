package org.baoxdev.hotelbooking_test.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CloudinarySaveResponse {
    String imageUrl;
    String imagePublicId;
}
