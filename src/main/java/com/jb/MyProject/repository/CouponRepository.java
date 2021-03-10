package com.jb.MyProject.repository;

import com.jb.MyProject.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    List<Coupon> findAllByDescription(String description);

    List<Coupon> findAllByTitle(String title);

    List<Coupon> findAllByCategory(String category);

    List<Coupon> findAllByPriceLessThan(double price);

    List<Coupon> findAllByPriceIsGreaterThan(double price);

    List<Coupon> findAllByPriceBetween(double fromPrice, double toPrice);

    List<Coupon> findAllByEndDate(LocalDate endDate);

    List<Coupon> findAllByStartDate(LocalDate startDate);

    List<Coupon> findAll();

    void deleteById(long id);

    @Query("select coupon from Customer as customer join customer.coupons as coupon where customer.id=:customer_id")
    List<Coupon> findAllByCustomerId(long customer_id);

    @Query("select coupon from Company as company join company.coupons as coupon where company.id=:company_id")
    List<Coupon> findAllByCompanyId(long company_id);


}
