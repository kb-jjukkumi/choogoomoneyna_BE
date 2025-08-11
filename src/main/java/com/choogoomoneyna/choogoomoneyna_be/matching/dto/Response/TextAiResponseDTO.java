package com.choogoomoneyna.choogoomoneyna_be.matching.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextAiResponseDTO {
    private int score;
}
