package com.tax.calculate.enums;

public enum GoodsType {
    NORMAL(0),
    FOOD(1),
    BOOK(2),
    MEDICAL(4);

    private Integer code;
    private GoodsType(Integer code) {
        this.code = code;
    }

    public static boolean isFree(GoodsType type) {
        if (type.equals(FOOD) || type.equals(BOOK) || type.equals(MEDICAL)) {
            return true;
        }
        return false;
    }
}
