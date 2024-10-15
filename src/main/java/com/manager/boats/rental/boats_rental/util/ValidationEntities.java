package com.manager.boats.rental.boats_rental.util;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import java.util.Map;
import java.util.HashMap;

@Component
public class ValidationEntities {

        public ResponseEntity<ApiResponse> validation(BindingResult result){
        Map<String,String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(new ApiResponse("erros",errores));
    }
}
