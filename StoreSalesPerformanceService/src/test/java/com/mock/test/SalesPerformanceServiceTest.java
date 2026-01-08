package com.mock.test;

import com.mock.bean.StoreReport;
import com.mock.bean.Stores;
import com.mock.bean.TopStores;
import com.mock.constant.SalesPerformanceConstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.mock.service.SalesPerformanceService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SalesPerformanceServiceTest {

    private SalesPerformanceService salesPerformanceService;

    @BeforeEach
    void setUp() {
        salesPerformanceService = new SalesPerformanceService();
    }

    @Test
    @DisplayName("Should correctly calculate revenue, rating, and sort by revenue")
    void testGenerateStoreReports_Success() {
        // input
        Stores store1 = new Stores(1, "London Central", new BigDecimal(80000), new BigDecimal(5000));
        Stores store2 = new Stores(2, "Manchester Piccadilly", new BigDecimal(30000), new BigDecimal(2000));
        Stores store3 = new Stores(3, "Birmingham New Street", new BigDecimal(9000), new BigDecimal(500));

        List<Stores> input = Arrays.asList(store2, store1, store3);

        // result
        List<StoreReport> result = salesPerformanceService.generateStoreReports(input);

        // Assert
        assertEquals(3, result.size());

        // Check Sorting (London should be first with 90000)
        assertEquals("London Central", result.get(0).getName());
        assertEquals(new BigDecimal("75000"), result.get(0).getRevenue());
        assertEquals(SalesPerformanceConstant.EXCELLENT, result.get(0).getRating());
       // Check Average rating
        assertEquals(new BigDecimal("28000"), result.get(1).getRevenue());
        assertEquals(SalesPerformanceConstant.AVERAGE, result.get(1).getRating());

        // Check Poor rating
        assertEquals(new BigDecimal("8500"), result.get(2).getRevenue());
        assertEquals(SalesPerformanceConstant.POOR, result.get(2).getRating());
    }

    @Test
    @DisplayName("Should return top 2 stores sorted by revenue")
    void testGenerateTopStoreReports() {
        // Arrange
        StoreReport r1 = new StoreReport(1, "London Central", new BigDecimal("50000"), "Excellent");
        StoreReport r2 = new StoreReport(2, "Manchester Piccadilly", new BigDecimal("100000"), "Excellent");
        StoreReport r3 = new StoreReport(3, "Birmingham New Street", new BigDecimal("10000"), "Average");

        List<StoreReport> reports = Arrays.asList(r1, r2, r3);

        // Act
        List<TopStores> topStores = salesPerformanceService.generateTopStoreReports(reports);

        // Assert
        assertNotNull(topStores);
        assertEquals(2, topStores.size());
        assertEquals("Manchester Piccadilly", topStores.get(0).getName()); // Highest revenue
        assertEquals(new BigDecimal("100000"), topStores.get(0).getRevenue());
        assertEquals("London Central", topStores.get(1).getName()); // Second highest
    }

    @Test
    @DisplayName("Should handle empty store list")
    void testGenerateStoreReports_EmptyInput() {
        List<StoreReport> result = salesPerformanceService.generateStoreReports(Collections.emptyList());
        assertTrue(result.isEmpty());
    }
}