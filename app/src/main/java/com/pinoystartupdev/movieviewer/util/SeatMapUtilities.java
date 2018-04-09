package com.pinoystartupdev.movieviewer.util;

import android.graphics.Color;
import android.support.v4.util.Pair;
import android.util.Log;

import com.pinoystartupdev.movieviewer.pojo.SeatMap;

import java.util.ArrayList;
import java.util.List;

import by.anatoldeveloper.hallscheme.hall.HallScheme;
import by.anatoldeveloper.hallscheme.hall.Seat;

public class SeatMapUtilities {
    private static Seat[][] basicScheme() {
        Seat seats[][] = new Seat[5][10];
        for (int i = 0; i < 5; i++)
            for(int j = 0; j < 10; j++) {
                SeatExample seat = new SeatExample();
                seat.id = i * 10 + (j+1);
                seat.selectedSeatMarker = "\u2713";
                seat.status = HallScheme.SeatStatus.FREE;
                seats[i][j] = seat;
            }
        return seats;
    }

    private static Seat[][] basicSchemeWithMarker() {
        Seat seats[][] = new Seat[12][18];
        int k = 0;
        for (int i = 0; i < 12; i++)
            for(int j = 0; j < 18; j++) {
                SeatExample seat = new SeatExample();
                seat.id = ++k;
                seat.selectedSeatMarker = String.valueOf(i+1);
                seat.status = HallScheme.SeatStatus.BUSY;
                if (j == 0 || j == 17) {
                    seat.status = HallScheme.SeatStatus.EMPTY;
                    if (i > 2 && i < 10) {
                        seat.marker = String.valueOf(i);
                        seat.status = HallScheme.SeatStatus.INFO;
                    }
                }
                if (((j > 0 && j < 3) || (j > 14 && j < 17)) && i == 0) {
                    seat.status = HallScheme.SeatStatus.EMPTY;
                    if (j == 2 || j == 15) {
                        seat.marker = String.valueOf(i+1);
                        seat.status = HallScheme.SeatStatus.INFO;
                    }
                }
                if (((j > 0 && j < 2) || (j > 15 && j < 17)) && i == 1) {
                    seat.status = HallScheme.SeatStatus.EMPTY;
                    if (j == 1 || j == 16) {
                        seat.marker = String.valueOf(i+1);
                        seat.status = HallScheme.SeatStatus.INFO;
                    }
                }
                if (i == 2)
                    seat.status = HallScheme.SeatStatus.EMPTY;
                if (i > 9 && (j == 1 || j == 16)) {
                    seat.status = HallScheme.SeatStatus.INFO;
                    seat.marker = String.valueOf(i);
                }
                seats[i][j] = seat;
            }
        return seats;
    }

