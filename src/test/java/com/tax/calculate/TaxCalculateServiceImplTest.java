package com.tax.calculate;

import com.tax.calculate.service.TaxCalculateService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.InvalidParameterException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaxCalculateServiceImplTest {
    @Autowired
    public TaxCalculateService taxCalculateService;

    @Test
    public void testCalculateGoodsInfoSuccess() {
        String[] param = {"1 imported bottle of perfume at 27.99",
                "1 bottle of perfume at 18.99",
                "1 packet of headache pills at 9.75",
                "1 box of imported chocolates at 11.25",
                "1 book at 12.49",
                "1 music CD at 14.99",
                "1 chocolate bar at 0.85",
                "1 imported box of chocolates at 10.00",
                "1 imported bottle of perfume at 47.50"};
        String[] exceptResult = {"1 imported bottle of perfume: 32.19",
                "1 bottle of perfume: 20.89",
                "1 packet of headache pills: 9.75",
                "1 box of imported chocolates: 11.85",
                "1 book: 12.49",
                "1 music CD: 16.49",
                "1 chocolate bar: 0.85",
                "1 imported box of chocolates: 10.50",
                "1 imported bottle of perfume: 54.65",
                "Sales Taxes: 15.85",
                "Total: 169.66"};
        String[] actualResult = new String[11];
        taxCalculateService.calculateGoodsInfo(param).toArray(actualResult);
        TestCase.assertEquals(actualResult.length, exceptResult.length);
        for (int i = 0; i < actualResult.length; i++) {
            TestCase.assertEquals(actualResult[i], exceptResult[i]);
        }
    }

    @Test
    public void testCalculateGoodsInfoSuccess1() {
        String[] param = {"1 imported bottle of perfume at 0.00",
                "1 bottle of perfume at 0",
                "1 packet of headache pills at 0.0000",
                "1 imported bottle of perfume at 0.006156",
                "1 imported packet of page at 10.12658",
                "1 Imported packet of Pills at 10.12658"};
        String[] exceptResult = { "1 imported bottle of perfume: 0.00",
                "1 bottle of perfume: 0.00",
                "1 packet of headache pills: 0.00",
                "1 imported bottle of perfume: 0.00",
                "1 imported packet of page: 11.67",
                "1 Imported packet of Pills: 10.62",
                "Sales Taxes: 2.05",
                "Total: 22.29"};
        String[] actualResult = new String[8];
        taxCalculateService.calculateGoodsInfo(param).toArray(actualResult);
        TestCase.assertEquals(actualResult.length, exceptResult.length);
        for (int i = 0; i < actualResult.length; i++) {
            TestCase.assertEquals(actualResult[i], exceptResult[i]);
        }
    }

    @Test(expected = InvalidParameterException.class)
    public void testCalculateGoodsInfoSuccessException1() {
        taxCalculateService.calculateGoodsInfo(new String[]{"1 invalid price at -1"});
    }

    @Test(expected = InvalidParameterException.class)
    public void testCalculateGoodsInfoSuccessException2() {
        taxCalculateService.calculateGoodsInfo(new String[]{"1 invalid price at 2d2d"});
    }

    @Test(expected = InvalidParameterException.class)
    public void testCalculateGoodsInfoSuccessException3() {
        taxCalculateService.calculateGoodsInfo(new String[]{"at 22"});
    }

    @Test(expected = InvalidParameterException.class)
    public void testCalculateGoodsInfoSuccessException4() {
        taxCalculateService.calculateGoodsInfo(new String[]{""});
    }
}
