package com.sangto.rental_car_server.controller;

import com.sangto.rental_car_server.constant.Endpoint;
import com.sangto.rental_car_server.domain.dto.booking.AddBookingRequestDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingDetailResponseDTO;
import com.sangto.rental_car_server.domain.dto.booking.BookingResponseDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaRequestDTO;
import com.sangto.rental_car_server.domain.dto.meta.MetaResponseDTO;
import com.sangto.rental_car_server.response.MetaResponse;
import com.sangto.rental_car_server.response.Response;
import com.sangto.rental_car_server.service.BookingService;
import com.sangto.rental_car_server.utility.AuthUtil;
import com.sangto.rental_car_server.utility.JwtTokenUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Tag(name = "Bookings")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BookingController {
    private final BookingService bookingService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping(Endpoint.V1.Booking.LIST_FOR_USER)
    public ResponseEntity<MetaResponse<MetaResponseDTO, List<BookingResponseDTO>>> getListBookingForUser(
            HttpServletRequest servletRequest, @ParameterObject MetaRequestDTO metaRequestDTO) throws ParseException {
        Integer userId =
                Integer.valueOf(jwtTokenUtil.getAccountId(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)));
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getListBookingForUser(metaRequestDTO, userId));
    }

    @GetMapping(Endpoint.V1.Booking.DETAILS)
    public ResponseEntity<Response<BookingDetailResponseDTO>> getDetailBooking(
            @PathVariable(name = "id") Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.getDetailBooking(bookingId));
    }

    @PostMapping(Endpoint.V1.Booking.BASE)
    public ResponseEntity<Response<BookingDetailResponseDTO>> addBooking(
            HttpServletRequest servletRequest, @RequestBody @Valid AddBookingRequestDTO requestDTO) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.addBooking(AuthUtil.getRequestedUser().getId(), requestDTO));
    }

//    @PatchMapping(Endpoint.V1.Booking.DETAILS)
//    public ResponseEntity<Response<BookingDetailResponseDTO>> updateBooking(
//            @PathVariable("id") Integer bookingId, @RequestBody @Valid UpdBookingRequestDTO requestDTO) {
//        return ResponseEntity.status(HttpStatus.OK).body(bookingService.updateBooking(bookingId, requestDTO));
//    }

    @PatchMapping(Endpoint.V1.Booking.CONFIRM_BOOKING)
    public ResponseEntity<Response<String>> confirmBooking(@PathVariable(name = "id") Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.confirmBooking(
                        bookingId, AuthUtil.getRequestedUser().getId()));
    }

    @PatchMapping(Endpoint.V1.Booking.CONFIRM_PICK_UP)
    public ResponseEntity<Response<String>> confirmPickUp(@PathVariable(name = "id") Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK).body(bookingService.confirmPickup(bookingId));
    }

    @PatchMapping(Endpoint.V1.Booking.CONFIRM_PAYMENT)
    public ResponseEntity<Response<String>> confirmPayment(@PathVariable(name = "id") Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.confirmPayment(AuthUtil.getRequestedUser().getId(), bookingId));
    }

    @PatchMapping(Endpoint.V1.Booking.CANCELLED_BOOKING)
    public ResponseEntity<Response<String>> cancelBooking(@PathVariable(name = "id") Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.cancelBooking(AuthUtil.getRequestedUser().getId(), bookingId));
    }

    @PatchMapping(Endpoint.V1.Booking.RETURN_CAR)
    public ResponseEntity<Response<String>> returnCar(@PathVariable(name = "id") Integer bookingId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(bookingService.returnCar(AuthUtil.getRequestedUser().getId(), bookingId));
    }

//    @PostMapping(Endpoint.V1.Booking.FEEDBACK)
//    public ResponseEntity<Response<Feedback>> giveRating(
//            @PathVariable(name = "id") Integer bookingId, @RequestBody @Valid AddFeedbackRequestDTO requestDTO) {
//        Feedback feedback =
//                feedbackService.addFeedback(AuthUtil.getRequestedUser().getId(), bookingId, requestDTO);
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(Response.successfulResponse("Send feedback successfully", feedback));
//    }
}
