package com.alooa.swish.dto.response;

import java.util.List;

public record ErrorResponse(
        int statusCode,
        List<String> errors,
        String message
) {
}
