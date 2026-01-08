
package com.mock.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.mock.bean.StoreReport;
import com.mock.bean.Stores;
import com.mock.bean.TopStores;
import com.mock.constant.SalesPerformanceConstant;
import org.springframework.stereotype.Service;

/**
 * Utility class it contain all helper logic.
 * 
 * @author Muruganand
 *
 */
@Service
public class SalesPerformanceService {

	/**
	 * This method calculate the revenue and rating from storeinput and generate the
	 * store reports.
	 * 
	 * @param stores
	 * @return storeReportList
	 */
	public List<StoreReport> generateStoreReports(List<Stores> stores) {
		List<StoreReport> storeReportList = new ArrayList<StoreReport>();
		storeReportList = stores.stream().map(input -> {
            BigDecimal revenue=BigDecimal.ZERO;
            String rating=SalesPerformanceConstant.NONE;
            if(input.getTotalSales()!=null && input.getReturns()!=null) {
                // Calculate revenue
                revenue = input.getTotalSales().subtract(input.getReturns());
                // Determine rating
                rating = (revenue.compareTo(BigDecimal.valueOf(10000)) < 0) ? SalesPerformanceConstant.POOR
                        : (revenue.compareTo(BigDecimal.valueOf(50000)) <= 0) ? SalesPerformanceConstant.AVERAGE : SalesPerformanceConstant.EXCELLENT;
            }
            // Transform to Output Bean
			return new StoreReport(input.getId(), input.getName(), revenue, rating);
		}).sorted(Comparator.comparing(StoreReport::getRevenue).reversed()).toList();
		return storeReportList;
	}

	/**
	 * This method return top 2 store list.
	 * 
	 * @param storeReports
	 * @return topCustomer
	 */
	public List<TopStores> generateTopStoreReports(List<StoreReport> storeReports) {
		List<TopStores> topCustomer = null;
		if (!storeReports.isEmpty()) {
			topCustomer = storeReports.stream().map(c -> new TopStores(c.getId(), c.getName(), c.getRevenue()))
					.sorted(Comparator.comparing(TopStores::getRevenue).reversed()).limit(2).toList();
		}
		return topCustomer;
	}

}
