import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthorizationRequestPojo {
    private String email;
    private String password;

    public static UserAuthorizationRequestPojo from(UserCreationRequestPojo userCreationRequestPojo) {
        return new UserAuthorizationRequestPojo(userCreationRequestPojo.getEmail(), userCreationRequestPojo.getPassword());
    }
}
