package com.acme.backendfreshsense.inventory.application.service;

import com.acme.backendfreshsense.inventory.domain.model.entities.CatalogItem;
import com.acme.backendfreshsense.inventory.domain.repository.CatalogItemRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Siembra el catálogo general de alimentos. Es "top-up": agrega por nombre los
 * que falten sin duplicar, así producción se completa sola al desplegar.
 *
 * <p>Las categorías son SIEMPRE las 7 de umbrales del Edge — un ítem con otra
 * categoría no tendría semáforo de frescura.</p>
 */
@Component
@RequiredArgsConstructor
public class CatalogDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CatalogDataInitializer.class);

    private final CatalogItemRepository catalogRepository;

    @Override
    public void run(String... args) {
        int added = 0;
        for (CatalogItem item : catalog()) {
            if (!catalogRepository.existsByName(item.getName())) {
                catalogRepository.save(item);
                added++;
            }
        }
        log.info("Catálogo de alimentos: {} nuevos sembrados ({} en el catálogo).", added, catalog().size());
    }

    private static String img(long pexelsId) {
        return "https://images.pexels.com/photos/" + pexelsId + "/pexels-photo-" + pexelsId
                + ".jpeg?auto=compress&cs=tinysrgb&w=640";
    }

    private static CatalogItem item(String name, String description, String category, long imageId) {
        return CatalogItem.builder()
                .name(name).description(description).category(category).imageUrl(img(imageId))
                .build();
    }

    private List<CatalogItem> catalog() {
        return List.of(
            // ---------- Frutas ----------
            item("Manzana", "Fruta fresca de estación, dulce y crujiente", "Frutas", 1510392),
            item("Plátano", "Plátano de seda maduro", "Frutas", 1093038),
            item("Naranja", "Cítrico jugoso, ideal para jugos", "Frutas", 616404),
            item("Mandarina", "Cítrico dulce fácil de pelar", "Frutas", 4033329),
            item("Papaya", "Fruta tropical rica en fibra", "Frutas", 5945848),
            item("Piña", "Fruta tropical ácida y refrescante", "Frutas", 1132047),
            item("Mango", "Mango maduro de estación", "Frutas", 2294471),
            item("Uvas", "Racimo de uvas frescas", "Frutas", 691166),
            item("Fresas", "Fresas frescas para postres y batidos", "Frutas", 708777),
            item("Palta", "Palta cremosa para untar o ensaladas", "Frutas", 63680),
            item("Limón", "Limón para aliños y bebidas", "Frutas", 1435904),
            item("Sandía", "Fruta hidratante de verano", "Frutas", 1435907),

            // ---------- Verduras ----------
            item("Zanahoria", "Zanahorias medianas frescas", "Verduras", 143133),
            item("Espinacas", "Hojas de espinaca baby", "Verduras", 2325843),
            item("Lechuga", "Lechuga fresca para ensaladas", "Verduras", 606540),
            item("Tomate", "Tomates rojos maduros", "Verduras", 533280),
            item("Cebolla", "Cebolla roja para aderezos", "Verduras", 461208),
            item("Papa", "Papa amarilla o blanca", "Verduras", 2286776),
            item("Brócoli", "Brócoli fresco en cabezuelas", "Verduras", 372851),
            item("Zapallo", "Zapallo para cremas y guisos", "Verduras", 236805),
            item("Pepino", "Pepino fresco para ensaladas", "Verduras", 2329440),
            item("Pimiento", "Pimiento rojo o verde", "Verduras", 128420),
            item("Culantro", "Hierba aromática para guisos", "Verduras", 3872406),
            item("Apio", "Tallos de apio frescos", "Verduras", 3735153),

            // ---------- Lácteos ----------
            item("Leche entera", "Leche fresca de vaca", "Lácteos", 236010),
            item("Leche evaporada", "Leche evaporada para cocina y postres", "Lácteos", 2255935),
            item("Yogur natural", "Yogur natural sin azúcar", "Lácteos", 704971),
            item("Yogur griego", "Yogur griego denso y cremoso", "Lácteos", 1854037),
            item("Queso fresco", "Queso fresco serrano", "Lácteos", 821365),
            item("Queso parmesano", "Queso curado para rallar", "Lácteos", 942805),
            item("Mantequilla", "Mantequilla con o sin sal", "Lácteos", 858496),
            item("Crema de leche", "Crema para salsas y postres", "Lácteos", 718742),

            // ---------- Carnes ----------
            item("Pechuga de pollo", "Pechuga sin hueso ni piel", "Carnes", 4110375),
            item("Carne molida de res", "Molida especial para guisos", "Carnes", 618775),
            item("Lomo de res", "Corte tierno para saltados", "Carnes", 1583884),
            item("Chuleta de cerdo", "Chuleta fresca de cerdo", "Carnes", 8477552),
            item("Pescado fresco", "Filete de pescado del día", "Carnes", 65175),
            item("Pavo", "Pechuga o filete de pavo", "Carnes", 376370),
            item("Hígado de res", "Hígado fresco, rico en hierro", "Carnes", 699953),

            // ---------- Proteínas ----------
            item("Huevos", "Huevos de granja", "Proteínas", 162712),
            item("Frijoles", "Frijoles canario o negro", "Proteínas", 4198935),
            item("Lentejas", "Lentejas secas para guisos", "Proteínas", 3756523),
            item("Garbanzos", "Garbanzos secos o cocidos", "Proteínas", 5737247),
            item("Quinua", "Grano andino rico en proteína", "Proteínas", 5945568),
            item("Atún en lata", "Filete de atún en agua o aceite", "Proteínas", 2097090),
            item("Tofu", "Proteína vegetal de soya", "Proteínas", 4051569),

            // ---------- Panadería ----------
            item("Pan integral", "Pan de molde integral", "Panadería", 1775043),
            item("Pan francés", "Pan crocante del día", "Panadería", 209194),
            item("Pan de molde", "Pan de molde blanco", "Panadería", 376464),
            item("Tortillas", "Tortillas de trigo o maíz", "Panadería", 70497),
            item("Queque casero", "Queque o bizcocho casero", "Panadería", 2133989),
            item("Galletas de avena", "Galletas caseras de avena", "Panadería", 2792186),

            // ---------- Snacks ----------
            item("Granola", "Mezcla de avena, miel y frutos secos", "Snacks", 1092730),
            item("Frutos secos", "Mix de nueces, almendras y pecanas", "Snacks", 86649),
            item("Maní", "Maní tostado con o sin sal", "Snacks", 1295572),
            item("Pasas", "Uvas pasas para repostería o snack", "Snacks", 3026808),
            item("Barras de cereal", "Barras energéticas de cereal", "Snacks", 2313686),
            item("Cancha serrana", "Maíz tostado andino", "Snacks", 1998920),
            item("Chifles", "Láminas fritas de plátano", "Snacks", 2456435)
        );
    }
}
