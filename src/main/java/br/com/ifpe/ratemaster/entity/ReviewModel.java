package br.com.ifpe.ratemaster.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_review")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String comment;
	private Integer rating;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private ProductModel productModel;

}
