package com.acme.recipesservice.application.service;

import com.acme.recipesservice.domain.model.Recipe;
import com.acme.recipesservice.domain.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Siembra el catálogo canónico de recetas.
 *
 * <p>Es "top-up": agrega las recetas del catálogo que falten (comparando por título)
 * sin duplicar las existentes, y retira los títulos obsoletos. Así una BD ya poblada
 * (producción) se completa sola al desplegar, en vez de saltarse la siembra.</p>
 *
 * <p>Niveles alineados con los filtros del frontend (Easy/Intermediate/Advanced) y
 * con el endpoint premium ({@code Advanced} es premium). Tipos: Breakfast/Meals/Snacks.</p>
 */
@Component
@RequiredArgsConstructor
public class RecipeDataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(RecipeDataInitializer.class);

    /** Recetas de la siembra original en inglés, reemplazadas por el catálogo en español. */
    private static final Set<String> OBSOLETE_TITLES = Set.of(
            "Crispy Waffles with Berries",
            "Quinoa & Avocado Green Salad",
            "Banana & Spinach Smoothie"
    );

    private final RecipeRepository recipeRepository;

    @Override
    public void run(String... args) {
        List<Recipe> existing = recipeRepository.findAll();

        existing.stream()
                .filter(r -> OBSOLETE_TITLES.contains(r.getTitle()))
                .forEach(r -> {
                    recipeRepository.deleteById(r.getId());
                    log.info("Receta obsoleta retirada: {}", r.getTitle());
                });

        Set<String> titles = existing.stream().map(Recipe::getTitle).collect(Collectors.toSet());
        List<Recipe> catalog = catalog();
        int added = 0;
        for (Recipe recipe : catalog) {
            if (!titles.contains(recipe.getTitle())) {
                recipeRepository.save(recipe);
                added++;
            }
        }
        log.info("Catálogo de recetas: {} nuevas sembradas ({} en el catálogo).", added, catalog.size());
    }

    private static String img(long pexelsId) {
        return "https://images.pexels.com/photos/" + pexelsId + "/pexels-photo-" + pexelsId
                + ".jpeg?auto=compress&cs=tinysrgb&w=960&h=720&dpr=1";
    }

    private static Recipe r(String title, String description, long imageId, int rating,
                            String level, String type, String time,
                            List<String> ingredients, List<String> steps) {
        return Recipe.builder()
                .title(title).description(description).imageUrl(img(imageId)).rating(rating)
                .level(level).type(type).time(time)
                .ingredients(ingredients).steps(steps)
                .build();
    }

    private List<Recipe> catalog() {
        return List.of(
            // ---------- Desayunos (10) ----------
            r("Avena con manzana y canela",
              "Avena cremosa cocida en leche con manzana rallada y un toque de canela.",
              262959, 5, "Easy", "Breakfast", "10 min",
              List.of("1 taza de hojuelas de avena", "2 tazas de leche", "1 manzana", "Canela en polvo", "Miel al gusto"),
              List.of("Calienta la leche y agrega la avena.", "Cocina 5 minutos removiendo.",
                      "Ralla la manzana e incorpórala.", "Sirve con canela y miel.")),
            r("Huevos revueltos con espinacas",
              "Huevos cremosos con espinacas salteadas, listos en minutos.",
              6210747, 4, "Easy", "Breakfast", "10 min",
              List.of("3 huevos", "1 puñado de espinacas", "1 chorrito de leche", "Mantequilla", "Sal y pimienta"),
              List.of("Saltea las espinacas en mantequilla.", "Bate los huevos con la leche.",
                      "Vierte sobre las espinacas y revuelve a fuego bajo.", "Sirve al punto cremoso.")),
            r("Tostadas integrales con palta",
              "Pan integral tostado con palta cremosa, limón y ají al gusto.",
              1640777, 5, "Easy", "Breakfast", "8 min",
              List.of("2 rebanadas de pan integral", "1 palta madura", "Limón", "Sal", "Hojuelas de ají opcional"),
              List.of("Tuesta el pan integral.", "Machaca la palta con limón y sal.",
                      "Unta sobre las tostadas y sirve.")),
            r("Panqueques de plátano",
              "Panqueques esponjosos endulzados naturalmente con plátano maduro.",
              2673353, 4, "Intermediate", "Breakfast", "20 min",
              List.of("2 plátanos maduros", "2 huevos", "1 taza de harina", "1/2 taza de leche", "Polvo de hornear", "Miel"),
              List.of("Machaca los plátanos y mezcla con huevos y leche.", "Integra la harina y el polvo de hornear.",
                      "Cocina porciones en sartén hasta dorar por ambos lados.", "Sirve con miel.")),
            r("Yogur con granola y frutas",
              "Bowl fresco de yogur griego con granola crocante y fruta de estación.",
              1092730, 5, "Easy", "Breakfast", "5 min",
              List.of("1 taza de yogur griego", "1/2 taza de granola", "1 manzana en cubos", "Miel"),
              List.of("Sirve el yogur en un bowl.", "Agrega la granola y la manzana.", "Termina con un hilo de miel.")),
            r("Omelette de verduras",
              "Omelette dorado relleno de zanahoria, espinacas y queso.",
              4676410, 4, "Intermediate", "Breakfast", "15 min",
              List.of("3 huevos", "1 zanahoria rallada", "1 puñado de espinacas", "Queso rallado", "Sal y pimienta"),
              List.of("Bate los huevos y salpimienta.", "Saltea la zanahoria y las espinacas.",
                      "Vierte los huevos y cocina a fuego medio.", "Agrega el queso, dobla y sirve.")),
            r("Batido de manzana y avena",
              "Batido saciante de manzana, avena y leche, ideal para empezar el día.",
              4040705, 4, "Easy", "Breakfast", "5 min",
              List.of("1 manzana", "3 cucharadas de avena", "1 taza de leche", "Hielo", "Canela"),
              List.of("Lleva todo a la licuadora.", "Licúa hasta que quede homogéneo.", "Sirve frío con canela.")),
            r("Tortilla española de papas",
              "La clásica tortilla de papas: jugosa por dentro, dorada por fuera.",
              2116094, 5, "Advanced", "Breakfast", "40 min",
              List.of("5 huevos", "4 papas medianas", "1 cebolla", "Aceite de oliva", "Sal"),
              List.of("Fríe las papas y la cebolla en abundante aceite a fuego bajo.",
                      "Escurre y mezcla con los huevos batidos.", "Cuaja en sartén por un lado.",
                      "Voltea con ayuda de un plato y termina la cocción.", "Reposa antes de servir.")),
            r("Pan francés con canela",
              "Rebanadas de pan integral bañadas en huevo y leche, doradas en mantequilla.",
              376464, 4, "Intermediate", "Breakfast", "15 min",
              List.of("4 rebanadas de pan integral", "2 huevos", "1/2 taza de leche", "Canela", "Mantequilla", "Miel"),
              List.of("Bate los huevos con la leche y la canela.", "Remoja las rebanadas de pan.",
                      "Dora en mantequilla por ambos lados.", "Sirve con miel.")),
            r("Bowl de yogur griego con miel y nueces",
              "Yogur griego denso con nueces tostadas y miel de abeja.",
              1854037, 4, "Easy", "Breakfast", "5 min",
              List.of("1 taza de yogur griego", "Puñado de nueces", "Miel", "Canela"),
              List.of("Tuesta ligeramente las nueces.", "Sirve el yogur y corónalo con nueces, miel y canela.")),

            // ---------- Comidas (20) ----------
            r("Pollo al horno con zanahorias",
              "Pechuga de pollo jugosa horneada con zanahorias glaseadas.",
              2338407, 5, "Easy", "Meals", "45 min",
              List.of("2 pechugas de pollo", "4 zanahorias", "Aceite de oliva", "Romero", "Sal y pimienta"),
              List.of("Sazona el pollo y las zanahorias con aceite y romero.",
                      "Hornea a 200 °C por 35 minutos.", "Sirve con el jugo de la bandeja.")),
            r("Arroz con pollo",
              "El clásico arroz verde con presas de pollo, al estilo peruano.",
              2092906, 5, "Intermediate", "Meals", "60 min",
              List.of("4 presas de pollo", "2 tazas de arroz", "1 taza de culantro licuado", "Arvejas y zanahoria", "Ají amarillo", "Cerveza negra opcional"),
              List.of("Dora las presas y resérvalas.", "Sofríe ají y culantro licuado.",
                      "Agrega el arroz, el caldo y las verduras.", "Incorpora el pollo y cocina hasta secar.")),
            r("Crema de zanahoria",
              "Crema suave de zanahoria con un toque de jengibre y leche.",
              1279330, 4, "Easy", "Meals", "30 min",
              List.of("5 zanahorias", "1 papa", "1/2 taza de leche", "Jengibre", "Caldo de verduras"),
              List.of("Cocina las zanahorias y la papa en el caldo.", "Licúa con la leche y el jengibre.",
                      "Rectifica la sal y sirve caliente.")),
            r("Ensalada de espinacas y pollo",
              "Ensalada fresca con pollo grillado, espinacas y vinagreta de mostaza.",
              1640772, 4, "Easy", "Meals", "20 min",
              List.of("1 pechuga de pollo", "2 tazas de espinacas", "Tomates cherry", "Queso fresco", "Mostaza, aceite y limón"),
              List.of("Grilla el pollo y córtalo en tiras.", "Mezcla espinacas, tomates y queso.",
                      "Aliña con la vinagreta y corona con el pollo.")),
            r("Lomo saltado",
              "Salteado peruano de res con cebolla, tomate y papas fritas.",
              803963, 5, "Intermediate", "Meals", "30 min",
              List.of("400 g de lomo de res", "1 cebolla roja", "2 tomates", "Sillao", "Vinagre", "Papas fritas", "Arroz blanco"),
              List.of("Saltea la carne a fuego muy alto.", "Agrega cebolla, tomate, sillao y vinagre.",
                      "Incorpora las papas fritas.", "Sirve con arroz.")),
            r("Ají de gallina",
              "Cremoso guiso peruano de pollo deshilachado en salsa de ají amarillo.",
              1907244, 5, "Advanced", "Meals", "60 min",
              List.of("2 pechugas de pollo", "4 ajíes amarillos", "3 rebanadas de pan remojado en leche", "Leche", "Queso parmesano", "Nueces", "Arroz y papa sancochada"),
              List.of("Sancocha y deshilacha el pollo.", "Licúa el ají con el pan remojado en leche.",
                      "Cocina la salsa y agrega el pollo, queso y nueces.",
                      "Sirve sobre papas con arroz y aceituna.")),
            r("Sopa de verduras casera",
              "Sopa reconfortante con zanahoria, espinacas y fideos.",
              1731535, 4, "Easy", "Meals", "35 min",
              List.of("2 zanahorias", "1 puñado de espinacas", "1 papa", "Fideos cabello de ángel", "Caldo de pollo", "Apio"),
              List.of("Sofríe las verduras picadas.", "Agrega el caldo y cocina 20 minutos.",
                      "Suma los fideos y las espinacas al final.")),
            r("Tallarines verdes con pollo",
              "Pasta en salsa cremosa de espinacas y albahaca con pechuga dorada.",
              1437267, 5, "Intermediate", "Meals", "40 min",
              List.of("400 g de tallarines", "2 tazas de espinacas", "Albahaca fresca", "1/2 taza de leche", "Queso fresco", "1 pechuga de pollo"),
              List.of("Licúa espinacas, albahaca, leche y queso.", "Cocina la salsa unos minutos.",
                      "Mezcla con los tallarines cocidos.", "Sirve con el pollo dorado encima.")),
            r("Pollo saltado con verduras al wok",
              "Tiras de pollo salteadas con zanahoria, pimiento y sillao.",
              2456435, 4, "Intermediate", "Meals", "25 min",
              List.of("2 pechugas de pollo", "1 zanahoria", "1 pimiento", "Brócoli", "Sillao", "Kion"),
              List.of("Saltea el pollo en tandas a fuego alto.", "Agrega las verduras crocantes.",
                      "Sazona con sillao y kion.", "Sirve con arroz.")),
            r("Causa limeña de pollo",
              "Capas de papa amarilla prensada con relleno cremoso de pollo.",
              4553111, 5, "Advanced", "Meals", "50 min",
              List.of("1 kg de papa amarilla", "Ají amarillo licuado", "Limón", "1 pechuga de pollo cocida", "Mayonesa", "Palta"),
              List.of("Prensa las papas cocidas y sazona con ají y limón.",
                      "Mezcla el pollo deshilachado con mayonesa.",
                      "Arma capas de papa, relleno y palta.", "Enfría y decora antes de servir.")),
            r("Quiche de espinacas",
              "Tarta salada de masa crocante con relleno de espinacas, huevos y leche.",
              1600711, 4, "Advanced", "Meals", "70 min",
              List.of("1 masa quebrada", "3 huevos", "1 taza de leche", "2 tazas de espinacas", "Queso gruyere", "Nuez moscada"),
              List.of("Prehornea la masa 10 minutos.", "Saltea las espinacas.",
                      "Bate huevos, leche y nuez moscada.", "Rellena y hornea 35 minutos a 180 °C.")),
            r("Estofado de pollo",
              "Guiso casero de pollo con zanahoria, arvejas y papas en salsa de tomate.",
              2313686, 4, "Easy", "Meals", "50 min",
              List.of("4 presas de pollo", "2 zanahorias", "2 papas", "Arvejas", "Salsa de tomate", "Laurel"),
              List.of("Dora las presas de pollo.", "Sofríe cebolla, ajo y tomate.",
                      "Agrega verduras, caldo y laurel.", "Cocina a fuego bajo 30 minutos.")),
            r("Puré de zanahoria con pollo grillado",
              "Puré suave de zanahoria y papa acompañado de pechuga a la parrilla.",
              3186654, 4, "Easy", "Meals", "35 min",
              List.of("3 zanahorias", "2 papas", "Mantequilla", "1 chorrito de leche", "1 pechuga de pollo"),
              List.of("Cocina y aplasta las zanahorias y papas con mantequilla y leche.",
                      "Grilla la pechuga sazonada.", "Sirve el pollo sobre el puré.")),
            r("Arroz chaufa de pollo",
              "Arroz frito al estilo chifa con pollo, huevo y cebolla china.",
              2233729, 5, "Intermediate", "Meals", "25 min",
              List.of("3 tazas de arroz cocido frío", "1 pechuga de pollo", "2 huevos", "Cebolla china", "Sillao", "Aceite de ajonjolí", "Kion"),
              List.of("Haz una tortilla con los huevos y pícala.", "Saltea el pollo en cubos a fuego alto.",
                      "Agrega el arroz, sillao y kion.", "Termina con el huevo y la cebolla china.")),
            r("Canelones de espinaca y ricotta",
              "Canelones rellenos gratinados con salsa blanca casera.",
              1633578, 4, "Advanced", "Meals", "75 min",
              List.of("12 placas de canelones", "2 tazas de espinacas", "250 g de ricotta", "2 tazas de leche", "Harina y mantequilla", "Queso rallado"),
              List.of("Mezcla espinacas salteadas con la ricotta.", "Rellena los canelones.",
                      "Prepara salsa blanca con leche, harina y mantequilla.",
                      "Cubre y gratina 25 minutos a 180 °C.")),
            r("Milanesa de pollo con ensalada",
              "Pechuga empanizada crocante con ensalada fresca.",
              3026808, 4, "Easy", "Meals", "30 min",
              List.of("2 pechugas de pollo", "2 huevos", "Pan integral rallado", "Lechuga, tomate y zanahoria", "Limón"),
              List.of("Pasa el pollo por huevo batido y pan rallado.", "Fríe hasta dorar.",
                      "Acompaña con ensalada aliñada con limón.")),
            r("Risotto de espinacas",
              "Arroz cremoso al estilo italiano con espinacas y parmesano.",
              1660030, 5, "Advanced", "Meals", "45 min",
              List.of("1 1/2 tazas de arroz arbóreo", "2 tazas de espinacas", "Caldo de verduras caliente", "Vino blanco", "Parmesano", "Mantequilla"),
              List.of("Sofríe el arroz y desglasa con vino.", "Agrega caldo caliente de a pocos, removiendo.",
                      "Incorpora las espinacas al final.", "Manteca con parmesano y sirve cremoso.")),
            r("Pollo a la crema con champiñones",
              "Pechuga en salsa cremosa de leche y champiñones salteados.",
              5409010, 4, "Intermediate", "Meals", "35 min",
              List.of("2 pechugas de pollo", "250 g de champiñones", "1 taza de leche", "1 cucharada de harina", "Ajo", "Perejil"),
              List.of("Dora el pollo y resérvalo.", "Saltea champiñones con ajo.",
                      "Agrega la leche con la harina disuelta y espesa.",
                      "Regresa el pollo y cocina 10 minutos.")),
            r("Lasaña de verduras",
              "Capas de pasta, verduras salteadas y salsa blanca gratinadas.",
              1410235, 5, "Advanced", "Meals", "80 min",
              List.of("Placas de lasaña", "2 zanahorias", "2 tazas de espinacas", "1 zapallito", "2 tazas de leche", "Queso mozzarella", "Salsa de tomate"),
              List.of("Saltea las verduras.", "Prepara salsa blanca con la leche.",
                      "Arma capas de pasta, verduras y salsas.",
                      "Hornea 40 minutos a 180 °C y gratina.")),
            r("Salpicón de pollo",
              "Ensalada fría de pollo deshilachado con verduras y mayonesa.",
              1703272, 4, "Easy", "Meals", "25 min",
              List.of("1 pechuga de pollo cocida", "1 zanahoria cocida", "Arvejas", "Papa cocida", "Mayonesa", "Limón"),
              List.of("Deshilacha el pollo.", "Corta las verduras en cubos.",
                      "Mezcla todo con mayonesa y limón.", "Sirve frío sobre hojas de lechuga.")),

            // ---------- Snacks (10) ----------
            r("Manzanas asadas con canela",
              "Manzanas al horno rellenas de avena, pasas y canela.",
              2255935, 4, "Easy", "Snacks", "35 min",
              List.of("4 manzanas", "4 cucharadas de avena", "Pasas", "Canela", "Miel", "Mantequilla"),
              List.of("Descorazona las manzanas.", "Rellena con avena, pasas, canela y miel.",
                      "Hornea 25 minutos a 180 °C.")),
            r("Batido verde de espinacas y manzana",
              "Jugo verde refrescante con espinacas, manzana y limón.",
              1998920, 4, "Easy", "Snacks", "5 min",
              List.of("1 puñado de espinacas", "1 manzana verde", "Jugo de 1 limón", "1 vaso de agua fría", "Miel opcional"),
              List.of("Licúa todos los ingredientes.", "Cuela si lo prefieres ligero.", "Sirve con hielo.")),
            r("Chips de zanahoria al horno",
              "Láminas de zanahoria crocantes al horno con especias.",
              3298064, 4, "Easy", "Snacks", "30 min",
              List.of("3 zanahorias grandes", "Aceite de oliva", "Pimentón", "Sal"),
              List.of("Corta las zanahorias en láminas finas.", "Mezcla con aceite y especias.",
                      "Hornea a 200 °C hasta que estén crocantes, volteando a la mitad.")),
            r("Muffins de manzana y avena",
              "Muffins húmedos de manzana con avena, perfectos para la lonchera.",
              1583884, 5, "Intermediate", "Snacks", "40 min",
              List.of("2 manzanas ralladas", "1 taza de avena", "1 taza de harina", "2 huevos", "1/2 taza de leche", "Polvo de hornear", "Canela"),
              List.of("Mezcla los secos por un lado y los húmedos por otro.", "Integra sin batir de más.",
                      "Reparte en moldes y hornea 22 minutos a 180 °C.")),
            r("Palitos de pan integral con hummus",
              "Bastones de pan integral horneados con hummus casero de garbanzos.",
              3850838, 4, "Easy", "Snacks", "15 min",
              List.of("3 rebanadas de pan integral", "1 taza de garbanzos cocidos", "Tahini o aceite de oliva", "Limón", "Comino", "Ajo"),
              List.of("Corta el pan en bastones y tuéstalos.", "Licúa garbanzos, limón, ajo y comino.",
                      "Sirve el hummus con los palitos.")),
            r("Smoothie de yogur y frutas",
              "Batido cremoso de yogur griego con fruta congelada.",
              704569, 5, "Easy", "Snacks", "5 min",
              List.of("1 taza de yogur griego", "1 taza de fruta congelada", "1/2 taza de leche", "Miel"),
              List.of("Licúa el yogur con la fruta y la leche.", "Endulza con miel y sirve de inmediato.")),
            r("Queque de zanahoria",
              "El clásico carrot cake húmedo con frosting de queso crema.",
              2133989, 5, "Advanced", "Snacks", "70 min",
              List.of("3 zanahorias ralladas", "3 huevos", "1 taza de aceite", "2 tazas de harina", "Nueces", "Canela", "Queso crema y azúcar impalpable"),
              List.of("Bate huevos, azúcar y aceite.", "Integra la harina, canela, zanahoria y nueces.",
                      "Hornea 45 minutos a 175 °C.", "Cubre con frosting de queso crema al enfriar.")),
            r("Galletas de avena y manzana",
              "Galletas suaves de avena con trocitos de manzana.",
              2792186, 4, "Intermediate", "Snacks", "30 min",
              List.of("1 1/2 tazas de avena", "1 manzana en cubos pequeños", "1 huevo", "Mantequilla", "Azúcar rubia", "Canela"),
              List.of("Crema la mantequilla con el azúcar.", "Agrega el huevo, la avena y la manzana.",
                      "Forma las galletas y hornea 15 minutos a 180 °C.")),
            r("Helado casero de yogur",
              "Helado natural de yogur griego y fruta, sin máquina.",
              3535383, 4, "Intermediate", "Snacks", "15 min + congelado",
              List.of("2 tazas de yogur griego", "1 taza de fruta licuada", "Miel", "Limón"),
              List.of("Mezcla el yogur con la fruta, miel y limón.",
                      "Congela removiendo cada 45 minutos, 3 veces.", "Sirve como helado cremoso.")),
            r("Suspiro a la limeña",
              "Postre limeño de manjar blanco con merengue al oporto.",
              3590401, 5, "Advanced", "Snacks", "60 min",
              List.of("1 lata de leche evaporada", "1 lata de leche condensada", "5 yemas", "2 claras", "Azúcar", "Oporto", "Canela"),
              List.of("Cocina las leches removiendo hasta obtener manjar.", "Fuera del fuego integra las yemas.",
                      "Prepara un merengue con almíbar de oporto.",
                      "Sirve el manjar con el merengue encima y canela."))
        );
    }
}
