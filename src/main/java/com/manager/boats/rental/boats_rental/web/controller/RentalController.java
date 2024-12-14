package com.manager.boats.rental.boats_rental.web.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manager.boats.rental.boats_rental.persistence.models.Rental;
import com.manager.boats.rental.boats_rental.persistence.models.Users;
import com.manager.boats.rental.boats_rental.repositories.IRentalRepository;
import com.manager.boats.rental.boats_rental.services.exception.NotFoundException;
import com.manager.boats.rental.boats_rental.services.interfaces.IRentalServices;
import com.manager.boats.rental.boats_rental.util.ApiResponse;
import com.manager.boats.rental.boats_rental.util.ExtractErrorMessage;
import com.manager.boats.rental.boats_rental.util.ValidationEntities;
import com.manager.boats.rental.boats_rental.web.controller.dto.RentalDto;
import com.manager.boats.rental.boats_rental.web.controller.dto.RentalResponse;

import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


 


@RestController
@RequestMapping("${api.base.path}/rentals")
public class RentalController {

    @Autowired
    private ExtractErrorMessage extractErrorMessage;


    @Autowired
    private ValidationEntities validationEntities;

    @Autowired
    private IRentalServices rentalServices;
    

    @Autowired
    private IRentalRepository rentalRepository;


    @Autowired
    private JavaMailSender javaMailSender;

