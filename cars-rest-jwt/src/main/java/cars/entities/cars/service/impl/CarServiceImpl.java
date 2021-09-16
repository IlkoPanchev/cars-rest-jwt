package cars.entities.cars.service.impl;

import cars.cloudinary.CloudinaryService;
import cars.constants.GlobalConstants;
import cars.entities.cars.model.*;
import cars.entities.cars.repository.CarsRepository;
import cars.entities.cars.service.CarService;
import cars.entities.users.model.UserEntity;
import cars.entities.users.repository.UserRepository;
import cars.jwt.JwtProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CarServiceImpl implements CarService {

    private final CarsRepository carsRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public CarServiceImpl(CarsRepository carsRepository, ModelMapper modelMapper, UserRepository userRepository, JwtProvider jwtProvider, CloudinaryService cloudinaryService) {
        this.carsRepository = carsRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public CarViewServiceModel createCar(@Valid CarAddServiceModel carAddServiceModel,
                                         String headerAuth,
                                         @Valid ImageAddServiceModel imageAddServiceModel) throws IOException {

        CarEntity carEntity = this.modelMapper.map(carAddServiceModel, CarEntity.class);
        MultipartFile multipartFile = imageAddServiceModel.getImageUrl();
        if(multipartFile != null){
            carEntity.setImageUrl(this.getCloudinaryLink(multipartFile));
        }else {
            carEntity.setImageUrl(GlobalConstants.NO_IMAGE_AVAILABLE);
        }
        String authToken = headerAuth.substring(7);
        String username = jwtProvider.getUserNameFromJwtToken(authToken);
        UserEntity userEntity = this.userRepository.
                findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(GlobalConstants.USER_NOT_FOUND + username));
        carEntity.setOwner(userEntity);

        carEntity = carsRepository.saveAndFlush(carEntity);
        CarViewServiceModel carViewServiceModel = this.modelMapper.map(carEntity, CarViewServiceModel.class);

        return carViewServiceModel;
    }

    @Override
    public CarViewServiceModel getCarById(Long id) {

        CarEntity carEntity = this.carsRepository.
                findById(id).orElseThrow(() -> new EntityNotFoundException(GlobalConstants.CAR_NOT_FOUND + id));

        CarViewServiceModel carViewServiceModel = this.modelMapper.map(carEntity, CarViewServiceModel.class);

        return carViewServiceModel;
    }

    @Override
    public CarViewServiceModel updateCar(@Valid CarAddServiceModel carAddServiceModel,
                          Long id,
                          @Valid ImageAddServiceModel imageAddServiceModel) throws IOException {

        CarEntity carEntity = this.carsRepository.
                findById(id).orElseThrow(() -> new EntityNotFoundException(GlobalConstants.CAR_NOT_FOUND + id));
        carEntity.setBrand(carAddServiceModel.getBrand());
        carEntity.setModel(carAddServiceModel.getModel());
        carEntity.setDescription(carAddServiceModel.getDescription());
        carEntity.setYear(carAddServiceModel.getYear());
        MultipartFile multipartFile = imageAddServiceModel.getImageUrl();
        if(multipartFile != null){
            carEntity.setImageUrl(this.getCloudinaryLink(multipartFile));
        }
        carEntity.setPrice(carAddServiceModel.getPrice());

        carEntity = this.carsRepository.saveAndFlush(carEntity);
        CarViewServiceModel carViewServiceModel = this.modelMapper.map(carEntity, CarViewServiceModel.class);

        return carViewServiceModel;
    }

    @Override
    public CarViewServiceModel deleteCar(Long id) {

        CarEntity carEntity = this.carsRepository.
                findById(id).orElseThrow(() -> new EntityNotFoundException(GlobalConstants.CAR_NOT_FOUND + id));
        this.carsRepository.deleteById(id);

        CarViewServiceModel carViewServiceModel = this.modelMapper.map(carEntity, CarViewServiceModel.class);

        return carViewServiceModel;
    }

    @Override
    public Page<CarViewServiceModel> getAllCars(Pageable pageable) {

        Page<CarEntity> carEntities = this.carsRepository.findAll(pageable);

        List<CarViewServiceModel> carViewServiceModels = carEntities.stream()
                .map(carEntity -> this.modelMapper.map(carEntity, CarViewServiceModel.class))
                .collect(Collectors.toList());

        Page<CarViewServiceModel> carViewServiceModelPage = new PageImpl<>(carViewServiceModels, pageable, carEntities.getTotalElements());

        return carViewServiceModelPage;
    }

    @Override
    public Page<CarViewServiceModel> getAllCarsByOwnerId(Long id, Pageable pageable) {

        Page<CarEntity> carEntities = this.carsRepository.findAllByOwnerId(id, pageable);

        List<CarViewServiceModel> carViewServiceModels = carEntities.stream()
                .map(carEntity -> this.modelMapper.map(carEntity, CarViewServiceModel.class))
                .collect(Collectors.toList());

        Page<CarViewServiceModel> carViewServiceModelPage = new PageImpl<>(carViewServiceModels, pageable, carEntities.getTotalElements());

        return carViewServiceModelPage;
    }

    @Override
    public Page<CarViewServiceModel> search(String keyword, Pageable pageable) {

        Page<CarEntity> carEntities = this.carsRepository.search(keyword, pageable);

        List<CarViewServiceModel> carViewServiceModels = carEntities.stream()
                .map(carEntity -> this.modelMapper.map(carEntity, CarViewServiceModel.class))
                .collect(Collectors.toList());

        Page<CarViewServiceModel> carViewServiceModelPage = new PageImpl<>(carViewServiceModels, pageable, carEntities.getTotalElements());

        return carViewServiceModelPage;
    }

    @Override
    public Long getCarsRepositoryCount() {

        Long carsCount = this.carsRepository.count();
        return carsCount;
    }

    @Override
    public Long getCarsRepositoryCountByOwnerId(Long id) {

        return this.carsRepository.getCarsCountByOwnerId(id);
    }

    @Override
    public Long getCarsRepositoryCountByKeyword(String keyword) {

        Long carsCount = this.carsRepository.searchResultCount(keyword);
        return carsCount;
    }

    private String getCloudinaryLink(MultipartFile multipartFile) throws IOException {

        String img = null;

        img = this.cloudinaryService.uploadImage(multipartFile);

        return img;
    }
}
