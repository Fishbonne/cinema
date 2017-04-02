package com.gpsolutions.rest.dao;


import com.gpsolutions.rest.entities.Reservation;
import com.gpsolutions.rest.entities.Session;

import java.io.IOException;
import java.util.List;

public interface Dao {

    List<Session> getSchedule() throws IOException;

    boolean bookSeats(int sessionId, String surName, int seats) throws IOException;

    boolean cancelReservation(int reservationId) throws IOException;

    Reservation getReserveInfo(int reservationId) throws IOException;

}
