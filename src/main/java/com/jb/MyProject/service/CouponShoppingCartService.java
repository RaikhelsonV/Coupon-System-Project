package com.jb.MyProject.service;

import com.jb.MyProject.entity.CouponShoppingCart;

import java.util.Set;

public interface CouponShoppingCartService {

    long getTotalPriceOfPurchasesCouponsInShoppingCart();

    Set<CouponShoppingCart> formationTableCouponsInShoppingCart();
}
