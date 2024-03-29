package com.crud.tasks.service;
import java.util.Optional;
import com.crud.tasks.config.AdminConfig;
import com.crud.tasks.domain.CreatedTrelloCard;
import com.crud.tasks.domain.Mail;
import com.crud.tasks.domain.TrelloBoardDto;
import com.crud.tasks.domain.TrelloCardDto;
import com.crud.tasks.trello.client.TrelloClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrelloService {
    @Autowired
    private AdminConfig adminConfig;
    @Autowired
    private TrelloClient trelloClient;
    @Autowired
    private SimpleEmailService emailService;
    public static final String SUBJECT = "Tasks: New trello card.";
    public List<TrelloBoardDto> fetchTrelloBoards(){
        return trelloClient.getTrelloBoards();
    }
    public CreatedTrelloCard createTrelloCard(final TrelloCardDto trelloCardDto){
        CreatedTrelloCard newCard = trelloClient.createTrelloCard(trelloCardDto);
        Optional.ofNullable(newCard).ifPresent(card-> emailService.send(new Mail(adminConfig.getAdminMail(),SUBJECT,"New card: "+card.getName()+ " has been created on your trello account.")));
        return newCard;
    }
}
