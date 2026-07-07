package com.acme.backendfreshsense.shared.infrastructure.init;

import com.acme.backendfreshsense.accounts.domain.model.Role;
import com.acme.backendfreshsense.accounts.infrastructure.persistence.jpa.UserJpaRepository;
import com.acme.backendfreshsense.accounts.infrastructure.persistence.jpa.UserEntity;
import com.acme.backendfreshsense.inventory.infrastructure.persistence.jpa.ProductEntity;
import com.acme.backendfreshsense.inventory.infrastructure.persistence.jpa.ProductJpaRepository;
import com.acme.backendfreshsense.reports.infrastructure.persistence.HistoryJpaEntity;
import com.acme.backendfreshsense.reports.infrastructure.persistence.HistoryJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Crea un usuario demo con datos de prueba al arrancar si no existe.
 * Credenciales: demo@freshsense.com / Demo1234!
 *
 * <p>Controlado por la propiedad {@code freshsense.demo-seed.enabled} (activo por defecto).
 * En un entorno productivo real, poner {@code freshsense.demo-seed.enabled=false}
 * para no crear una cuenta con credenciales conocidas.</p>
 */
@Component
@ConditionalOnProperty(name = "freshsense.demo-seed.enabled", havingValue = "true", matchIfMissing = true)
public class DemoDataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataInitializer.class);

    private static final String DEMO_EMAIL = "demo@freshsense.com";

    private final UserJpaRepository userRepo;
    private final ProductJpaRepository productRepo;
    private final HistoryJpaRepository historyRepo;
    private final PasswordEncoder passwordEncoder;

    public DemoDataInitializer(UserJpaRepository userRepo,
                               ProductJpaRepository productRepo,
                               HistoryJpaRepository historyRepo,
                               PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.historyRepo = historyRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        boolean userExists = userRepo.findByEmail(DEMO_EMAIL).isPresent();

        if (!userExists) {
            log.info("Creating demo user...");
            UserEntity demo = new UserEntity(DEMO_EMAIL, passwordEncoder.encode("Demo1234!"), "Demo FreshSense", Role.USER_STANDARD);
            userRepo.save(demo);
        }

        Long userId = userRepo.findByEmail(DEMO_EMAIL).get().getId();

        // If the user already has products, skip seeding
        long existingProducts = productRepo.findByUserId(userId).size();
        if (existingProducts > 0) {
            log.info("Demo user already has {} products — skipping seed.", existingProducts);
            return;
        }

        log.info("Seeding demo data for user id={}...", userId);

        // --- Productos ---
        ProductEntity leche    = productRepo.save(ProductEntity.builder().userId(userId).name("Leche entera").description("Leche fresca de vaca").category("Lácteos").quantity(2).imageUrl("https://images.pexels.com/photos/236010/pexels-photo-236010.jpeg?auto=compress&cs=tinysrgb&w=400").build());
        ProductEntity manzanas = productRepo.save(ProductEntity.builder().userId(userId).name("Manzanas Fuji").description("Manzanas dulces y crujientes").category("Frutas").quantity(6).imageUrl("https://images.pexels.com/photos/1510392/pexels-photo-1510392.jpeg?auto=compress&cs=tinysrgb&w=400").build());
        ProductEntity pollo    = productRepo.save(ProductEntity.builder().userId(userId).name("Pechuga de pollo").description("Pechuga sin hueso").category("Carnes").quantity(1).imageUrl("https://images.pexels.com/photos/4110375/pexels-photo-4110375.jpeg?auto=compress&cs=tinysrgb&w=400").build());
        ProductEntity yogur    = productRepo.save(ProductEntity.builder().userId(userId).name("Yogur griego").description("Yogur natural sin azúcar").category("Lácteos").quantity(3).imageUrl("https://images.pexels.com/photos/704971/pexels-photo-704971.jpeg?auto=compress&cs=tinysrgb&w=400").build());
        ProductEntity espinaca = productRepo.save(ProductEntity.builder().userId(userId).name("Espinacas").description("Espinacas baby frescas").category("Verduras").quantity(1).imageUrl("https://images.pexels.com/photos/2325843/pexels-photo-2325843.jpeg?auto=compress&cs=tinysrgb&w=400").build());
        ProductEntity pan      = productRepo.save(ProductEntity.builder().userId(userId).name("Pan integral").description("Pan de molde integral").category("Panadería").quantity(1).imageUrl("https://images.pexels.com/photos/1775043/pexels-photo-1775043.jpeg?auto=compress&cs=tinysrgb&w=400").build());
        ProductEntity huevos   = productRepo.save(ProductEntity.builder().userId(userId).name("Huevos").description("Docena de huevos de granja").category("Proteínas").quantity(12).imageUrl("https://images.pexels.com/photos/162712/egg-white-food-protein-162712.jpeg?auto=compress&cs=tinysrgb&w=400").build());
        productRepo.save(ProductEntity.builder().userId(userId).name("Zanahorias").description("Zanahorias medianas").category("Verduras").quantity(4).imageUrl("https://images.pexels.com/photos/143133/pexels-photo-143133.jpeg?auto=compress&cs=tinysrgb&w=400").build());

        // --- Historial ---
        saveHistory(userId, leche.getId(),    "Leche entera",    "Lácteos",   "CONSUMED", 1, LocalDateTime.now().minusDays(6));
        saveHistory(userId, pollo.getId(),    "Pechuga de pollo","Carnes",    "CONSUMED", 1, LocalDateTime.now().minusDays(5));
        saveHistory(userId, pan.getId(),      "Pan integral",    "Panadería", "DISCARDED",1, LocalDateTime.now().minusDays(4));
        saveHistory(userId, manzanas.getId(), "Manzanas Fuji",   "Frutas",    "CONSUMED", 2, LocalDateTime.now().minusDays(3));
        saveHistory(userId, espinaca.getId(), "Espinacas",       "Verduras",  "CONSUMED", 1, LocalDateTime.now().minusDays(2));
        saveHistory(userId, yogur.getId(),    "Yogur griego",    "Lácteos",   "DISCARDED",1, LocalDateTime.now().minusDays(1));
        saveHistory(userId, huevos.getId(),   "Huevos",          "Proteínas", "CONSUMED", 4, LocalDateTime.now().minusHours(6));

        log.info("Demo data created successfully. Login: {} / Demo1234!", DEMO_EMAIL);
    }

    private void saveHistory(Long userId, Long productId, String productName, String category,
                             String action, int qty, LocalDateTime date) {
        HistoryJpaEntity h = new HistoryJpaEntity();
        h.setUserId(userId);
        h.setProductId(productId);
        h.setProductName(productName);
        h.setCategory(category);
        h.setAction(action);
        h.setQuantity(qty);
        h.setDate(date);
        historyRepo.save(h);
    }
}
