package com.sangto.rental_car_server.utility;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RentalCalculateUtil {

    public static Long calculateHour(Date startDate, Date endDate) {
        long differenceMillis = endDate.getTime() - startDate.getTime();
        return TimeUnit.MILLISECONDS.toHours(differenceMillis);
    }

    public static Double calculatePrice(Date startDate, Date endDate, Double basePrice) {
        long differenceInHours = calculateHour(startDate, endDate);
        return differenceInHours * (basePrice / 24);
    }
}
