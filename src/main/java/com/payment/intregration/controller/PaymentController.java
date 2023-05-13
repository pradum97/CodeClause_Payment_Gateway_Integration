package com.payment.intregration.controller;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

@Controller
public class PaymentController {

    @RequestMapping("/home")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World")
                           String name, Model model) {
        model.addAttribute("name", name);
        return "home";
    }

    @PostMapping("/create_order")
    @ResponseBody
    public ResponseEntity<Object> createOrder(@RequestParam(value = "amount", required = false)
                                              String amount) {

        try {
            double v = Double.parseDouble(amount);

            if (v < 1){
                return new ResponseEntity<>("Please enter valid amount", HttpStatus.NOT_FOUND);
            }

        } catch (NumberFormatException e) {

            return new ResponseEntity<>("Please enter valid amount", HttpStatus.NOT_FOUND);
        }

        String key = "";
        String secret = "";

        int num = new Random().nextInt(10000);

        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", (Double.parseDouble(amount) * 100));
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_rcptid_" + num);

            var razorpay = new RazorpayClient(key, secret);
            return new ResponseEntity<>(razorpay.orders.create(orderRequest).toJson().toString(), HttpStatus.OK);

        } catch (RazorpayException e) {
            return new ResponseEntity<>("Something went wrong !!", HttpStatus.NOT_FOUND);
        }
    }
}
