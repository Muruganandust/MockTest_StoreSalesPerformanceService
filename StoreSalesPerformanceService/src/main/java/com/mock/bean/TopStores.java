package com.mock.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
/**
 * Class contains the top store details
 * @author Muruganand
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class TopStores {
    int id;
    String name;
    BigDecimal revenue;
}