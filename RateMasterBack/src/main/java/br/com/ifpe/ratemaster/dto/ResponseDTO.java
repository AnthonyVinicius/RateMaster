package br.com.ifpe.ratemaster.dto;

import br.com.ifpe.ratemaster.entity.ResponseModel;
import lombok.Data;

@Data
public class ResponseDTO {
    private Long id;
    private String userId;
    private String name;
    private String comment;
    private String createdAt;
    private Long reviewId;

    public ResponseDTO() {
        // Construtor padrão necessário para desserialização pelo Jackson
    }

    public ResponseDTO(ResponseModel response) {
        this.id = response.getId();
        this.name = response.getName();
        this.comment = response.getComment();
        this.createdAt = response.getCreatedAt();
        if (response.getUserModel() != null) {
            this.userId = response.getUserModel().getId();
        }
        if (response.getReview() != null) {
            this.reviewId = response.getReview().getId();
        }
    }
}