    private final Logger log = LoggerFactory.getLogger(RentalController.class); // O la clase donde esté sendConfirmationEmail


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> getRentals() {
        return ResponseEntity.ok().body(new ApiResponse("suscces",rentalServices.getAll()));
    }
    
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<ApiResponse> getRentaOfUser(@AuthenticationPrincipal Users user) {
        try {
            List<RentalResponse> rentals = rentalServices.getByUserId(user.getId());
            return ResponseEntity.ok().body(new ApiResponse("get rental", rentals));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", "Rental not found for user " + user.getId()));
        } catch (JwtException e) {  // Capturar excepciones JWT
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("error", "Unauthorized"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")    
    @Operation( 
        summary = "Update date of rental",
        description = "Admin updates date of rental."
        )
    public ResponseEntity<ApiResponse> updateRental(@Valid @RequestBody RentalDto entity, BindingResult result ,@PathVariable Long id ) {
        if(result.hasFieldErrors()){
            return validationEntities.validation(result);
        }
        try {
            rentalServices.updateRental(entity, id);
            return ResponseEntity.ok().body(new ApiResponse("update",entity));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }catch (Exception e) {
            String message = extractErrorMessage.extractErrorMessage(e.getMessage()); //o el mensaje que quieras enviar
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", message));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')") 
    @GetMapping("/cancel/rental/{id}") 
    public ResponseEntity<ApiResponse> cancelRental(@PathVariable Long id, @AuthenticationPrincipal Users user) {
        try { 
            rentalServices.cancelRental(id,user);
            return ResponseEntity.ok().body(new ApiResponse("success", "Rental cancelled")); 
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage())); 
        } catch (IllegalStateException e) { // Para la excepción de cancelación fuera de plazo 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("error", e.getMessage())); 
        } catch (Exception e) { 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Failed to cancel rental")); 
        } 
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse> deleteRental(@PathVariable Long id){
        try {
            rentalServices.delete(id);
            return ResponseEntity.ok().body(new ApiResponse("delete",null));
            
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error",e.getMessage()));
        }

    }

    @PostMapping("/client/boatsId/{boatsId}")
    @Operation( 
        summary = "Save rental",
        description = "Send email to user for confirmation."
        )
    public ResponseEntity<ApiResponse> saveRental(
                @Valid 
                @RequestBody RentalDto entity, 
                BindingResult result ,
                @PathVariable Long boatsId,
                @AuthenticationPrincipal Users user
            )
        {
        
        if(result.hasFieldErrors()){
            return validationEntities.validation(result);
        }
        try {
            Rental savedRental = rentalServices.save(entity, user.getId(), boatsId);
            
            
            // Generar token de confirmación
            String confirmationToken = UUID.randomUUID().toString();
            
            // Guardar el token en la renta (necesitas agregar el atributo confirmationToken a la entidad Rental)
            savedRental.setConfirmationToken(confirmationToken);
            rentalRepository.save(savedRental); // Asegúrate de que se guarda en la base de datos
            
            
            // Enviar correo electrónico con el enlace de confirmación
            String confirmationLink = "http://localhost:8080/api/v1/rentals/confirm/" + confirmationToken;  // Reemplaza con tu dominio
            log.debug("Enviando correo de confirmación a: {}", savedRental.getUser().getEmail());
            log.debug("Enlace de user: {}",savedRental.getUser());
            sendConfirmationEmail(savedRental.getUser().getEmail(), confirmationLink); // Suponiendo que tienes el email del usuario
            
            return ResponseEntity.ok().body(new ApiResponse("success", savedRental));
            
            
        }catch (NotFoundException e) {  // <-- Captura la NotFoundException aquí
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage()));
        }catch (DataIntegrityViolationException e) {
            String message = extractErrorMessage.extractErrorMessage(e.getMessage()); // Extrae el mensaje de la excepción de la base de datos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("error", message)); // 400 Bad Request es más apropiado
        } catch (MailException | MessagingException e) { //  Manejar la excepcion
            // ... Maneja el error. Podría ser loguearlo, devolver un error 500, etc.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Error sending confirmation email."));
        } catch (Exception e) {  // Otras excepciones (Internal Server Error)
            log.error("Error al guardar la renta: {}", e); // Log con detalles para depuración
            String message = extractErrorMessage.extractErrorMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", message));
        }
        
    }
    
    @GetMapping("/confirm/{token}")
    @Operation( 
        summary = "Confirm rental for user",
        description = "confirm the rent through the url sent by email."
        )
    
    public ResponseEntity<ApiResponse> confirmRental(@PathVariable String token) {
        try {
            rentalServices.confirmRental(token);  // Implementa este método en tu servicio
            return ResponseEntity.ok().body(new ApiResponse("success", "Rental confirmed"));  //  O redirige a una página de éxito
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage()));
        } catch (Exception e) {  // Otros errores
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error","Failed to confirm Rental."));
        }
    }

    
    private void sendConfirmationEmail(String toEmail, String confirmationLink) throws MailException, MessagingException {
        try {
            log.debug("Enviando correo de confirmación a: {}", toEmail);
            log.debug("Enlace de confirmación: {}", confirmationLink);
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("abelyludmila@gmail.com");
            message.setTo(toEmail);
            message.setSubject("Confirma tu renta");
            message.setText("Haz clic en el siguiente enlace para confirmar tu renta: " + confirmationLink);
            
            javaMailSender.send(message);
            
            log.info("Correo de confirmación enviado correctamente a: {}", toEmail);
            
            
        } catch (MailException e) {
            log.error("Error al enviar correo de confirmación a {}: {}", toEmail, e); // Loggea la excepción con detalles
        }
        
    }
    
}


// private void sendConfirmationEmail(String userEmail, String confirmationLink) throws MessagingException {

//     MimeMessage message = emailSender.createMimeMessage();
//     MimeMessageHelper helper = new MimeMessageHelper(message, true); // true para HTML

//     helper.setFrom("tucorreo@gmail.com"); // Configura el remitente
//     helper.setTo(userEmail); // Email del usuario
//     helper.addCc("admin@example.com"); // Copia al administrador.  Cambia por la dirección correcta

//     helper.setSubject("Confirma tu renta");
//     helper.setText("Haz clic en el siguiente enlace para confirmar tu renta: " + confirmationLink,true); // true indica formato HTML en el mensaje


//     emailSender.send(message);
// }