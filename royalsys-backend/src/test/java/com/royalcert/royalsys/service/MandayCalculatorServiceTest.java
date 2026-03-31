package com.royalcert.royalsys.service;

import com.royalcert.royalsys.domain.entity.*;
import com.royalcert.royalsys.domain.repository.*;
import com.royalcert.royalsys.dto.MandayCalculationRequest;
import com.royalcert.royalsys.dto.MandayCalculationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MandayCalculatorServiceTest {

    @Mock private MandayRuleSetRepository ruleSetRepository;
    @Mock private MandayRuleRepository ruleRepository;
    @Mock private MandayCalculationRepository calculationRepository;
    @Mock private ClientRepository clientRepository;
    @Mock private SchemeRepository schemeRepository;

    @InjectMocks
    private MandayCalculatorService service;

    private Scheme scheme;
    private Client client;
    private MandayRuleSet ruleSet;
    private MandayRule rule;

    @BeforeEach
    void setUp() {
        scheme = new Scheme();
        scheme.setId(UUID.randomUUID());
        scheme.setCode("ISO_9001");
        scheme.setName("ISO 9001");

        Organization org = new Organization();
        org.setId(UUID.randomUUID());
        org.setName("Test Org");

        client = new Client();
        client.setId(UUID.randomUUID());
        client.setOrganization(org);
        client.setCompanyName("Test Client");

        ruleSet = new MandayRuleSet();
        ruleSet.setId(UUID.randomUUID());
        ruleSet.setScheme(scheme);
        ruleSet.setName("Test Rules");
        ruleSet.setVersion(1);
        ruleSet.setEffectiveFrom(LocalDate.of(2024, 1, 1));

        rule = new MandayRule();
        rule.setId(UUID.randomUUID());
        rule.setRuleSet(ruleSet);
        rule.setEmployeeMin(46);
        rule.setEmployeeMax(65);
        rule.setBaseMandays(new BigDecimal("5.0"));
        rule.setStage1Ratio(new BigDecimal("0.300"));
        rule.setStage2Ratio(new BigDecimal("0.700"));
        rule.setSurveillanceDivisor(new BigDecimal("3.00"));
        rule.setRecertMultiplier(new BigDecimal("0.667"));
    }

    @Test
    void testBasicCalculation() {
        when(schemeRepository.findById(any())).thenReturn(Optional.of(scheme));
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        when(ruleSetRepository.findBySchemeIdAndCurrentTrue(any())).thenReturn(Optional.of(ruleSet));
        when(ruleRepository.findMatchingRuleNoRisk(any(), eq(50))).thenReturn(Optional.of(rule));
        when(calculationRepository.save(any())).thenAnswer(inv -> {
            MandayCalculation calc = inv.getArgument(0);
            calc.setId(UUID.randomUUID());
            return calc;
        });

        MandayCalculationRequest request = new MandayCalculationRequest();
        request.setSchemeId(scheme.getId());
        request.setClientId(client.getId());
        request.setEmployeeCount(50);

        MandayCalculationResponse response = service.calculate(request);

        assertNotNull(response.getId());
        assertEquals(new BigDecimal("5.0"), response.getBaseMandays());
        assertEquals(new BigDecimal("1.50"), response.getStage1Mandays());
        assertEquals(new BigDecimal("3.50"), response.getStage2Mandays());
        assertEquals(new BigDecimal("1.67"), response.getSurveillanceMandays());
        assertEquals(new BigDecimal("3.34"), response.getRecertificationMandays());
    }

    @Test
    void testCalculationWithIncrease() {
        when(schemeRepository.findById(any())).thenReturn(Optional.of(scheme));
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        when(ruleSetRepository.findBySchemeIdAndCurrentTrue(any())).thenReturn(Optional.of(ruleSet));
        when(ruleRepository.findMatchingRuleNoRisk(any(), eq(50))).thenReturn(Optional.of(rule));
        when(calculationRepository.save(any())).thenAnswer(inv -> {
            MandayCalculation calc = inv.getArgument(0);
            calc.setId(UUID.randomUUID());
            return calc;
        });

        MandayCalculationRequest request = new MandayCalculationRequest();
        request.setSchemeId(scheme.getId());
        request.setClientId(client.getId());
        request.setEmployeeCount(50);
        request.setAdjustmentType("INCREASE");
        request.setAdjustmentPercent(new BigDecimal("10"));
        request.setAdjustmentReasons("Multiple sites");

        MandayCalculationResponse response = service.calculate(request);

        assertEquals(new BigDecimal("1.65"), response.getAdjustedStage1());
        assertEquals(new BigDecimal("3.85"), response.getAdjustedStage2());
    }

    @Test
    void testCalculationWithDecrease() {
        when(schemeRepository.findById(any())).thenReturn(Optional.of(scheme));
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        when(ruleSetRepository.findBySchemeIdAndCurrentTrue(any())).thenReturn(Optional.of(ruleSet));
        when(ruleRepository.findMatchingRuleNoRisk(any(), eq(50))).thenReturn(Optional.of(rule));
        when(calculationRepository.save(any())).thenAnswer(inv -> {
            MandayCalculation calc = inv.getArgument(0);
            calc.setId(UUID.randomUUID());
            return calc;
        });

        MandayCalculationRequest request = new MandayCalculationRequest();
        request.setSchemeId(scheme.getId());
        request.setClientId(client.getId());
        request.setEmployeeCount(50);
        request.setAdjustmentType("DECREASE");
        request.setAdjustmentPercent(new BigDecimal("20"));

        MandayCalculationResponse response = service.calculate(request);

        assertEquals(new BigDecimal("1.20"), response.getAdjustedStage1());
        assertEquals(new BigDecimal("2.80"), response.getAdjustedStage2());
    }

    @Test
    void testSchemeNotFound() {
        when(schemeRepository.findById(any())).thenReturn(Optional.empty());

        MandayCalculationRequest request = new MandayCalculationRequest();
        request.setSchemeId(UUID.randomUUID());
        request.setClientId(UUID.randomUUID());
        request.setEmployeeCount(50);

        assertThrows(IllegalArgumentException.class, () -> service.calculate(request));
    }
}
