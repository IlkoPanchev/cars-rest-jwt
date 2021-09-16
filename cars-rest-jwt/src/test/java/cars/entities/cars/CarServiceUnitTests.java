package cars.entities.cars;

import cars.cloudinary.CloudinaryService;
import cars.entities.cars.model.CarAddServiceModel;
import cars.entities.cars.model.CarEntity;
import cars.entities.cars.model.CarViewServiceModel;
import cars.entities.cars.model.ImageAddServiceModel;
import cars.entities.cars.repository.CarsRepository;
import cars.entities.cars.service.CarService;
import cars.entities.cars.service.impl.CarServiceImpl;
import cars.entities.roles.model.RoleEntity;
import cars.entities.roles.model.RoleName;
import cars.entities.users.model.UserEntity;
import cars.entities.users.repository.UserRepository;
import cars.jwt.JwtProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CarServiceUnitTests {

    private final Long CAR_ID = 1L;
    private final String BRAND = "Audi";
    private final String MODEL = "A3";
    private final String DESCRIPTION = "Description";
    private final int YEAR = 2020;
    private final String IMAGE_URL = "image_url";
    private final BigDecimal PRICE = new BigDecimal("1000");

    private final Long USER_ID = 1L;
    private final String USERNAME = "username";
    private final String PASSWORD = "password";
    private final String EMAIL = "email@abv.bg";

    private final String HEADER_AUTH = "header_auth";

    private final int PAGE = 0;
    private final int PAGE_SIZE = 3;


    private CarService carServiceToTest;
    private CarEntity carEntity;
    private CarEntity existingCarEntity;
    private UserEntity existingUserEntity;
    private CarAddServiceModel carAddServiceModel;
    private ImageAddServiceModel imageAddServiceModel;
    private Pageable pageable;
    private Page<CarEntity> carEntityPage;


    @Mock
     CarsRepository mockCarsRepository;
    @Mock
     UserRepository mockUserRepository;
    @Mock
     JwtProvider mockJwtProvider;
    @Mock
     CloudinaryService mockCloudinaryService;

    @BeforeEach
    public void setUp(){
        this.carServiceToTest = new CarServiceImpl(mockCarsRepository,
                new ModelMapper(),
                mockUserRepository,
                mockJwtProvider,
                mockCloudinaryService);
        this.carEntity = this.createCarEntity();
        this.existingUserEntity = this.createExistingUserEntity();
        this.carAddServiceModel = this.createCarAddServiceModel();
        this.imageAddServiceModel = this.createImageAddServiceModel();
        this.existingCarEntity = this.createExistingCarEntity();
        this.pageable = this.createPageable();
        this.carEntityPage = this.createCarEntityPage();

    }


    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testCreateCar() throws IOException {

        when(mockCarsRepository.saveAndFlush(any(CarEntity.class))).thenReturn(this.existingCarEntity);
        when(mockUserRepository.findByUsername(any(String.class))).thenReturn(Optional.of(this.existingUserEntity));
        when(mockJwtProvider.getUserNameFromJwtToken(any(String.class))).thenReturn(this.USERNAME);
        when(mockCloudinaryService.uploadImage(any(MultipartFile.class))).thenReturn(this.IMAGE_URL);

        this.carServiceToTest.createCar(this.carAddServiceModel,
                this.HEADER_AUTH,
                this.imageAddServiceModel);

        ArgumentCaptor<CarEntity> argument = ArgumentCaptor.forClass(CarEntity.class);
        Mockito.verify(mockCarsRepository, times(1)).saveAndFlush(argument.capture());
        CarEntity carEntityActual = argument.getValue();

        Assertions.assertEquals(carEntityActual.getBrand(), carAddServiceModel.getBrand());
        Assertions.assertEquals(carEntityActual.getModel(), carAddServiceModel.getModel());
        Assertions.assertEquals(carEntityActual.getDescription(), carAddServiceModel.getDescription());
        Assertions.assertEquals(carEntityActual.getYear(), carAddServiceModel.getYear());
        Assertions.assertEquals(carEntityActual.getImageUrl(), this.IMAGE_URL);
        Assertions.assertEquals(carEntityActual.getPrice(), carAddServiceModel.getPrice());
        Assertions.assertEquals(carEntityActual.getOwner().getId(), this.existingUserEntity.getId());

    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testCreateCarReturnCarViewServiceModel() throws IOException {

        when(mockCarsRepository.saveAndFlush(any(CarEntity.class))).thenReturn(this.existingCarEntity);
        when(mockUserRepository.findByUsername(any(String.class))).thenReturn(Optional.of(this.existingUserEntity));
        when(mockJwtProvider.getUserNameFromJwtToken(any(String.class))).thenReturn(this.USERNAME);
        when(mockCloudinaryService.uploadImage(any(MultipartFile.class))).thenReturn(this.IMAGE_URL);

        CarViewServiceModel carViewServiceModel = this.carServiceToTest.createCar(this.carAddServiceModel,
                this.HEADER_AUTH,
                this.imageAddServiceModel);

        Assertions.assertEquals(carViewServiceModel.getId(), this.CAR_ID);
        Assertions.assertEquals(carViewServiceModel.getBrand(), carAddServiceModel.getBrand());
        Assertions.assertEquals(carViewServiceModel.getModel(), carAddServiceModel.getModel());
        Assertions.assertEquals(carViewServiceModel.getDescription(), carAddServiceModel.getDescription());
        Assertions.assertEquals(carViewServiceModel.getYear(), carAddServiceModel.getYear());
        Assertions.assertEquals(carViewServiceModel.getImageUrl(), this.IMAGE_URL);
        Assertions.assertEquals(carViewServiceModel.getPrice(), carAddServiceModel.getPrice());
        Assertions.assertEquals(carViewServiceModel.getOwnerId(), this.existingUserEntity.getId());

    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testCreateCarThrowsException(){

        when(mockUserRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () ->   this.carServiceToTest.createCar(this.carAddServiceModel,
                        this.HEADER_AUTH,
                        this.imageAddServiceModel));
    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testGetCarByIdReturnCarViewServiceModel() {

        when(mockCarsRepository.findById(any(Long.class))).thenReturn(Optional.of(this.existingCarEntity));


        CarViewServiceModel carViewServiceModel = this.carServiceToTest.getCarById(1L);

        Assertions.assertEquals(carViewServiceModel.getId(), 1L);
        Assertions.assertEquals(carViewServiceModel.getBrand(), this.BRAND);
        Assertions.assertEquals(carViewServiceModel.getModel(), this.MODEL);
        Assertions.assertEquals(carViewServiceModel.getDescription(), this.DESCRIPTION);
        Assertions.assertEquals(carViewServiceModel.getYear(), this.YEAR);
        Assertions.assertEquals(carViewServiceModel.getImageUrl(), this.IMAGE_URL);
        Assertions.assertEquals(carViewServiceModel.getPrice(), this.PRICE);
        Assertions.assertEquals(carViewServiceModel.getOwnerId(), this.existingUserEntity.getId());

    }


    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testGetCarByIdThrowsException(){

        when(mockCarsRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () ->  carServiceToTest.getCarById(0L));
    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testUpdateCar() throws IOException {

        when(mockCarsRepository.saveAndFlush(any(CarEntity.class))).thenReturn(this.existingCarEntity);
        when(mockCarsRepository.findById(any(Long.class))).thenReturn(Optional.of(this.existingCarEntity));
        when(mockCloudinaryService.uploadImage(any(MultipartFile.class))).thenReturn(this.IMAGE_URL);

        this.carServiceToTest.updateCar(this.carAddServiceModel,
                1L,
                this.imageAddServiceModel);

        ArgumentCaptor<CarEntity> argument = ArgumentCaptor.forClass(CarEntity.class);
        Mockito.verify(mockCarsRepository, times(1)).saveAndFlush(argument.capture());
        CarEntity carEntityActual = argument.getValue();

        Assertions.assertEquals(carEntityActual.getBrand(), carAddServiceModel.getBrand());
        Assertions.assertEquals(carEntityActual.getModel(), carAddServiceModel.getModel());
        Assertions.assertEquals(carEntityActual.getDescription(), carAddServiceModel.getDescription());
        Assertions.assertEquals(carEntityActual.getYear(), carAddServiceModel.getYear());
        Assertions.assertEquals(carEntityActual.getImageUrl(), this.IMAGE_URL);
        Assertions.assertEquals(carEntityActual.getPrice(), carAddServiceModel.getPrice());
        Assertions.assertEquals(carEntityActual.getOwner().getId(), this.existingUserEntity.getId());

    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testUpdateCarReturnCarViewServiceModel() throws IOException {

        when(mockCarsRepository.saveAndFlush(any(CarEntity.class))).thenReturn(this.existingCarEntity);
        when(mockCarsRepository.findById(any(Long.class))).thenReturn(Optional.of(this.existingCarEntity));
        when(mockCloudinaryService.uploadImage(any(MultipartFile.class))).thenReturn(this.IMAGE_URL);

        CarViewServiceModel carViewServiceModel = this.carServiceToTest.updateCar(this.carAddServiceModel,
                1L,
                this.imageAddServiceModel);

        Assertions.assertEquals(carViewServiceModel.getId(), this.CAR_ID);
        Assertions.assertEquals(carViewServiceModel.getBrand(), carAddServiceModel.getBrand());
        Assertions.assertEquals(carViewServiceModel.getModel(), carAddServiceModel.getModel());
        Assertions.assertEquals(carViewServiceModel.getDescription(), carAddServiceModel.getDescription());
        Assertions.assertEquals(carViewServiceModel.getYear(), carAddServiceModel.getYear());
        Assertions.assertEquals(carViewServiceModel.getImageUrl(), this.IMAGE_URL);
        Assertions.assertEquals(carViewServiceModel.getPrice(), carAddServiceModel.getPrice());
        Assertions.assertEquals(carViewServiceModel.getOwnerId(), this.existingUserEntity.getId());

    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testUpdateCarThrowsException(){

        when(mockCarsRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () ->  carServiceToTest.updateCar(this.carAddServiceModel, 0L, this.imageAddServiceModel));
    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testDeleteCarReturnCarViewServiceModel() {

        when(mockCarsRepository.findById(any(Long.class))).thenReturn(Optional.of(this.existingCarEntity));


        CarViewServiceModel carViewServiceModel = this.carServiceToTest.deleteCar(1L);

        Assertions.assertEquals(carViewServiceModel.getBrand(), carAddServiceModel.getBrand());
        Assertions.assertEquals(carViewServiceModel.getModel(), carAddServiceModel.getModel());
        Assertions.assertEquals(carViewServiceModel.getDescription(), carAddServiceModel.getDescription());
        Assertions.assertEquals(carViewServiceModel.getYear(), carAddServiceModel.getYear());
        Assertions.assertEquals(carViewServiceModel.getImageUrl(), this.IMAGE_URL);
        Assertions.assertEquals(carViewServiceModel.getPrice(), carAddServiceModel.getPrice());
        Assertions.assertEquals(carViewServiceModel.getOwnerId(), this.existingUserEntity.getId());

    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testDeleteCarThrowsException(){

        when(mockCarsRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () ->  carServiceToTest.deleteCar(0L));
    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testGetAllCars() {

        when(mockCarsRepository.findAll(any(Pageable.class))).thenReturn(this.carEntityPage);


        Page<CarViewServiceModel> carViewServiceModelPage = this.carServiceToTest
                .getAllCars(this.pageable);

        Assertions.assertEquals(carViewServiceModelPage.getTotalElements(), 1L);
        Assertions.assertEquals(carViewServiceModelPage.getTotalPages(), 1);

    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testGetAllCarsByOwnerId() {

        when(mockCarsRepository.findAllByOwnerId(any(Long.class), any(Pageable.class))).thenReturn(this.createCarEntityPage());


        Page<CarViewServiceModel> carViewServiceModelPage = this.carServiceToTest
                .getAllCarsByOwnerId(1L, this.pageable);

        Assertions.assertEquals(carViewServiceModelPage.getTotalElements(), 1L);
        Assertions.assertEquals(carViewServiceModelPage.getTotalPages(), 1);

    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testSearch() {

        when(mockCarsRepository.search(any(String.class), any(Pageable.class))).thenReturn(this.createCarEntityPage());


        Page<CarViewServiceModel> carViewServiceModelPage = this.carServiceToTest
                .search("search", this.pageable);

        Assertions.assertEquals(carViewServiceModelPage.getTotalElements(), 1L);
        Assertions.assertEquals(carViewServiceModelPage.getTotalPages(), 1);

    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testGetCarsRepositoryCount() {

        when(mockCarsRepository.count()).thenReturn(1L);

        Long carsRepositoryCount = this.carServiceToTest.getCarsRepositoryCount();

        Assertions.assertEquals(carsRepositoryCount, 1L);
    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testGetCarsRepositoryCountByOwnerId() {

        when(mockCarsRepository.getCarsCountByOwnerId(1L)).thenReturn(1L);

        Long carsRepositoryCountByOwnerId = this.carServiceToTest.getCarsRepositoryCountByOwnerId(1L);

        Assertions.assertEquals(carsRepositoryCountByOwnerId, 1L);
    }

    @Test
    @MockitoSettings(strictness = Strictness.WARN)
    public void testGetCarsRepositoryCountByKeyword() {

        when(mockCarsRepository.searchResultCount("search")).thenReturn(1L);

        Long carsRepositoryCountByKeyword = this.carServiceToTest.getCarsRepositoryCountByKeyword("search");

        Assertions.assertEquals(carsRepositoryCountByKeyword, 1L);
    }



    private Page<CarEntity> createCarEntityPage() {
        Page<CarEntity> carEntityPage = new PageImpl<>(List.of(existingCarEntity),
                this.pageable, 1L);
        return carEntityPage;
    }

    private Pageable createPageable() {

        return PageRequest.of(this.PAGE, this.PAGE_SIZE);
    }

    private CarEntity createExistingCarEntity() {

        CarEntity existingCarEntity = this.carEntity;
        existingCarEntity.setId(this.CAR_ID);
        existingCarEntity.setOwner(this.existingUserEntity);

        return existingCarEntity;
    }

    private ImageAddServiceModel createImageAddServiceModel() {

        ImageAddServiceModel imageAddServiceModel = new ImageAddServiceModel();
        MultipartFile multipartFile = this.createMultipartFile();
        imageAddServiceModel.setImageUrl(multipartFile);
        return imageAddServiceModel;
    }

    private CarAddServiceModel createCarAddServiceModel() {

        CarAddServiceModel carAddServiceModel = new CarAddServiceModel();
        carAddServiceModel.setBrand(this.BRAND);
        carAddServiceModel.setModel(this.MODEL);
        carAddServiceModel.setDescription(this.DESCRIPTION);
        carAddServiceModel.setYear(this.YEAR);
        carAddServiceModel.setPrice(this.PRICE);
        return carAddServiceModel;
    }

    private CarEntity createCarEntity() {

        CarEntity carEntity = new CarEntity();
        carEntity.setBrand(this.BRAND);
        carEntity.setModel(this.MODEL);
        carEntity.setDescription(this.DESCRIPTION);
        carEntity.setImageUrl(this.IMAGE_URL);
        carEntity.setYear(this.YEAR);
        carEntity.setPrice(this.PRICE);

        return carEntity;
    }

    private UserEntity createExistingUserEntity() {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(this.USER_ID);
        userEntity.setUsername(this.USERNAME);
        userEntity.setPassword(this.PASSWORD);
        userEntity.setEmail(this.EMAIL);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(RoleName.ROLE_USER);
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(roleEntity);
        userEntity.setRoles(roles);

        return userEntity;
    }

    private MockMultipartFile createMultipartFile(){
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );
        return file;
    }


}
