package com.gpsolutions.rest.dao;

import com.gpsolutions.rest.entities.Reservation;
import com.gpsolutions.rest.entities.Session;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DaoImpl implements Dao {
    private final File SCHEDULE = new File("C:\\Users\\Semen\\IdeaProjects\\cinema\\SCHEDULE.txt");
    private final File RESERVATIONS = new File("C:\\Users\\Semen\\IdeaProjects\\cinema\\RESERVATIONS.txt");

    @Override
    public List<Session> getSchedule() throws IOException {
        synchronized (SCHEDULE) {
            BufferedReader reader = new BufferedReader(new FileReader(SCHEDULE));
            String sample;
            List<Session> schedule = new ArrayList<>();
            while ((sample = reader.readLine()) != null) {
                String[] tmpArray = sample.split("\\s");
                int id = Integer.parseInt(tmpArray[0]);
                LocalDateTime dateTime = LocalDateTime.parse(tmpArray[1]);
                String film = tmpArray[2];
                int countSeats = Integer.parseInt(tmpArray[3]);
                schedule.add(new Session(id, dateTime, film, countSeats));
            }
            reader.close();
            return schedule;
        }
    }

    @Override
    public boolean bookSeats(int sessionId, String surName, int seats) throws IOException {
        synchronized (SCHEDULE) {
            synchronized (RESERVATIONS) {
                BufferedReader reader = new BufferedReader(new FileReader(SCHEDULE));
                String sample;
                String data = "";
                String sourceLine = null;
                String editedLine;
                String[] tmpArray;
                while ((sample = reader.readLine()) != null) {
                    tmpArray = sample.split("\\s");
                    if (Integer.parseInt(tmpArray[0]) == sessionId) {
                        sourceLine = sample;
                    }
                    data += sample + "\n";
                }
                reader.close();
                if (sourceLine == null) return false;
                tmpArray = sourceLine.split("\\s");
                int seatsAvailable = Integer.parseInt(tmpArray[3]);
                if (seatsAvailable < seats) return false;
                seatsAvailable -= seats;
                editedLine = sourceLine;
                StringBuilder b = new StringBuilder(editedLine);

                b.replace(editedLine.lastIndexOf(tmpArray[3]), b.length(), "");
                b.append(seatsAvailable + "");
                editedLine = b.toString();
                data = data.replace(sourceLine, editedLine);
                FileWriter writer = new FileWriter(SCHEDULE);
                writer.write(data.substring(0, data.length() - 1));
                writer.flush();
                writer.close();
                reader = new BufferedReader(new FileReader(RESERVATIONS));
                while (reader.ready()) {
                    sample = reader.readLine();
                }
                reader.close();
                tmpArray = sample != null ? sample.split("\\s") : new String[0];
                int lastId = Integer.parseInt(tmpArray[0]) + 1;
                writer = new FileWriter(RESERVATIONS, true);
                writer.write("\n" + lastId + " " + sessionId + " " + surName + " " + seats);
                writer.flush();
                writer.close();
                return true;
            }
        }
    }

    @Override
    public boolean cancelReservation(int reservationId) throws IOException {
        synchronized (RESERVATIONS) {
            synchronized (SCHEDULE) {
                BufferedReader reader = new BufferedReader(new FileReader(RESERVATIONS));
                String sample;
                String data = "";
                String sourceLine = null;
                String editedLine;
                String[] tmpArray;
                while ((sample = reader.readLine()) != null) {
                    tmpArray = sample.split("\\s");
                    if (Integer.parseInt(tmpArray[0]) == reservationId) {
                        sourceLine = sample;
                        continue;
                    }
                    data += sample + "\n";
                }
                reader.close();
                if (sourceLine == null) return false;
                tmpArray = sourceLine.split("\\s");
                int seats = Integer.parseInt(tmpArray[3]);
                int sessionId = Integer.parseInt(tmpArray[1]);
                FileWriter writer = new FileWriter(RESERVATIONS);
                writer.write(data.substring(0, data.length() - 1));
                writer.flush();
                writer.close();

                data = "";
                reader = new BufferedReader(new FileReader(SCHEDULE));
                while ((sample = reader.readLine()) != null) {
                    tmpArray = sample.split("\\s");
                    if (Integer.parseInt(tmpArray[0]) == sessionId) {
                        sourceLine = sample;
                    }
                    data += sample + "\n";
                }
                tmpArray = sourceLine.split("\\s");
                int seatsAvailable = Integer.parseInt(tmpArray[3]);
                seatsAvailable += seats;
                editedLine = sourceLine;
                StringBuilder b = new StringBuilder(editedLine);

                b.replace(editedLine.lastIndexOf(tmpArray[3]), b.length(), "");
                b.append(seatsAvailable + "");
                editedLine = b.toString();

                data = data.replace(sourceLine, editedLine);
                writer = new FileWriter(SCHEDULE);
                writer.write(data);
                writer.flush();
                writer.close();
                return true;
            }
        }
    }

    @Override
    public Reservation getReserveInfo(int reservationId) throws IOException {
        synchronized (RESERVATIONS) {
            BufferedReader reader = new BufferedReader(new FileReader(RESERVATIONS));
            String sample;
            Reservation reservation = null;
            while ((sample = reader.readLine()) != null) {
                String[] tmpArray = sample.split("\\s");
                if (Integer.parseInt(tmpArray[0]) == reservationId) {
                    int id = Integer.parseInt(tmpArray[0]);
                    int sessionId = Integer.parseInt(tmpArray[1]);
                    String surName = tmpArray[2];
                    int seats = Integer.parseInt(tmpArray[3]);
                    reservation = new Reservation(id, sessionId, surName, seats);
                    break;
                }
            }
            reader.close();
            return reservation;
        }
    }
}
