package tn.esprit.tpautoloc.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import tn.esprit.tpautoloc.exception.GlobalExceptionHandler;
import tn.esprit.tpautoloc.service.IAgenceService;
import tn.esprit.tpautoloc.web.controller.AgenceController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AgenceValidationWebTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        IAgenceService agenceService = Mockito.mock(IAgenceService.class);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(new AgenceController(agenceService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void creerAgenceSansVilleRetourneUniquementErreurVille() throws Exception {
        String body = """
                {
                  "nom": "Agence Centrale Tunis",
                  "adresse": "12 Avenue Habib Bourguiba",
                  "telephone": "+21671000000"
                }
                """;

        mockMvc.perform(post("/agences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.ville").value("La ville est obligatoire"));
    }

    @Test
    void creerAgenceSansNomRetourneUniquementErreurNom() throws Exception {
        String body = """
                {
                  "ville": "Tunis",
                  "adresse": "12 Avenue Habib Bourguiba",
                  "telephone": "+21671000000"
                }
                """;

        mockMvc.perform(post("/agences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.nom").value("Le nom est obligatoire"));
    }
}
