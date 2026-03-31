package com.royalcert.royalsys.service;

import com.royalcert.royalsys.domain.entity.*;
import com.royalcert.royalsys.domain.repository.*;
import com.royalcert.royalsys.dto.MandayCalculationRequest;
import com.royalcert.royalsys.dto.MandayCalculationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class MandayCalculatorService {

    private final MandayRuleSetRepository ruleSetRepository;
    private final MandayRuleRepository ruleRepository;
    private final MandayCalculationRepository calculationRepository;
    private final ClientRepository clientRepository;
    private final SchemeRepository schemeRepository;

    public MandayCalculatorService(MandayRuleSetRepository ruleSetRepository,
                                    MandayRuleRepository ruleRepository,
                                    MandayCalculationRepository calculationRepository,
                                    ClientRepository clientRepository,
                                    SchemeRepository schemeRepository) {
        this.ruleSetRepository = ruleSetRepository;
        this.ruleRepository = ruleRepository;
        this.calculationRepository = calculationRepository;
        this.clientRepository = clientRepository;
        this.schemeRepository = schemeRepository;
    }

    @Transactional
    public MandayCalculationResponse calculate(MandayCalculationRequest request) {
        Scheme scheme = schemeRepository.findById(request.getSchemeId())
                .orElseThrow(() -> new IllegalArgumentException("Scheme not found"));

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        MandayRuleSet ruleSet = ruleSetRepository.findBySchemeIdAndCurrentTrue(scheme.getId())
                .orElseThrow(() -> new IllegalArgumentException("No active rule set for scheme: " + scheme.getCode()));

        // Find matching rule
        MandayRule rule;
        if (request.getRiskLevel() != null) {
            rule = ruleRepository.findMatchingRule(ruleSet.getId(), request.getRiskLevel(), request.getEmployeeCount())
                    .orElseThrow(() -> new IllegalArgumentException("No matching rule for employee count: " + request.getEmployeeCount()));
        } else {
            rule = ruleRepository.findMatchingRuleNoRisk(ruleSet.getId(), request.getEmployeeCount())
                    .orElseThrow(() -> new IllegalArgumentException("No matching rule for employee count: " + request.getEmployeeCount()));
        }

        BigDecimal baseMandays = rule.getBaseMandays();
        BigDecimal stage1 = baseMandays.multiply(rule.getStage1Ratio()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal stage2 = baseMandays.multiply(rule.getStage2Ratio()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal surveillance = baseMandays.divide(rule.getSurveillanceDivisor(), 2, RoundingMode.HALF_UP);
        BigDecimal recertification = baseMandays.multiply(rule.getRecertMultiplier()).setScale(2, RoundingMode.HALF_UP);

        // Build entity
        MandayCalculation calc = new MandayCalculation();
        calc.setClient(client);
        calc.setScheme(scheme);
        calc.setRuleSet(ruleSet);
        calc.setEmployeeCount(request.getEmployeeCount());
        calc.setNaceCode(request.getNaceCode());
        calc.setRiskLevel(request.getRiskLevel());
        calc.setBaseMandays(baseMandays);
        calc.setStage1Mandays(stage1);
        calc.setStage2Mandays(stage2);
        calc.setSurveillanceMandays(surveillance);
        calc.setRecertificationMandays(recertification);

        // Apply adjustments
        if (request.getAdjustmentType() != null && !"NONE".equals(request.getAdjustmentType())) {
            BigDecimal pct = request.getAdjustmentPercent().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            calc.setAdjustmentType(request.getAdjustmentType());
            calc.setAdjustmentPercent(request.getAdjustmentPercent());
            calc.setAdjustmentReasons(request.getAdjustmentReasons());

            if ("INCREASE".equals(request.getAdjustmentType())) {
                calc.setAdjustedStage1(stage1.add(stage1.multiply(pct)).setScale(2, RoundingMode.HALF_UP));
                calc.setAdjustedStage2(stage2.add(stage2.multiply(pct)).setScale(2, RoundingMode.HALF_UP));
                calc.setAdjustedSurveillance(surveillance.add(surveillance.multiply(pct)).setScale(2, RoundingMode.HALF_UP));
                calc.setAdjustedRecertification(recertification.add(recertification.multiply(pct)).setScale(2, RoundingMode.HALF_UP));
            } else {
                calc.setAdjustedStage1(stage1.subtract(stage1.multiply(pct)).setScale(2, RoundingMode.HALF_UP));
                calc.setAdjustedStage2(stage2.subtract(stage2.multiply(pct)).setScale(2, RoundingMode.HALF_UP));
                calc.setAdjustedSurveillance(surveillance.subtract(surveillance.multiply(pct)).setScale(2, RoundingMode.HALF_UP));
                calc.setAdjustedRecertification(recertification.subtract(recertification.multiply(pct)).setScale(2, RoundingMode.HALF_UP));
            }
        }

        // Site calculations
        if (request.getSiteEmployeeCount() != null && request.getSiteEmployeeCount() > 0) {
            MandayRule siteRule;
            if (request.getRiskLevel() != null) {
                siteRule = ruleRepository.findMatchingRule(ruleSet.getId(), request.getRiskLevel(), request.getSiteEmployeeCount())
                        .orElseThrow(() -> new IllegalArgumentException("No matching site rule"));
            } else {
                siteRule = ruleRepository.findMatchingRuleNoRisk(ruleSet.getId(), request.getSiteEmployeeCount())
                        .orElseThrow(() -> new IllegalArgumentException("No matching site rule"));
            }

            BigDecimal siteBase = siteRule.getBaseMandays();
            calc.setSiteEmployeeCount(request.getSiteEmployeeCount());
            calc.setSiteBaseMandays(siteBase);
            calc.setSiteStage1(siteBase.multiply(rule.getStage1Ratio()).setScale(2, RoundingMode.HALF_UP));
            calc.setSiteStage2(siteBase.multiply(rule.getStage2Ratio()).setScale(2, RoundingMode.HALF_UP));
            calc.setSiteSurveillance(siteBase.divide(rule.getSurveillanceDivisor(), 2, RoundingMode.HALF_UP));
            calc.setSiteRecertification(siteBase.multiply(rule.getRecertMultiplier()).setScale(2, RoundingMode.HALF_UP));

            if (request.getAdjustmentType() != null && !"NONE".equals(request.getAdjustmentType())) {
                BigDecimal pct = request.getAdjustmentPercent().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
                BigDecimal s1 = calc.getSiteStage1();
                BigDecimal s2 = calc.getSiteStage2();
                BigDecimal surv = calc.getSiteSurveillance();
                BigDecimal recert = calc.getSiteRecertification();
                if ("INCREASE".equals(request.getAdjustmentType())) {
                    calc.setSiteAdjustedStage1(s1.add(s1.multiply(pct)).setScale(2, RoundingMode.HALF_UP));
                    calc.setSiteAdjustedStage2(s2.add(s2.multiply(pct)).setScale(2, RoundingMode.HALF_UP));
                    calc.setSiteAdjustedSurveillance(surv.add(surv.multiply(pct)).setScale(2, RoundingMode.HALF_UP));
                    calc.setSiteAdjustedRecertification(recert.add(recert.multiply(pct)).setScale(2, RoundingMode.HALF_UP));
                } else {
                    calc.setSiteAdjustedStage1(s1.subtract(s1.multiply(pct)).setScale(2, RoundingMode.HALF_UP));
                    calc.setSiteAdjustedStage2(s2.subtract(s2.multiply(pct)).setScale(2, RoundingMode.HALF_UP));
                    calc.setSiteAdjustedSurveillance(surv.subtract(surv.multiply(pct)).setScale(2, RoundingMode.HALF_UP));
                    calc.setSiteAdjustedRecertification(recert.subtract(recert.multiply(pct)).setScale(2, RoundingMode.HALF_UP));
                }
            }
        }

        MandayCalculation saved = calculationRepository.save(calc);
        return MandayCalculationResponse.from(saved);
    }
}
