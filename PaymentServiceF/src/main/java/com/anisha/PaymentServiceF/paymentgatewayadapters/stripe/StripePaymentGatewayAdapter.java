package com.anisha.PaymentServiceF.paymentgatewayadapters.stripe;

import com.anisha.PaymentServiceF.dtos.OrderDto;
import com.anisha.PaymentServiceF.models.PaymentGateway;
import com.anisha.PaymentServiceF.models.StripeProductOrder;
import com.anisha.PaymentServiceF.paymentgatewayadapters.PaymentGatewayAdapter;
import com.anisha.PaymentServiceF.repositories.StripeProductOrderRepository;
import com.stripe.StripeClient;
import com.stripe.model.PaymentLink;
import com.stripe.model.StripeObject;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentLinkCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StripePaymentGatewayAdapter implements PaymentGatewayAdapter {
    private final StripeClient stripeClient;
    private StripeProductOrderRepository spor;

    @Autowired
    public StripePaymentGatewayAdapter(StripeClient stripeClient, StripeProductOrderRepository spor) {
        this.stripeClient = stripeClient;
        this.spor=spor;
    }


    @Override
    public PaymentGateway getGatewayType() {
        return PaymentGateway.STRIPE;
    }

    @Override
    public String createPaymentLink(OrderDto order) throws Exception {

        List<Long> productIds = order != null ? order.getProductIds() : new ArrayList<>();
        List<String> priceIds = new ArrayList<>();

        if (productIds != null) {
            for (Long productId : productIds) {
                Optional<StripeProductOrder> stripeProductOrderOpt = spor.findByProductId(productId);
                if (stripeProductOrderOpt.isPresent()) {
                    String priceId = stripeProductOrderOpt.get().getStripePriceId();
                    if (priceId != null) {
                        priceIds.add(priceId);
                    }
                }
            }
        }
        if (priceIds.isEmpty()) {
            throw new IllegalArgumentException("No products found for the order; cannot create payment link.");
        }
        PaymentLinkCreateParams.Builder paramsBuilder = PaymentLinkCreateParams.builder()
                .setCurrency("inr")
                .setAfterCompletion(
                        PaymentLinkCreateParams.AfterCompletion.builder()
                                .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT)
                                .setRedirect(
                                        PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                                .setUrl("https://oauth.pstmn.io/v1/browser-callback") // redirect after payment
                                                .build()
                                )
                                .build()
                )
                .setInvoiceCreation(
                        PaymentLinkCreateParams.InvoiceCreation.builder().setEnabled(true).build()
                )
                .setPhoneNumberCollection(
                        PaymentLinkCreateParams.PhoneNumberCollection.builder().setEnabled(false).build()
                )
                .setPaymentIntentData(
                        PaymentLinkCreateParams.PaymentIntentData.builder()
                                .putMetadata("orderId", order.getOrderId())
                                .putMetadata("emailId", order.getEmail() )
                                .build())

                ;

        // Add each product (price ID) as a line item
        for (String priceId : priceIds) {
            paramsBuilder.addLineItem(
                    PaymentLinkCreateParams.LineItem.builder()
                            .setPrice(priceId)
                            .setQuantity(1L) // you can make quantity dynamic too
                            .build()
            );
        }

        PaymentLinkCreateParams params = paramsBuilder.build();
        RequestOptions requestOptions = RequestOptions.builder()
                .setIdempotencyKey("payment-" + System.currentTimeMillis())
                .build();

        PaymentLink paymentLink = PaymentLink.create(params, requestOptions);
        return paymentLink.getUrl();

    }

}