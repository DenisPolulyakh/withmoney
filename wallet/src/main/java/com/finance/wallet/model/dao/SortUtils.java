package com.finance.wallet.model.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class SortUtils {

    private SortUtils() {
    }

    public static Sort.Order getOrder(String order, Sort.Direction defaultDirection) {
        return getOrderByProperty(order, defaultDirection, "id");
    }

    public static Sort.Order getOrderByProperty(String order, Sort.Direction defaultDirection, String property) {
        if (StringUtils.isEmpty(order)) {
            return new Sort.Order(defaultDirection != null ? defaultDirection : Sort.Direction.ASC, property);
        }
        boolean minus = order.startsWith("-");
        Sort.Direction direction = minus ? Sort.Direction.DESC : Sort.Direction.ASC;
        order = minus ? order.substring(1) : order;
        return new Sort.Order(direction, order);
    }

    /**
     * конвертирует направление сортировки для формирования выражения order by из значения, присланного фронтом
     *
     * @param orderCond направление сортировки, присланное фронтом (регистр значения не имеет)
     * @return значение, приведенное к  @see {@link Sort.Direction}. Если фронт прислал некорректное значение или пусто, возвращаем ASC
     */
    public static String parseSortDirection(String orderCond) {
        String retVal = Sort.Direction.ASC.toString();
        if (!isEmpty(orderCond)) {
            for (Sort.Direction sd : Sort.Direction.values()) {
                if (sd.toString().toLowerCase().equals(orderCond.toLowerCase())) {
                    retVal = sd.toString();
                }
            }
        }
        return retVal;
    }
}
