package cars.entities.cars;

import cars.cloudinary.CloudinaryService;
import cars.constants.GlobalConstants;
import cars.entities.cars.model.CarAddBindingModel;
import cars.entities.cars.model.CarEntity;
import cars.entities.cars.repository.CarsRepository;
import cars.entities.roles.RoleRepository;
import cars.entities.roles.model.RoleEntity;
import cars.entities.roles.model.RoleName;
import cars.entities.users.model.UserEntity;
import cars.entities.users.model.UserLoginBindingModel;
import cars.entities.users.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.math.BigDecimal;
import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CarsControllerIntegrationTests {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final String BRAND = "Audi";
    private final String MODEL = "A3";
    private final String DESCRIPTION = "description";
    private final int YEAR = 2020;
    private final String IMAGE_URL = "http://res.cloudinary.com/ipanchev/image/upload/v1616226988/id08tagytlyglzz84nyb.jpg";
    private final BigDecimal PRICE = new BigDecimal("10000.0");

    private final String BRAND_CREATE = "VW";
    private final String MODEL_CREATE = "GOLF";
    private final String DESCRIPTION_CREATE = "description";
    private final int YEAR_CREATE = 2021;
    private final BigDecimal PRICE_CREATE = new BigDecimal("11111");

    private final String USERNAME = "username";
    private final String EXISTING_USERNAME = "existing_username";
    private final String PASSWORD = "password";
    private final String EMAIL = "email@abv.bg";
    private final String EXISTING_EMAIL = "existing_email@abv.bg";
    private final String ROLE = "ROLE_USER";

    private CarAddBindingModel carAddBindingModel;
    private UserLoginBindingModel userLoginBindingModel;
    private String accessToken;


    @MockBean
    private CloudinaryService mockCloudinaryService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CarsRepository carsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @BeforeAll
    public  void setUpDatabase() throws Exception {
        this.userRepository.deleteAll();
        this.initUserRepository();
        this.carsRepository.deleteAll();
        this.initCarsRepository();
        this.userLoginBindingModel = this.createUserLoginBindingModel();
        this.accessToken = getAccessToken();
    }



    @BeforeEach
    public void setUp() {

        this.carAddBindingModel = this.createCarAddBindingModel();

    }


    @Test
    @Order(1)
    public void testGetAllCarsWithoutCriteria() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cars")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].model").value(this.MODEL))
                .andExpect(jsonPath("$[0].brand").value(this.BRAND))
                .andExpect(jsonPath("$[0].description").value(this.DESCRIPTION))
                .andExpect(jsonPath("$[0].year").value(this.YEAR))
                .andExpect(jsonPath("$[0].imageUrl").value(this.IMAGE_URL))
                .andExpect(jsonPath("$[0].price").value(this.PRICE))
                .andExpect(jsonPath("$[0].ownerId").value(1L));
    }

    @Test
    @Order(2)
    public void testGetAllCarsByOwnerId() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cars")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .param("ownerId", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].model").value(this.MODEL))
                .andExpect(jsonPath("$[0].brand").value(this.BRAND))
                .andExpect(jsonPath("$[0].description").value(this.DESCRIPTION))
                .andExpect(jsonPath("$[0].year").value(this.YEAR))
                .andExpect(jsonPath("$[0].imageUrl").value(this.IMAGE_URL))
                .andExpect(jsonPath("$[0].price").value(this.PRICE))
                .andExpect(jsonPath("$[0].ownerId").value(1L));
    }

    @Test
    @Order(3)
    public void testGetAllCarsByOwnerIdReturnEmptyArrayWhenNoSuchOwner() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/cars")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .param("ownerId", String.valueOf(10L)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
               .andReturn();

        String content = result.getResponse().getContentAsString();
       Assertions.assertEquals("[]", content);


    }

    @Test
    @Order(4)
    public void testGetAllCarsByKeyword() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cars")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .param("keyword", "Audi"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].model").value(this.MODEL))
                .andExpect(jsonPath("$[0].brand").value(this.BRAND))
                .andExpect(jsonPath("$[0].description").value(this.DESCRIPTION))
                .andExpect(jsonPath("$[0].year").value(this.YEAR))
                .andExpect(jsonPath("$[0].imageUrl").value(this.IMAGE_URL))
                .andExpect(jsonPath("$[0].price").value(this.PRICE))
                .andExpect(jsonPath("$[0].ownerId").value(1L));
    }

    @Test
    @Order(5)
    public void testGetAllCarsByKeywordReturnEmptyArrayWhenNoMatch() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/cars")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .param("keyword", "Lada"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Assertions.assertEquals("[]", content);


    }

    @Test
    @Order(6)
    public void testCreateCar() throws Exception {

//        when(this.mockCloudinaryService.uploadImage(any(MultipartFile.class))).thenReturn(IMAGE_URL);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cars")
                .flashAttr("carAddBindingModel", this.carAddBindingModel)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.brand").value(this.BRAND_CREATE))
                .andExpect(jsonPath("$.model").value(this.MODEL_CREATE))
                .andExpect(jsonPath("$.description").value(this.DESCRIPTION_CREATE))
                .andExpect(jsonPath("$.year").value(this.YEAR_CREATE))
                .andExpect(jsonPath("$.price").value(this.PRICE_CREATE));

    }

    @Test
    @Order(7)
    public void testCreateCarThrowsMethodArgumentNotValidException() throws Exception {


        this.carAddBindingModel.setBrand("");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cars")
                .flashAttr("carAddBindingModel", this.carAddBindingModel)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Order(8)
    public void testCreateCarWithoutAuthorizationHeader() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cars")
                .flashAttr("carAddBindingModel", this.carAddBindingModel))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @Order(9)
    public void testCreateCarWithoutJwtToken() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cars")
                .flashAttr("carAddBindingModel", this.carAddBindingModel)
                .header(HttpHeaders.AUTHORIZATION, ""))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @Order(10)
    public void testCreateCarWithInvalidJwtToken() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cars")
                .flashAttr("carAddBindingModel", this.carAddBindingModel)
                .header(HttpHeaders.AUTHORIZATION, "eyJhbGciOiJIUzUxMiJ9.invalidiOiJpcGFuY2hldiIsImlhdCI6MTYzMTYxMjY4NiwiZXhwIjoxNjMxNjk5MDg2fQ.f0nxI_6QwNt90h3gVLfX0_W4ib2OR0CuZiJJzw0dGAt-7PdWChTPc2LJjQMhnkTWV1XYyHLltRddsr3iAj26LA"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @Order(11)
    public void testGetCar() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.brand").value(this.BRAND))
                .andExpect(jsonPath("$.model").value(this.MODEL))
                .andExpect(jsonPath("$.description").value(this.DESCRIPTION))
                .andExpect(jsonPath("$.year").value(this.YEAR))
                .andExpect(jsonPath("$.price").value(this.PRICE));

    }

    @Test
    @Order(12)
    public void testGetCarThrowsEntityNotFoundException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/20")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Not found car with id: 20"));

    }

    @Test
    @Order(13)
    public void testUpdateCar() throws Exception {

//        when(this.mockCloudinaryService.uploadImage(any(MultipartFile.class))).thenReturn(IMAGE_URL);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/cars/1")
                .flashAttr("carAddBindingModel", this.carAddBindingModel)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken))
                .andExpect(status().isOk());

    }

    @Test
    @Order(14)
    public void testUpdateCarThrowsMethodArgumentNotValidException() throws Exception {

        this.carAddBindingModel.setBrand("");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/cars/1")
                .flashAttr("carAddBindingModel", this.carAddBindingModel)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Order(15)
    public void testUpdateCarThrowsEntityNotFoundException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/api/cars/20")
                .flashAttr("carAddBindingModel", this.carAddBindingModel)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("Not found car with id: 20"));

    }


    @Test
    @Order(16)
    public void testGetCarsCountByOwnerId() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/count")
                .param("ownerId", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Long carsCount = Long.parseLong(content);

        Assertions.assertEquals(2L, carsCount);

    }

    @Test
    @Order(17)
    public void testGetCarsCountByOwnerIdReturnZeroWhenNoSuchOwner() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/count")
                .param("ownerId", String.valueOf(10L)))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertEquals("0", content);

    }

    @Test
    @Order(18)
    public void testGetCarsCountByKeyword() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/count")
                .param("keyword", "VW"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Long carsCount = Long.parseLong(content);

        Assertions.assertEquals(2L, carsCount);

    }

    @Test
    @Order(19)
    public void testGetCarsCountByKeywordReturnZeroWhenNoMatch() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/count")
                .param("keyword", "Lada"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        Assertions.assertEquals("0", content);

    }

    @Test
    @Order(20)
    public void testGetCarsCountWithoutCriteria() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/cars/count"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Long carsCount = Long.parseLong(content);

        Assertions.assertEquals(2L, carsCount);

    }

    @Test
    @Order(21)
    public void testDeleteCar() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cars/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken))
                .andExpect(status().isOk());

    }

    @Test
    @Order(22)
    public void testDeleteCarThrowsEntityNotFoundException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cars/20")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Not found car with id: 20"));

    }


    private String getAccessToken() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/users/login")
                .content(objectMapper.writeValueAsString(this.userLoginBindingModel))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(content);
        String accessToken = jsonObject.getString("accessToken");

        return accessToken;
    }

    private CarAddBindingModel createCarAddBindingModel() {

        CarAddBindingModel carAddBindingModel = new CarAddBindingModel();
        carAddBindingModel.setBrand(this.BRAND_CREATE);
        carAddBindingModel.setModel(this.MODEL_CREATE);
        carAddBindingModel.setDescription(this.DESCRIPTION_CREATE);
        carAddBindingModel.setYear(this.YEAR_CREATE);
        carAddBindingModel.setPrice(this.PRICE_CREATE);

        return carAddBindingModel;
    }

    private UserLoginBindingModel createUserLoginBindingModel() {

        UserLoginBindingModel userLoginBindingModel = new UserLoginBindingModel();
        userLoginBindingModel.setUsername(this.EXISTING_USERNAME);
        userLoginBindingModel.setPassword(this.PASSWORD);

        return  userLoginBindingModel;
    }

    private MockMultipartFile createMultipartFile() {
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );
        return file;
    }

    private void initUserRepository() {

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(this.EXISTING_USERNAME);
        userEntity.setPassword(this.passwordEncoder.encode(this.PASSWORD));
        userEntity.setEmail(this.EXISTING_EMAIL);

        RoleEntity userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException(GlobalConstants.ROLE_NOT_FOUND + RoleName.ROLE_USER));
        userEntity.getRoles().add(userRole);

        this.userRepository.saveAndFlush(userEntity);
    }

    private void initCarsRepository() {

        CarEntity carEntity = new CarEntity();
        carEntity.setBrand(this.BRAND);
        carEntity.setModel(this.MODEL);
        carEntity.setDescription(this.DESCRIPTION);
        carEntity.setYear(this.YEAR);
        carEntity.setImageUrl(this.IMAGE_URL);
        carEntity.setPrice(this.PRICE);
        carEntity.setOwner(this.userRepository.getById(1L));

        this.carsRepository.saveAndFlush(carEntity);
    }

}
