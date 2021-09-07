package com.tax.calculate.controller;

import com.tax.calculate.entity.Goods;
import com.tax.calculate.service.TaxCalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tax")
public class TaxCalCulateController {
    @Autowired
    public TaxCalculateService taxCalculateService;

    @PostMapping(value = "/calculate")
    public List<String> calculate(@RequestBody String[] inputs) {
        return taxCalculateService.calculateGoodsInfo(inputs);
    }
}
