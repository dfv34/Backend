
package com.portfolio.dfv.Controller;

import com.portfolio.dfv.Dto.dtoProyecto;
import com.portfolio.dfv.Entity.Proyecto;
import com.portfolio.dfv.Security.Controller.Mensaje;
import com.portfolio.dfv.Service.SProyecto;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proylab")
@CrossOrigin(origins = "https://portfoliodfv-83217.web.app")
public class CProyecto {
    @Autowired
    SProyecto sProyecto;
    
    @GetMapping("/lista")
    public ResponseEntity<List<Proyecto>> list(){
        List<Proyecto> list = sProyecto.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }
    
    @GetMapping("/detail/{id}")
    public ResponseEntity<Proyecto> getById(@PathVariable("id") int id){
        if(!sProyecto.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Proyecto proyecto = sProyecto.getOne(id).get();
        return new ResponseEntity(proyecto, HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        if (!sProyecto.existsById(id)) {
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        }
        sProyecto.delete(id);
        return new ResponseEntity(new Mensaje("producto eliminado"), HttpStatus.OK);
    }

    
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody dtoProyecto dtoproy){      
        if(StringUtils.isBlank(dtoproy.getNombrePro()))
            return new ResponseEntity(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        if(sProyecto.existsBynombrePro(dtoproy.getNombrePro()))
            return new ResponseEntity(new Mensaje("Ese Proyecto existe"), HttpStatus.BAD_REQUEST);
        
       Proyecto proyecto = new Proyecto(dtoproy.getNombrePro(),dtoproy.getFechaPro(),dtoproy.getDescPro(),dtoproy.getImagenPro());
        sProyecto.save(proyecto);
        
        return new ResponseEntity(new Mensaje("Experiencia agregada"), HttpStatus.OK);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody dtoProyecto dtoproy){
        //Validamos si existe el ID
        if(!sProyecto.existsById(id))
            return new ResponseEntity(new Mensaje("El ID no existe"), HttpStatus.BAD_REQUEST);
        //Compara nombre de experiencias
        if(sProyecto.existsBynombrePro(dtoproy.getNombrePro()) && sProyecto.getBynombrePro(dtoproy.getNombrePro()).get().getIdPro() != id)
            return new ResponseEntity(new Mensaje("Ese proyecto ya existe"), HttpStatus.BAD_REQUEST);
        //No puede estar vacio
        if(StringUtils.isBlank(dtoproy.getNombrePro()))
            return new ResponseEntity(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        
        Proyecto proyecto = sProyecto.getOne(id).get();
        proyecto.setNombrePro(dtoproy.getNombrePro());
        proyecto.setDescPro((dtoproy.getDescPro()));
        proyecto.setFechaPro((dtoproy.getFechaPro()));
        proyecto.setImagenPro((dtoproy.getImagenPro()));
        
        sProyecto.save(proyecto);
        return new ResponseEntity(new Mensaje("Proyecto actualizado"), HttpStatus.OK);
             
    }
    
}
