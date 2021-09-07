package com.tax.calculate.service.impl;

import com.tax.calculate.entity.Goods;
import com.tax.calculate.service.TaxCalculateService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaxCalculateServiceImpl implements TaxCalculateService {
    public List<String> calculateGoodsInfo(String[] inputs) {
        List<String> result = new ArrayList<>();
        List<Goods> goods = new ArrayList<>();
        BigDecimal SalesTaxes = BigDecimal.ZERO;
        BigDecimal TotalSales = BigDecimal.ZERO;
        for(String input : inputs) {
            Goods good = new Goods((input));
            goods.add(good);
            SalesTaxes = SalesTaxes.add(good.getTaxPrice());
            TotalSales = TotalSales.add(good.getSalesPrice());
            result.add(good.toString());
        }

        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        result.add("Sales Taxes: "+ decimalFormat.format(SalesTaxes));
        result.add("Total: "+ decimalFormat.format(TotalSales));
        return result;
    }
}
