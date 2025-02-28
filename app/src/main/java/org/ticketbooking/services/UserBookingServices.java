package org.ticketbooking.services;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ticketbooking.entities.*;
import org.ticketbooking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class UserBookingServices {
    private ObjectMapper objectMapper=new ObjectMapper();
    private User user;
    private List<User> usersList;
    private final String USERS_PATH="app/src/main/java/org.ticketbooking/localDb/users.json" ;
    public UserBookingServices(User user1) throws IOException {  // A
        this.user=user1;
        loadUserListFromFile();
    }
    public UserBookingServices() throws IOException {  // A
        loadUserListFromFile();
    }
    public void loadUserListFromFile() throws IOException{
        File users=new File(USERS_PATH);
        usersList=objectMapper.readValue(users, new TypeReference<List<User>>() {}); //IOException <- no users of App
    }
    public Boolean loginUser(){
        // If no user Signup -> Login => NPE
        Optional<User> foundUser=usersList.stream().filter(user1 -> {
            return user1.getUserName().equalsIgnoreCase(user.getUserName()) && UserServiceUtil.checkPassword(user.getHashedPassword(),user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User user1){
        try {
            usersList.add(user1);
            saveUsersListToFile();
            return Boolean.TRUE;
        }catch (IOException exception){
            return Boolean.FALSE;
        }
    }
    private void saveUsersListToFile() throws IOException{
        // De-Serialization => json -> file
        File usersFile=new File(USERS_PATH);
        objectMapper.writeValue(usersFile,usersList);

    }
    public void fetchBooking(){
        //If user recently signedUp
        Optional<User> userFetched = usersList.stream().filter(user1 -> {
            return user1.getUserName().equals(user.getUserName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        if(userFetched.isPresent()){
            userFetched.get().showTickets();
        }
    }
    public Boolean cancelBooking(String ticketId){

       /* for (Ticket ticket: user.getTicketsBooked()) {
            if (ticket.getTicketId().equalsIgnoreCase(ticketId)){
                user.getTicketsBooked().remove(ticket);
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;*/

        Scanner s = new Scanner(System.in);
        System.out.println("Enter the ticket id to cancel");
        ticketId = s.next();

        if (ticketId == null || ticketId.isEmpty()) {
            System.out.println("Ticket ID cannot be null or empty.");
            return Boolean.FALSE;
        }

        String finalTicketId1 = ticketId;  //Because strings are immutable
        boolean removed = user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(finalTicketId1));

        String finalTicketId = ticketId;
        user.getTicketsBooked().removeIf(Ticket -> Ticket.getTicketId().equals(finalTicketId));
        if (removed) {
            System.out.println("Ticket with ID " + ticketId + " has been canceled.");
            return Boolean.TRUE;
        }else{
            System.out.println("No ticket found with ID " + ticketId);
            return Boolean.FALSE;
        }


    }
    public List<Train> getTrains(String source, String destination){
        try{
            TrainServices trainService = new TrainServices();
            return trainService.searchTrains(source, destination);
        }catch(IOException ex){
            return new ArrayList<>();
        }
    }
    public List<List<Integer>> fetchSeats(Train train){
        return train.getSeats();
    }
    public Boolean bookTrainSeat(Train train, int row, int seat) {
        try{
            TrainServices trainService = new TrainServices();
            List<List<Integer>> seats = train.getSeats();
            if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
                //seat isAvailable (0)
                if (seats.get(row).get(seat) == 0) {

                    //seat isBooked by user
                    seats.get(row).set(seat, 1);

                    // Updated seatsAvailability for other users
                    train.setSeats(seats);

                    // records stored of user -> travelling to this train
                    trainService.addTrain(train);

                    // Booking successful
                    return true;
                } else {

                    // Seat(1) -> already booked
                    return false;
                }
            } else {
                return false; // Invalid row or seat index
            }
        }catch (IOException ex){
            return Boolean.FALSE;
        }
    }


}
