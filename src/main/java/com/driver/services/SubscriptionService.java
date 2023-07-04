package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();

        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setStartSubscriptionDate(new Date());
        subscription.setUser(user);
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());

        int price  = 0;

        if(subscription.getSubscriptionType().toString().equals("BASIC")) price  = 500 + (200 * subscriptionEntryDto.getNoOfScreensRequired());
        if(subscription.getSubscriptionType().toString().equals("PRO")) price  = 800 + (250 * subscriptionEntryDto.getNoOfScreensRequired());
        else price  = 1000 + (350 * subscriptionEntryDto.getNoOfScreensRequired());

        subscription.setTotalAmountPaid(price);
        subscription.setUser(user);

        user.setSubscription(subscription);
        userRepository.save(user);
        return price;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user = userRepository.findById(userId).get();
        Subscription subscription = user.getSubscription();

        if(subscription.getSubscriptionType().toString().equals("ELITE")){
            throw new Exception("Already the best Subscription");
        }

        int currPrice = user.getSubscription().getTotalAmountPaid();
        int priceToPay = 0;

        if(subscription.getSubscriptionType().toString().equals("BASIC")){
            priceToPay = 800 + (250 * user.getSubscription().getNoOfScreensSubscribed());
            subscription.setSubscriptionType(SubscriptionType.PRO);
        }
        else{
            priceToPay = 1000 + (350 * user.getSubscription().getNoOfScreensSubscribed());
            subscription.setSubscriptionType(SubscriptionType.ELITE);
        }
        subscription.setTotalAmountPaid(priceToPay);
        subscriptionRepository.save(subscription);

        return priceToPay-currPrice;

    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        List<Subscription> subscriptionList = subscriptionRepository.findAll();
        int revenue = 0;

        for(Subscription subscription : subscriptionList) revenue += subscription.getTotalAmountPaid();

        return revenue;

    }

}
