/**
 * 
 */
package com.mock.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Muruganand
 * To map list of StoreReport and TopStores
 */
@Getter
@Setter
public class StoreResponseBean {
	private List<StoreReport> storeReport;
	private List<TopStores> topStores;
	}
