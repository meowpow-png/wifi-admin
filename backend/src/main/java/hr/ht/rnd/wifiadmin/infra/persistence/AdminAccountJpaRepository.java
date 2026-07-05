package hr.ht.rnd.wifiadmin.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring data repository for administrator accounts.
 */
interface AdminAccountJpaRepository extends JpaRepository<AdminAccountEntity, String> {}
