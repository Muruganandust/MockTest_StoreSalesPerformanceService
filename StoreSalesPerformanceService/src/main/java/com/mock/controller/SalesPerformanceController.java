package com.mock.controller;

import com.mock.bean.StoreReport;
import com.mock.bean.StoreResponseBean;
import com.mock.bean.StoreRequestBean;
import com.mock.bean.TopStores;
import com.mock.service.SalesPerformanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class SalesPerformanceController {
    private static final Logger logger = LoggerFactory.getLogger(SalesPerformanceController.class);
    @Autowired
    SalesPerformanceService salesPerformanceService;

    @PostMapping("/stores/performance")
    public ResponseEntity handleSalesPerformance(@RequestBody StoreRequestBean storeRequestBean) {

        StoreResponseBean storeResponseBean = new StoreResponseBean();
        List<StoreReport> storeReports;
        List<TopStores> topStores = null;
        if (storeRequestBean == null || storeRequestBean.getStores() == null) {
           return ResponseEntity.badRequest().body("Invalid input format");
        }
        // To find the StoreReport list
        storeReports = getStoreReports(storeRequestBean);
        // To find the Top 2 Stores
        topStores = getTopStores(storeReports, topStores);
        // Construct Response
        if (storeReports != null && topStores != null) {
            storeResponseBean.setStoreReport(storeReports);
            storeResponseBean.setTopStores(topStores);
        } else {
            return ResponseEntity.badRequest().body("Internal error");
        }
        return new ResponseEntity<>(storeResponseBean, HttpStatus.OK);
    }
    // To find the Top 2 Stores
    private List<TopStores> getTopStores(List<StoreReport> storeReports, List<TopStores> topStores) {
        if (storeReports != null) {
            topStores = salesPerformanceService.generateTopStoreReports(storeReports);
            if (logger.isDebugEnabled()) {
                topStores.forEach(e -> logger.debug("Id:{} Name:{} Revenue:{}", e.getId(), e.getName(), e.getRevenue()));
            }
        }else{
            logger.error("storeReports is empty");
        }
        return topStores;
    }

    // To find the StoreReport list
    private List<StoreReport> getStoreReports(StoreRequestBean storeRequestBean) {
        List<StoreReport> storeReports;
        storeReports = salesPerformanceService.generateStoreReports(storeRequestBean.getStores());
        if (logger.isDebugEnabled()) {
            storeReports.forEach(e -> logger.debug("Id:{} Name:{} Revenue:{} Rating:{}", e.getId(),
                    e.getName(), e.getRevenue(), e.getRating()));
        }
        return storeReports;
    }
}
