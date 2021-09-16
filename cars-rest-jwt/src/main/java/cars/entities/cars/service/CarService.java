package cars.entities.cars.service;

import cars.entities.cars.model.CarAddServiceModel;
import cars.entities.cars.model.CarViewServiceModel;
import cars.entities.cars.model.ImageAddServiceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
@Validated
public interface CarService {


    CarViewServiceModel createCar(@Valid CarAddServiceModel carAddServiceModel,
                                  String headerAuth,
                                  @Valid ImageAddServiceModel imageAddServiceModel) throws IOException;

    CarViewServiceModel getCarById(Long id);

    CarViewServiceModel updateCar(@Valid CarAddServiceModel carAddServiceModel,
                   Long id,
                   @Valid ImageAddServiceModel imageAddServiceModel) throws IOException;

    CarViewServiceModel deleteCar(Long id);

    Page<CarViewServiceModel> getAllCars(Pageable pageable);

    Page<CarViewServiceModel> getAllCarsByOwnerId(Long id, Pageable pageable);

    Page<CarViewServiceModel> search(String keyword, Pageable pageable);

    Long getCarsRepositoryCount();

    Long getCarsRepositoryCountByOwnerId(Long id);

    Long getCarsRepositoryCountByKeyword(String keyword);
}
