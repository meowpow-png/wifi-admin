package hr.ht.rnd.wifiadmin.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for Wi-Fi configurations.
 */
interface WifiConfigurationJpaRepository extends JpaRepository<WifiConfigurationEntity, String> {}
