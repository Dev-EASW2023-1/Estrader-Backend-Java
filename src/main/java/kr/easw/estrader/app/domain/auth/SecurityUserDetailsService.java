package kr.easw.estrader.app.domain.auth;

import kr.easw.estrader.app.domain.model.entity.UserEntity;
import kr.easw.estrader.app.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SecurityUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final SupportAuthorityUtils supportAuthorityUtils;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("not found loginId : " + userId));

        return new SecurityUser(userEntity, supportAuthorityUtils);
    }
}
