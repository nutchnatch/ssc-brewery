package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BeerRestControllerIT extends BaseIT {

    @Test
    void deleteBeersUrl() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df8c39-90c4-4ae8-b663-453e8e19c311")
                        .param("apiKey", "spring")
                        .param("apiSecret", "guru"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeersBadCredsUrl() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df8c39-90c4-4ae8-b663-453e8e19c311")
                        .param("apiKey", "spring")
                        .param("apiSecret", "guruxxx"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeersBadCredentials() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df8c39-90c4-4ae8-b663-453e8e19c311")
                .header("Api-Key", "spring")
                .header("Api-Secret", "guruxxx"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    void deleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/97df8c39-90c4-4ae8-b663-453e8e19c311")
                .header("Api-Key", "spring").header("Api-secret", "guru"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer/"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerById() throws Exception{
        mockMvc.perform(get("/api/v1/beer/97df8c39-90c4-4ae8-b663-453e8e19c311"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/86131234200036"))
                .andExpect(status().isOk());
    }
}
