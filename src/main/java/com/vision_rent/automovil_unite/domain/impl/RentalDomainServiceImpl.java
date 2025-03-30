package com.vision_rent.automovil_unite.domain.impl;



import com.vision_rent.automovil_unite.domain.entity.Rental;
import com.vision_rent.automovil_unite.domain.entity.User;
import com.vision_rent.automovil_unite.domain.entity.Vehicle;
import com.vision_rent.automovil_unite.domain.exception.InvalidRentalOperationException;
import com.vision_rent.automovil_unite.domain.service.RentalDomainService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Implementación del servicio de dominio para operaciones relacionadas con alquileres.
 */
@Service
public class RentalDomainServiceImpl implements RentalDomainService {

    private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.10");  // 10%
    private static final BigDecimal LATE_FEE_RATE = new BigDecimal("0.15");  // 15%
    private static final int LATE_RETURN_THRESHOLD_MINUTES = 30;
    
    @Override
    public boolean isVehicleAvailable(Vehicle vehicle, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (!vehicle.isAvailable()) {
            return false;
        }
        
        if (startDateTime.isBefore(LocalDateTime.now())) {
            return false;
        }
        
        if (startDateTime.isAfter(endDateTime)) {
            return false;
        }
        
        // La lógica para verificar si hay otros alquileres activos en ese rango
        // de fechas se implementará en la capa de aplicación, ya que requiere
        // consultar el repositorio.
        
        // Verificar período de espera después del último alquiler
        if (vehicle.getLastRentalEnd() != null) {
            LocalDateTime availableAfter = vehicle.getLastRentalEnd().plusDays(1);
            if (startDateTime.isBefore(availableAfter)) {
                return false;
            }
        }
        
        return true;
    }

    @Override
    public BigDecimal calculateRentalPrice(Vehicle vehicle, User renter, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // Calcular días de alquiler (mínimo 1 día)
        long days = Duration.between(startDateTime, endDateTime).toDays();
        if (days < 1) days = 1;
        
        BigDecimal basePrice = vehicle.getPricePerDay().multiply(BigDecimal.valueOf(days));
        
        // Aplicar descuento si el usuario es elegible
        if (renter.isEligibleForDiscount()) {
            BigDecimal discount = basePrice.multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP);
            basePrice = basePrice.subtract(discount);
        }
        
        return basePrice;
    }

    @Override
    public Rental applyDiscount(Rental rental, User renter) {
        if (!renter.isEligibleForDiscount()) {
            return rental;
        }
        
        BigDecimal discount = rental.getTotalPrice().multiply(DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP);
        
        rental.setDiscountApplied(true);
        rental.setDiscountAmount(discount);
        rental.setTotalPrice(rental.getTotalPrice().subtract(discount));
        
        return rental;
    }

    @Override
    public boolean canExtendRental(Rental rental, LocalDateTime newEndDateTime) {
        if (!rental.isActive()) {
            return false;
        }
        
        if (newEndDateTime.isBefore(rental.getEndDateTime())) {
            return false;
        }
        
        // En el servicio de aplicación se verificará si existen otros alquileres
        // en el nuevo rango de fechas
        
        return true;
    }

    @Override
    public BigDecimal calculateLateFee(Rental rental) {
        if (rental.getActualReturnDateTime() == null || rental.getEndDateTime() == null) {
            return BigDecimal.ZERO;
        }
        
        Duration lateTime = Duration.between(rental.getEndDateTime(), rental.getActualReturnDateTime());
        
        if (lateTime.toMinutes() <= LATE_RETURN_THRESHOLD_MINUTES) {
            return BigDecimal.ZERO;
        }
        
        return rental.getTotalPrice().multiply(LATE_FEE_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public Rental returnVehicle(Rental rental, LocalDateTime returnDateTime) {
        if (!rental.isActive() && !"CONFIRMED".equals(rental.getStatus())) {
            throw new InvalidRentalOperationException("No se puede devolver un vehículo de un alquiler que no está activo");
        }
        
        rental.setActualReturnDateTime(returnDateTime);
        rental.setStatus("COMPLETED");
        
        // Verificar si la devolución fue tardía
        Duration lateTime = Duration.between(rental.getEndDateTime(), returnDateTime);
        boolean isLate = lateTime.toMinutes() > LATE_RETURN_THRESHOLD_MINUTES;
        
        rental.setLateReturn(isLate);
        
        if (isLate) {
            BigDecimal lateFee = calculateLateFee(rental);
            rental.setLateReturnFee(lateFee);
            rental.setTotalPrice(rental.getTotalPrice().add(lateFee));
        }
        
        return rental;
    }

    @Override
    public boolean shouldBanUser(Rental rental, User renter) {
        if (rental.getActualReturnDateTime() == null || rental.getEndDateTime() == null) {
            return false;
        }
        
        Duration lateTime = Duration.between(rental.getEndDateTime(), rental.getActualReturnDateTime());
        return lateTime.toMinutes() > LATE_RETURN_THRESHOLD_MINUTES;
    }
}