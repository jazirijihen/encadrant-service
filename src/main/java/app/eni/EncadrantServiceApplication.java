package app.eni;

import lombok.AllArgsConstructor;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
class Encadrant{
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private String email;
	private Boolean available;
}
@RepositoryRestResource
interface EncadrantRepository extends JpaRepository<Encadrant,Long>{
	public Encadrant findEncadrantByName(String name);
}
@Projection(name = "fullEncadrant",types = Encadrant.class)
interface EncadrantProjection{
	public Long getId();
	public String getName();
	public String getEmail();
}
@SpringBootApplication
public class EncadrantServiceApplication extends SpringBootServletInitializer {
@Autowired
	EncadrantRepository encadrantRepository;
	public static void main(String[] args) {
		SpringApplication.run(EncadrantServiceApplication.class, args);
	}
@Controller
@RequestMapping("/encadrant/")
class EncadrantRestController{
	@Autowired
	EncadrantRepository encadrantRepository;
	
	/*public EncadrantRestController(EncadrantRepository encadrantRepository) {
        this.encadrantRepository = encadrantRepository;
    }*/

	@GetMapping("list")
    //@ResponseBody
    public String listEncadrants(Model model) {
    	
    	
    	List<Encadrant> le = (List<Encadrant>) encadrantRepository.findAll();
    	if(le.size()==0) le = null;
        model.addAttribute("encadrants", le);       
        return "encadrant/listEncadrants";
        
       
    }
    
    @GetMapping("add")
    public String showAddEncadrantForm(Model model) {
    	Encadrant encadrant = new Encadrant();// object dont la valeur des attributs par defaut
   
    	model.addAttribute("encadrant", encadrant);
        return "encadrant/addEncadrant";
    }
    
    @PostMapping("add")
    public String addEncadrant( Encadrant encadrant, BindingResult result) {
        if (result.hasErrors()) {
            return "encadrant/addEncadrant";
        }
        encadrantRepository.save(encadrant);
        return "redirect:list";
    }

    
    @GetMapping("delete/{id}")
    public String deleteEncadrant(@PathVariable("id") long id, Model model) {

        Encadrant encadrant = encadrantRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("l'Id de l'encadrant est invalide:" + id));

        encadrantRepository.delete(encadrant);

        return "redirect:../list";
    }
    
    
    @GetMapping("edit/{id}")
    public String showEncadrantFormToUpdate(@PathVariable("id") long id, Model model) {
        Encadrant encadrant = encadrantRepository.findById(id)
            .orElseThrow(()->new IllegalArgumentException("l'Id de l'encadrant est invalide:" + id));
        
        model.addAttribute("encadrant", encadrant);
        
        return "encadrant/updateEncadrant";
    }


    
    @PostMapping("update")
    public String updateEncadrant(Encadrant encadrant, BindingResult result,Model model) {
    	
    	
    	encadrantRepository.save(encadrant);
    	return"redirect:list";
    	
    }
    
    @GetMapping("show/{id}")
	public String showEncadrant(@PathVariable("id") long id, Model model) {
		Encadrant encadrant = encadrantRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("l'Id de l'encadrant est invalide:" + id));
		model.addAttribute("encadrant", encadrant);
		return "encadrant/showEncadrant";
	}


}

	/*@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							RepositoryRestConfiguration restConfiguration){
		return args->{
			restConfiguration.exposeIdsFor(Customer.class);
		customerRepository.save(new Customer(null,"ENI","enic@gmail.com"));
			customerRepository.save(new Customer(null,"ENSI","ensi@gmail.com"));
			customerRepository.save(new Customer(null,"ENIT","enit@gmail.com"));
		};
	}*/


}
