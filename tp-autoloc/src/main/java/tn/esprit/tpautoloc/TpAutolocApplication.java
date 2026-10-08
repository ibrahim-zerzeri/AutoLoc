package tn.esprit.tpautoloc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tn.esprit.tpautoloc.repository.IAgenceRepository;

@SpringBootApplication

public class TpAutolocApplication {
    private int id;

    public static void main(String[] args) {
        SpringApplication.run(TpAutolocApplication.class, args);
    }

}
