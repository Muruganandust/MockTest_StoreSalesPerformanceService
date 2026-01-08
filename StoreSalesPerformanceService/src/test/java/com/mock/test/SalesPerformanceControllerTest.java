package com.mock.test;


import com.mock.bean.*;
import com.mock.controller.SalesPerformanceController;
import com.mock.service.SalesPerformanceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalesPerformanceController.class)
class SalesPerformanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean // Replaces deprecated @MockBean
    private SalesPerformanceService salesPerformanceService;

    @Test
    @DisplayName("Positive: multiple stores")
    void testMultipleStoresSuccess() throws Exception {
        // Prepare Sample Input
        StoreRequestBean request = new StoreRequestBean();
        request.setStores(Arrays.asList(
                new Stores(1, "London Central", new BigDecimal(80000), new BigDecimal(5000)),
                new Stores(2, "Manchester Piccadilly", new BigDecimal(30000), new BigDecimal(2000)),
                new Stores(3, "Birmingham New Street", new BigDecimal(9000), new BigDecimal(500))
        ));

        // Mock Service Behavior
        List<StoreReport> reports = Arrays.asList(
                new StoreReport(1, "London Central", new BigDecimal(75000), "Excellent"),
                new StoreReport(2, "Manchester Piccadilly", new BigDecimal(28000), "Average"),
                new StoreReport(3, "Birmingham New Street", new BigDecimal(8500), "Poor")
        );
        List<TopStores> top = Arrays.asList(
                new TopStores(1, "London Central", new BigDecimal(75000)),
                new TopStores(2, "Manchester Piccadilly", new BigDecimal(28000))
        );

        Mockito.when(salesPerformanceService.generateStoreReports(anyList())).thenReturn(reports);
        Mockito.when(salesPerformanceService.generateTopStoreReports(anyList())).thenReturn(top);

        mockMvc.perform(post("/stores/performance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeReport[0].rating").value("Excellent"))
                .andExpect(jsonPath("$.topStores.length()").value(2));
    }

    @Test
    @DisplayName("Edge: Revenue exactly on boundary values")
    void testBoundaryValueRevenue() throws Exception {
        StoreRequestBean request = new StoreRequestBean();
        request.setStores(Arrays.asList(new Stores(1, "Edge Store", new BigDecimal(10000), BigDecimal.ZERO)));

        // Boundary test: exactly 10,000 should be "Average" (based on your logic <= 50000)
        List<StoreReport> reports = Arrays.asList(new StoreReport(1, "Edge Store", new BigDecimal(10000), "Average"));
        Mockito.when(salesPerformanceService.generateStoreReports(anyList())).thenReturn(reports);
        Mockito.when(salesPerformanceService.generateTopStoreReports(anyList())).thenReturn(Arrays.asList(new TopStores(1, "Edge Store", new BigDecimal(55000))));

        mockMvc.perform(post("/stores/performance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeReport[0].revenue").value(10000))
                .andExpect(jsonPath("$.storeReport[0].rating").value("Average"));
    }

    @Test
    @DisplayName("Negative: Returns greater than sales (Negative Revenue)")
    void testNegativeRevenue() throws Exception {
        StoreRequestBean request = new StoreRequestBean();
        request.setStores(Arrays.asList(new Stores(4, "Loss Store", new BigDecimal(1000), new BigDecimal(5000))));

        // Revenue = -4000, should be "Poor"
        List<StoreReport> reports = Arrays.asList(new StoreReport(4, "Loss Store", new BigDecimal(-4000), "Poor"));
        Mockito.when(salesPerformanceService.generateStoreReports(anyList())).thenReturn(reports);
        Mockito.when(salesPerformanceService.generateTopStoreReports(anyList())).thenReturn(Arrays.asList(new TopStores(4, "Loss Store", new BigDecimal(-4000))));

        mockMvc.perform(post("/stores/performance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeReport[0].revenue").value(-4000))
                .andExpect(jsonPath("$.storeReport[0].rating").value("Poor"));
    }
}