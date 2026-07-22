package com.proyecto.servicio.empresa.config;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
@Slf4j
@Configuration
public class FlywayConfig {
    @Value("${flyway.erp.locations}")
    private String erpLocation;

    @Value("${flyway.erp.history-table}")
    private String erpHistoryTable;

    @Value("${flyway.erp.schema-name}")
    private String erpSchemaName;

    @Value("${flyway.erp.enabled}")
    private boolean erpEnabled;

    @Bean
    public Flyway erpFlyway(@Qualifier("sfDataSource") DataSource dataSource) {

        return createFlyway(
                dataSource,
                erpLocation,
                erpHistoryTable,
                erpSchemaName,
                erpEnabled
        );
    }

    private Flyway createFlyway(
            DataSource dataSource,
            String location,
            String historyTable,
            String schemaName,
            boolean executeMigration
    ) {

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations(location)
                .table(historyTable)
                .schemas(schemaName)
                .defaultSchema(schemaName)
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .validateOnMigrate(true)
                .outOfOrder(true)
                .load();

        if (executeMigration) {
            flyway.migrate();
        }

        logPendingMigrations(flyway, schemaName);

        return flyway;
    }

    private void logPendingMigrations(Flyway flyway, String schema) {
        try {

            MigrationInfo[] pending = flyway.info().pending();

            if (pending.length == 0) {

                log.info("[Flyway-{}] No hay migraciones pendientes.", schema);

            } else {

                log.info("[Flyway-{}] Migraciones pendientes: {}", schema, pending.length);

                for (MigrationInfo info : pending) {
                    log.info(
                            "[Flyway-{}] -> {} | {}",
                            schema,
                            info.getVersion(),
                            info.getDescription()
                    );
                }
            }

        } catch (Exception e) {

            log.warn(
                    "[Flyway-{}] No se pudo consultar el estado de migraciones: {}",
                    schema,
                    e.getMessage()
            );
        }
    }
}
