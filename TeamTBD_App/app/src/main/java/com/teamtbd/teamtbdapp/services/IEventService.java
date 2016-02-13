package com.teamtbd.teamtbdapp.services;


public interface IEventService {
    /**
     * Créé un nouvel événement.
     * @param eventName Nom de l'événement.
     * @param hostID ID de l'utilisateur qui host l'événement
     * @return L'ID de l'événement créé.
     */
    public void createEvent(String eventName, String hostID, int price);


    /**
     * Ajoute des tickets pour un événement à un utilisateur.
     * @param eventID ID de l'événement.
     * @param userID ID de l'utilisateur.
     * @param qty Quantité de tickets à ajouter.
     */
    public void getTickets(String eventID, String userID, int qty);
}
