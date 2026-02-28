package org.baoxdev.hotelbooking_test.service.impl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.baoxdev.hotelbooking_test.model.entity.Payments;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "NOTIFICATION_SERVICE")
public class NotificationService{
    @Value("${spring.sendgrid.from-email}")
    private String from ;
    @Value("${app.verification-link-base}")
    private String verifyLinkBase;
    private  final SendGrid sendGrid;

    public void emailNotifyPaymentSuccess(Payments payments) throws IOException {
        log.info("Email payment success started");

        Email fromEmail = new Email(from);
        Email toEmail = new Email(payments.getUser().getEmail());
        String subject = "Thanh Toán Thành Công ";

        Map<String , String> map = new HashMap<>();
        map.put("name" , payments.getUser().getUserName());
        map.put("bookingCode" , payments.getBooking().getBookingCode() );
        map.put("hotelName" , payments.getBooking().getHotel().getHotelName());
        map.put("checkIn", String.valueOf(payments.getBooking().getCheckInDate())) ;
        map.put("checkOut" , String.valueOf(payments.getBooking().getCheckOutDate()));
        map.put("amount" , String.valueOf(payments.getAmountPayment()));

        Mail mail = new Mail();
        mail.setFrom(fromEmail);
        mail.setSubject(subject);

        Personalization personalization =new Personalization();
        personalization.addTo(toEmail);

        //Add to dynamic data
        map.forEach(personalization::addDynamicTemplateData);

        mail.addPersonalization(personalization);
        mail.setTemplateId("d-dfa1b94b4aac4022a2bdcbc328d2d54f");

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        //response cua request gui di
        Response response = sendGrid.api(request);
    }

}
