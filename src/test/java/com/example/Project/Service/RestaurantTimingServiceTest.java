package com.example.Project.Service;

import com.example.Project.model.RestaurantTiming;
import com.example.Project.repository.RestaurantTImingRepository;
import com.example.Project.service.RestaurantTimingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantTimingServiceTest {

    @Mock
    private RestaurantTImingRepository restaurantTImingRepository;

    @InjectMocks
    private RestaurantTimingService restaurantTimingService;

    @Test
    void isRestaurantOpen_success(){
        RestaurantTiming restaurantTiming = new RestaurantTiming();
        restaurantTiming.setId(1);
        restaurantTiming.setOpenTime(LocalTime.now().minusHours(1));
        restaurantTiming.setCloseTime(LocalTime.now().plusHours(1));
        restaurantTiming.setOpen(true);

        when(restaurantTImingRepository.findById(1)).thenReturn(Optional.of(restaurantTiming));

        boolean result = restaurantTimingService.isRestaurantOpen();
        assertTrue(result);
        verify(restaurantTImingRepository,times(1)).findById(1);
    }
    @Test
    void isRestaurantOpen_noTimingSet(){
        when(restaurantTImingRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> restaurantTimingService.isRestaurantOpen());

        assertEquals("No Timming set!", ex.getMessage());
        verify(restaurantTImingRepository,times(1)).findById(1);
    }

    @Test
    void isRestaurantOpen_restaurantClosedFlagFalse() {

        RestaurantTiming timing = new RestaurantTiming();
        timing.setOpen(false);

        when(restaurantTImingRepository.findById(1)).thenReturn(Optional.of(timing));

        boolean result = restaurantTimingService.isRestaurantOpen();

        assertFalse(result);
        verify(restaurantTImingRepository,times(1)).findById(1);
    }

}
