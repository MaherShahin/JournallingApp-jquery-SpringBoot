//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Rollback(value = false)
////@SpringBootTest(classes = UserRepository.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ContextConfiguration(classes = BrueckePortalApplication.class)
//public class UserRepositoryTests {
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Autowired
//    private UserRepository repo;
//
//
//    @Test
//    public void testCreateUser(){
//        User user = new User();
//        user.setEmail("mahershahin2@gmail.com");
//        user.setUsername("maher2");
//        user.setPassword("ravi2020");
//        user.setName("Adsa Kumar");
//        user.setNationality("EG");
//        user.setPhone("123456789");
//        user.setSex("M");
//
//
//        User savedUser = repo.save(user);
//
//        User existUser = entityManager.find(User.class, savedUser.getID());
//
//        assert (user.getEmail()).equals(existUser.getEmail());
//    }
//}
