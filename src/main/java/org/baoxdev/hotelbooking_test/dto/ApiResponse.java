package org.baoxdev.hotelbooking_test.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)  //nhung truong null ko in ra
@Builder
public class ApiResponse<T> {
    int code = 1000;
    String message;
    T result;
}
