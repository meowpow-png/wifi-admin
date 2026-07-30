package hr.ht.rnd.wifiadmin.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

/**
 * Spring data repository for Wi-Fi configurations.
 */
interface WifiConfigurationJpaRepository extends JpaRepository<WifiConfigurationEntity, String> {

    @Modifying
    @Query("""
            delete from WifiConfigurationEntity c
            where c.lastSynchronized < :lastSynchronized
            """)
    void deleteOlderThan(LocalDate lastSynchronized);
}
