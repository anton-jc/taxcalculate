package com.tax.calculate.entity;

import com.tax.calculate.constant.TaxConstant;
import com.tax.calculate.enums.GoodsType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Data
public class Goods {
    // Origin price, not include tax
    private BigDecimal originPrice;

    // Sales price, include tax
    private BigDecimal salesPrice;

    // Sales price, include tax
    private BigDecimal taxPrice;

    // Sales price, include tax
    private GoodsType goodsType;

    // Sales price, include tax
    private Boolean isImported = false;

    /*
     * tax rate
     * books, food, and medical products is 0
     * imported goods is 0.15
     * other goods is 0.1
     */
    private BigDecimal taxRate = BigDecimal.ZERO;

    /*
     * The format must be xxxxxx: double(price)
     */
    private String goodsInfo = "";

    // the param format must be xxxx at 12.49(price)
    public Goods(String goodsOriginInfo) {
        this.initGoodsInfo(goodsOriginInfo);
    }

    private void initGoodsInfo(String goodsOriginInfo) {
        String[] info = goodsOriginInfo.split(" at ");

        if (info.length <= 1) {
            log.error("Param invalid: param format is incorrect: {}", goodsOriginInfo);
            throw new InvalidParameterException("the goods format is incorrect");
        }
        for (int i = 0; i < info.length; i++) {
            if (i == info.length - 1) {
                try {
                    originPrice = BigDecimal.valueOf(Double.parseDouble(info[i])).setScale(2,BigDecimal.ROUND_DOWN);;
                    salesPrice = BigDecimal.valueOf(Double.parseDouble(info[i])).setScale(2,BigDecimal.ROUND_DOWN);
                    if (originPrice.doubleValue() < 0) {
                        log.error("Param invalid: price is less than 0: {}", goodsOriginInfo);
                        throw new InvalidParameterException("the goods price is less than 0");
                    }
                } catch (NumberFormatException ex) {
                    log.error("Param invalid: price is not a number: {}", goodsOriginInfo);
                    throw new InvalidParameterException("the goods price is incorrect");
                }
            } else {
                goodsInfo = goodsInfo + info[i];
            }
        }

        this.checkGoodsType();
        boolean isFree = GoodsType.isFree(goodsType);
        if (!isFree) {
            taxRate = taxRate.add(BigDecimal.valueOf(0.1));
        }
        if (isImported) {
            taxRate = taxRate.add(BigDecimal.valueOf(0.05));
        }
        taxPrice = formatTax(taxRate.doubleValue() * originPrice.doubleValue()).setScale(2,BigDecimal.ROUND_DOWN);
        salesPrice = salesPrice.add(taxPrice).setScale(2,BigDecimal.ROUND_DOWN);
    }

    private BigDecimal formatTax(double d) {
        int lastValue = ((int)(d*100))%10;
        if (lastValue == 0 || lastValue == 5) {
            return BigDecimal.valueOf(((int)(d*100))/100.00);
        } else if (lastValue <= 4) {
            return BigDecimal.valueOf(((int)(d*10))/10.0).add(BigDecimal.valueOf(0.05));
        } else {
            return BigDecimal.valueOf(((int)(d*10))/10.0).add(BigDecimal.valueOf(0.1));
        }
    }

    private void checkGoodsType() {
        List<String> infoSplit = Arrays.asList(goodsInfo.toLowerCase().split(" "));
        for (String keyWord : TaxConstant.ImportedKeyWord) {
            if (infoSplit.contains(keyWord)) {
                isImported = true;
                break;
            }
        }

        for (String keyWord : TaxConstant.BookTypeKeyWord) {
            if (infoSplit.contains(keyWord)) {
                goodsType = GoodsType.BOOK;
                return;
            }
        }
        for (String keyWord : TaxConstant.FoodTypeKeyWord) {
            if (infoSplit.contains(keyWord)) {
                goodsType = GoodsType.FOOD;
                return;
            }
        }
        for (String keyWord : TaxConstant.MecidalTypeKeyWord) {
            if (infoSplit.contains(keyWord)) {
                goodsType = GoodsType.MEDICAL;
                return;
            }
        }

        goodsType = GoodsType.NORMAL;
    }

    @Override
    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return goodsInfo + ": " + decimalFormat.format(salesPrice);
    }
}