    public static Seat[][] generateSeatmap(SeatMap seatMap) {
        /*generate two dimensional array from seatMap object*/

        int rowCount = seatMap.getSeatPlacementList().size();
        int columnCount = seatMap.getSeatPlacementList().get(0).size();

//        for (List<String> row : seatMap.getSeatPlacementList()) {
//            for (String seatNumber : row) {
//                columnCount ++;
//            }
//        }

        Log.e("zdxcvklasdf", "rowCount=" + rowCount);
        Log.e("zdxcvklasdf", "columnCount=" + columnCount);

//        rowCount = 13;
//        columnCount = 36;

        columnCount += 2;

        Seat seats[][] = new Seat[rowCount][columnCount];

//        char rowId = 'A';

//        for (int i = 0; i < rowCount; i++) {
//            for(int j = 0; j < columnCount; j++) {
//                Log.e("zdxcvklasdf", "Creating seat for seat[" + (j+1) + "]");
//                SeatExample seat = new SeatExample();
//
//                if (j == 0 || j == columnCount - 1) {
//                    seat.status = HallScheme.SeatStatus.INFO;
//                    seat.marker = String.valueOf(rowId);
//                } else {
//                    seat.id = i * columnCount + (j+1);
//                    seat.selectedSeatMarker = "\u2713";
//
//                    if (i == 2) {
//                        seat.status = HallScheme.SeatStatus.BUSY;
//                    } else {
//                        seat.status = HallScheme.SeatStatus.FREE;
//                    }
//                }
//
//                seats[i][j] = seat;
//            }
//
//            rowId ++;
//        }

//        seats = basicSchemeWithMarker();

        /*
        *
        * LOGS OF SEAT PLACEMENT
        *
        * */

        Log.e("ajkhsdf", String.valueOf(seatMap.getSeatAvailablility().getSeatCount()));

//                Log.e("ajkhsdf", "++++++++++++++++++++++++++++++++++++++++++++++");
//                Log.e("ajkhsdf", "+++++++++++++++ SEAT PLACEMENT +++++++++++++++");
//                Log.e("ajkhsdf", "++++++++++++++++++++++++++++++++++++++++++++++");
//
//                char alphabet = 'A';
//
//                for (List<String> row : seatMap.getSeatPlacementList()) {
//                    Log.e("ajkhsdf", "=============ROw "  + alphabet + "=============");
//
//                    for (String seatNumber : row) {
//                        switch (seatNumber) {
//                            case "a(30)":
//                                Log.e("ajkhsdf", "[AISLE]");
//                                break;
//                            case "b(20)":
//                                Log.e("ajkhsdf", "[AISLE]");
//                                break;
//                            default:
//                                Log.e("ajkhsdf", "SEAT_NUMBER>>>" + seatNumber);
//                        }
//                    }
//
//                    alphabet++;
//                }

        Log.e("ajkhsdf", "+++++++++++++++++++++++++++++++++++++++++++++++++");
        Log.e("ajkhsdf", "+++++++++++++++ SEAT AVAILABILITY +++++++++++++++");
        Log.e("ajkhsdf", "+++++++++++++++++++++++++++++++++++++++++++++++++");

        char rowId = 'A';
        int availableSeats = 0;
        int reservedSeats = 0;
        int totalSeats = 0;
        int placeholder = 0;

        List<List<String>> seatPlacementList = seatMap.getSeatPlacementList();

        for (int i = 0; i < seatPlacementList.size(); i++) {
            List<String> seatNumberList = seatPlacementList.get(i);

            Log.e("ajkhsdf", "=============ROw "  + rowId + "=============");
            seatNumberList.add(0, "LABEL");
            seatNumberList.add(seatNumberList.size(), "LABEL");

            for (int j = 0; j < seatNumberList.size(); j++) {
                String seatNumber = seatNumberList.get(j);

                SeatExample seat = new SeatExample();

                switch (seatNumber) {
                    case "a(30)":
                    case "b(20)":
                    case "A33":
                        String left = "";
                        String right = "";

                        try {
                            left = seatNumberList.get(seatNumberList.indexOf(seatNumber) - 1) + "<<<";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            right = ">>>" + seatNumberList.get(seatNumberList.indexOf(seatNumber) + 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Log.e("ajkhsdf", left  + "[AISLE]" + right);
                        seat.status = HallScheme.SeatStatus.EMPTY;
                        placeholder++;
                        break;
                    case "LABEL":
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.marker = String.valueOf(rowId);
                        placeholder++;
                        break;
                    default:
                        if (seatMap.getSeatAvailablility().getAvailableSeatsList().indexOf(seatNumber) != -1) {
                            Log.e("ajkhsdf", seatNumber + " AVAILABLE");

                            seat.id = i * columnCount + (j+1);
                            seat.selectedSeatMarker = "\u2713";
                            seat.status = HallScheme.SeatStatus.FREE;
                            availableSeats ++;
                        } else {
                            Log.e("ajkhsdf", seatNumber + " RESERVED");

                            seat.status = HallScheme.SeatStatus.BUSY;

                            reservedSeats ++;
                        }

                        totalSeats ++;
                }

                seats[i][j] = seat;
            }

            rowId++;
        }

//        for (List<String> row : seatMap.getSeatPlacementList()) {
//            Log.e("ajkhsdf", "=============ROw "  + alphabet + "=============");
//            row.add(0, "LABEL");
//            row.add(row.size() - 1, "LABEL");
//
//            for (String seatNumber : row) {
//                switch (seatNumber) {
//                    case "a(30)":
//                    case "b(20)":
//                    case "A33":
//                        String left = "";
//                        String right = "";
//
//                        try {
//                            left = row.get(row.indexOf(seatNumber) - 1) + "<<<";
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        try {
//                            right = ">>>" + row.get(row.indexOf(seatNumber) + 1);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        Log.e("ajkhsdf", left  + "[AISLE]" + right);
//                        placeholder++;
//                        break;
//                    case "LABEL":
//                        break;
//                    default:
//                        if (seatMap.getSeatAvailablility().getAvailableSeatsList().indexOf(seatNumber) != -1) {
//                            Log.e("ajkhsdf", seatNumber + " AVAILABLE");
//
//                            availableSeats ++;
//                        } else {
//                            Log.e("ajkhsdf", seatNumber + " RESERVED");
//
//                            reservedSeats ++;
//                        }
//
//                        totalSeats ++;
//                }
//            }
//
//            alphabet++;
//        }

        Log.e("ajkhsdf", totalSeats + "TOTAL SEATS");
        Log.e("ajkhsdf", availableSeats + "AVAILABLE SEATS");
        Log.e("ajkhsdf", reservedSeats + "AVAILABLE SEATS");
        Log.e("ajkhsdf", placeholder + "PLACEHOLDER");

        return seats;
    }

    public static void generateSeatmap(SeatMap seatMap, MySeatMapCallback mySeatMapCallback) {
        /*generate two dimensional array from seatMap object*/

        int rowCount = seatMap.getSeatPlacementList().size();
        int columnCount = seatMap.getSeatPlacementList().get(0).size();

        Log.e("zdxcvklasdf", "rowCount=" + rowCount);
        Log.e("zdxcvklasdf", "columnCount=" + columnCount);

        columnCount += 2;

        Seat seats[][] = new Seat[rowCount][columnCount];
        List<Pair<Integer, String>> seatNumberManifest = new ArrayList<>();

        /*
        *
        * LOGS OF SEAT PLACEMENT
        *
        * */

        Log.e("ajkhsdf", String.valueOf(seatMap.getSeatAvailablility().getSeatCount()));
        Log.e("ajkhsdf", "+++++++++++++++++++++++++++++++++++++++++++++++++");
        Log.e("ajkhsdf", "+++++++++++++++ SEAT AVAILABILITY +++++++++++++++");
        Log.e("ajkhsdf", "+++++++++++++++++++++++++++++++++++++++++++++++++");

        char rowId = 'A';
        int availableSeats = 0;
        int reservedSeats = 0;
        int totalSeats = 0;
        int placeholder = 0;

        List<List<String>> seatPlacementList = seatMap.getSeatPlacementList();

        for (int i = 0; i < seatPlacementList.size(); i++) {
            List<String> seatNumberList = seatPlacementList.get(i);

            Log.e("ajkhsdf", "=============ROw "  + rowId + "=============");
            seatNumberList.add(0, "LABEL");
            seatNumberList.add(seatNumberList.size(), "LABEL");

            for (int j = 0; j < seatNumberList.size(); j++) {
                String seatNumber = seatNumberList.get(j);

                SeatExample seat = new SeatExample();

                switch (seatNumber) {
                    case "a(30)":
                    case "b(20)":
                    case "A33":
                        String left = "";
                        String right = "";

                        try {
                            left = seatNumberList.get(seatNumberList.indexOf(seatNumber) - 1) + "<<<";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            right = ">>>" + seatNumberList.get(seatNumberList.indexOf(seatNumber) + 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Log.e("ajkhsdf", left  + "[AISLE]" + right);
                        seat.status = HallScheme.SeatStatus.EMPTY;
                        placeholder++;
                        break;
                    case "LABEL":
                        seat.status = HallScheme.SeatStatus.INFO;
                        seat.marker = String.valueOf(rowId);
                        placeholder++;
                        break;
                    default:
                        if (seatMap.getSeatAvailablility().getAvailableSeatsList().indexOf(seatNumber) != -1) {
                            Log.e("ajkhsdf", seatNumber + " AVAILABLE");

                            int seatId = i * columnCount + (j+1);

                            seat.id = seatId;
                            seat.selectedSeatMarker = "\u2713";
                            seat.status = HallScheme.SeatStatus.FREE;

                            seatNumberManifest.add(Pair.create(seatId, seatNumber));
                            availableSeats ++;
                        } else {
                            Log.e("ajkhsdf", seatNumber + " RESERVED");

                            seat.status = HallScheme.SeatStatus.BUSY;

                            reservedSeats ++;
                        }

                        totalSeats ++;
                }

                seats[i][j] = seat;
            }

            rowId++;
        }

        Log.e("ajkhsdf", totalSeats + "TOTAL SEATS");
        Log.e("ajkhsdf", availableSeats + "AVAILABLE SEATS");
        Log.e("ajkhsdf", reservedSeats + "AVAILABLE SEATS");
        Log.e("ajkhsdf", placeholder + "PLACEHOLDER");

        mySeatMapCallback.generateSuccess(seats, seatNumberManifest);
    }

    public Pair<Integer, String> getSeatNumber(List<Pair<Integer, String>> seatNumberManifest, Integer id) {
        for (Pair<Integer, String> seatNumber : seatNumberManifest) {
            if (seatNumber.first.equals(id)) {
                return seatNumber;
            }
        }

        return null;
    }

    public void generateLogsForSeatMap (SeatMap seatMap) {
                /*
        *
        * LOGS OF SEAT PLACEMENT
        *
        * */

        Log.e("ajkhsdf", String.valueOf(seatMap.getSeatAvailablility().getSeatCount()));

//                Log.e("ajkhsdf", "++++++++++++++++++++++++++++++++++++++++++++++");
//                Log.e("ajkhsdf", "+++++++++++++++ SEAT PLACEMENT +++++++++++++++");
//                Log.e("ajkhsdf", "++++++++++++++++++++++++++++++++++++++++++++++");
//
//                char alphabet = 'A';
//
//                for (List<String> row : seatMap.getSeatPlacementList()) {
//                    Log.e("ajkhsdf", "=============ROw "  + alphabet + "=============");
//
//                    for (String seatNumber : row) {
//                        switch (seatNumber) {
//                            case "a(30)":
//                                Log.e("ajkhsdf", "[AISLE]");
//                                break;
//                            case "b(20)":
//                                Log.e("ajkhsdf", "[AISLE]");
//                                break;
//                            default:
//                                Log.e("ajkhsdf", "SEAT_NUMBER>>>" + seatNumber);
//                        }
//                    }
//
//                    alphabet++;
//                }

        Log.e("ajkhsdf", "+++++++++++++++++++++++++++++++++++++++++++++++++");
        Log.e("ajkhsdf", "+++++++++++++++ SEAT AVAILABILITY +++++++++++++++");
        Log.e("ajkhsdf", "+++++++++++++++++++++++++++++++++++++++++++++++++");

        char alphabet = 'A';
        int availableSeats = 0;
        int reservedSeats = 0;
        int totalSeats = 0;
        int placeholder = 0;

        for (List<String> row : seatMap.getSeatPlacementList()) {
            Log.e("ajkhsdf", "=============ROw "  + alphabet + "=============");

            for (String seatNumber : row) {
                switch (seatNumber) {
                    case "a(30)":
                    case "b(20)":
                    case "A33":
                        String left = "";
                        String right = "";

                        try {
                            left = row.get(row.indexOf(seatNumber) - 1) + "<<<";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            right = ">>>" + row.get(row.indexOf(seatNumber) + 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Log.e("ajkhsdf", left  + "[AISLE]" + right);
                        placeholder++;
                        break;
                    default:
                        if (seatMap.getSeatAvailablility().getAvailableSeatsList().indexOf(seatNumber) != -1) {
                            Log.e("ajkhsdf", seatNumber + " AVAILABLE");

                            availableSeats ++;
                        } else {
                            Log.e("ajkhsdf", seatNumber + " RESERVED");

                            reservedSeats ++;
                        }

                        totalSeats ++;
                }
            }

            alphabet++;
        }

        Log.e("ajkhsdf", totalSeats + "TOTAL SEATS");
        Log.e("ajkhsdf", availableSeats + "AVAILABLE SEATS");
        Log.e("ajkhsdf", reservedSeats + "AVAILABLE SEATS");
        Log.e("ajkhsdf", placeholder + "PLACEHOLDER");
    }

    public interface MySeatMapCallback {
        void generateSuccess(Seat[][] seats, List<Pair<Integer, String>> seatNumberManifest);
    }

    public static class SeatExample implements Seat {

        public int id;
        public int color = Color.GRAY;
        public String marker;
        public String selectedSeatMarker;
        public HallScheme.SeatStatus status;

        @Override
        public int id() {
            return id;
        }

        @Override
        public int color() {
            return color;
        }

        @Override
        public String marker() {
            return marker;
        }

        @Override
        public String selectedSeat() {
            return selectedSeatMarker;
        }

        @Override
        public HallScheme.SeatStatus status() {
            return status;
        }

        @Override
        public void setStatus(HallScheme.SeatStatus status) {
            this.status = status;
        }

    }
}
