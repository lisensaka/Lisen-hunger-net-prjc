package app.hungernet.controllers;

import app.hungernet.models.dtos.OrderDto;
import app.hungernet.models.enums.OrderStatus;
import app.hungernet.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static app.hungernet.constants.ApplicationConstantsConfigs.ORDER_API;

@RestController
@RequestMapping(ORDER_API)
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<Page<?>> getAllOrders(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size,
            @RequestParam(value = "orderBy", defaultValue = "createdAt", required = false) String createdAt,
            Principal principal
    ) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(createdAt)));
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrders(pageable, principal.getName()));
    }

    @GetMapping("/filter-by-orderStatus/{orderStatus}")
    public ResponseEntity<?> filterOrdersByStatus(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "5", required = false) int size,
            @RequestParam(value = "orderBy", defaultValue = "createdAt", required = false) String createdAt,
            @PathVariable OrderStatus orderStatus,
            Principal principal) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(createdAt)));
        return ResponseEntity.status(HttpStatus.OK).body(orderService.filterOrdersByStatus(orderStatus, principal.getName(), pageable));
    }

    @PostMapping
    public ResponseEntity<?> addNewOrder(@RequestBody OrderDto orderDto, Principal principal) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addNewOrder(orderDto, principal.getName()));
    }


    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrderById(@PathVariable String orderId, @RequestBody OrderDto orderDto, Principal principal) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.updateOrderById(orderId, orderDto, principal.getName()));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrderById(@PathVariable String orderId) throws Exception {
        orderService.deleteOrderById(orderId);
        return ResponseEntity.status(HttpStatus.OK).body("Order was deleted successfully");
    }

}
