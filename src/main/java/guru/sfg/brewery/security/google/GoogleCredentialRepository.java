package guru.sfg.brewery.security.google;

import com.warrenstrange.googleauth.ICredentialRepository;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class GoogleCredentialRepository implements ICredentialRepository {
    private final UserRepository userRepository;

    @Override
    public String getSecretKey(String userName) {
        User user = userRepository.findByUsername(userName).orElseThrow();
        return user.getGoogle2fSecret();
    }

    @Override
    public void saveUserCredentials(String userName, String secretKey, int i, List<Integer> list) {
        User user = userRepository.findByUsername(userName).orElseThrow();
        user.setGoogle2fSecret(secretKey);
        user.setUseGoogle2fa(true);
        userRepository.save(user);
    }
}
