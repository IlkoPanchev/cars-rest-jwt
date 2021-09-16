package cars.entities.cars.repository;

import cars.entities.cars.model.CarEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarsRepository extends JpaRepository<CarEntity, Long> {


    @Query("SELECT count(c.owner.id)  FROM CarEntity c WHERE c.owner.id = :id")
    Long getCarsCountByOwnerId(@Param("id") Long id);

    Page<CarEntity> findAllByOwnerId(Long id, Pageable pageable);

    @Query("SELECT c FROM CarEntity c WHERE CONCAT(lower(c.brand), lower(c.model), concat(c.price, ''), lower(c.description)," +
             " concat(c.year, '')) LIKE lower(concat('%', ?1, '%'))")
    Page<CarEntity> search(String keyword, Pageable pageable);

    @Query("SELECT count(c.owner.id) FROM CarEntity c WHERE CONCAT(lower(c.brand), lower(c.model), concat(c.price, ''), lower(c.description)," +
             " concat(c.year, '')) LIKE lower(concat('%', ?1, '%'))")
    Long searchResultCount(String keyword);
}
