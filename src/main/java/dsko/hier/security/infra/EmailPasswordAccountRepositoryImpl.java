package dsko.hier.security.infra;

import dsko.hier.security.domain.EmailPasswordAccount;
import dsko.hier.security.domain.EmailPasswordAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmailPasswordAccountRepositoryImpl implements EmailPasswordAccountRepository {

    private final EmailPasswordAccountJpaRepository emailPasswordAccountJpaRepository;

    @Override
    public EmailPasswordAccount save(EmailPasswordAccount account) {
        return emailPasswordAccountJpaRepository.save(account);
    }
}
