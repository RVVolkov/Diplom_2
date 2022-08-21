import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationRequestPojo {
    private String email;
    private String password;
    private String name;

    public UserCreationRequestPojo(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public static UserCreationRequestPojo getRandomUser() {
        Faker faker = new Faker();
        final String email = faker.internet().emailAddress();
        final String password = faker.internet().password(6, 9);
        final String name = faker.name().firstName();
        return new UserCreationRequestPojo(email, password, name);
    }
}
