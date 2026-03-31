package com.royalcert.royalsys.controller;

import com.royalcert.royalsys.dto.MandayCalculationRequest;
import com.royalcert.royalsys.dto.MandayCalculationResponse;
import com.royalcert.royalsys.service.MandayCalculatorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manday-calculator")
public class MandayCalculatorController {

    private final MandayCalculatorService calculatorService;

    public MandayCalculatorController(MandayCalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<MandayCalculationResponse> calculate(@Valid @RequestBody MandayCalculationRequest request) {
        return ResponseEntity.ok(calculatorService.calculate(request));
    }
}
