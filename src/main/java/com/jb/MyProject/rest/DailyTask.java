package com.example.MyProject.rest;

import com.example.MyProject.entity.Coupon;
import com.example.MyProject.exceptions.NoSuchCompanyException;
import com.example.MyProject.exceptions.NoSuchCouponException;
import com.example.MyProject.repository.CouponRepository;
import com.example.MyProject.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class DailyTask implements Runnable {

    private static final long day = 86400000;
    private static boolean isWorking = false;
    private CouponRepository couponRepo;
    private ApplicationContext context;

    @Autowired
    public DailyTask(CouponRepository couponRepo, ApplicationContext context) {
        this.couponRepo = couponRepo;
        this.context = context;
    }
    @Override
    public void run() {
        isWorking = true;
        while (isWorking) {
            long coupon_id;
            for (Coupon coupon : couponRepo.findAll()) {
                coupon_id = coupon.getId();
                LocalDate endDate = coupon.getEndDate();
                if (LocalDate.now().isAfter(endDate)) {
                    CompanyService companyService = context.getBean(CompanyService.class);
                    try {
                        companyService.deleteCouponById(coupon_id);
                    } catch (NoSuchCouponException | NoSuchCompanyException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(day);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void stopDailyTask() {
        isWorking = false;
    }
}