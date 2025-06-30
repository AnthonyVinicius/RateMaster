package br.com.ifpe.ratemaster.repository;

import br.com.ifpe.ratemaster.entity.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductModel, Long> {
    List<ProductModel> findByCategoryModelName(String categoryName);
    List<ProductModel> findByUserModelId(String userId);

    @Query("""
    SELECT p FROM ProductModel p
    LEFT JOIN FETCH p.reviewModels r
    LEFT JOIN FETCH r.response
    WHERE p.id = :id
""")
    Optional<ProductModel> findByIdWithReviewsAndResponses(@Param("id") Long id);

    @Query("""
    SELECT DISTINCT p FROM ProductModel p
    JOIN p.reviewModels r
    WHERE r.user.id = :userId
""")
    List<ProductModel> findDistinctByReviewModelsUserId(@Param("userId") String userId);


}
